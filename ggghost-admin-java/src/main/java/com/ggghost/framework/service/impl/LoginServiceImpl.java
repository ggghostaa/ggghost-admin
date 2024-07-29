package com.ggghost.framework.service.impl;

import com.ggghost.framework.constant.RedisConstant;
import com.ggghost.framework.dto.LoginUser;
import com.ggghost.framework.dto.ResponseInfo;
import com.ggghost.framework.entity.SysUser;
import com.ggghost.framework.entity.log.LoginLog;
import com.ggghost.framework.exception.user.UserPasswordNotMatcherException;
import com.ggghost.framework.repository.SysUserRepository;
import com.ggghost.framework.repository.log.LoginLogRepository;
import com.ggghost.framework.service.ILoginService;
import com.ggghost.framework.service.ISysUserService;
import com.ggghost.framework.utlis.IpAddrUtils;
import com.ggghost.framework.utlis.JwtUtils;
import com.ggghost.framework.utlis.bcrypt.BCryptPasswordEncoder;
import eu.bitwalker.useragentutils.UserAgent;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-15
 * @Description:登录注册
 * @Version: 1.0
 */
@Service
public class LoginServiceImpl implements ILoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Autowired
    ISysUserService userService;
    @Autowired
    SysUserRepository sysUserRepository;
    @Autowired
    TransactionTemplate transactionTemplate;
    @Autowired
    RedisService redisService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    LoginLogRepository loginLogRepository;

    /**
     * 登录
     *
     * @param loginUser
     * @return
     */
    @Override
    public ResponseInfo<?> login(LoginUser loginUser) {
        boolean success = false;
        try {
            Subject subject = SecurityUtils.getSubject();
            HashMap<String, Object> result = new HashMap<>();
            subject.login(new UsernamePasswordToken(loginUser.getUsername(), loginUser.getPassword()));
            SysUser sysUser = (SysUser) subject.getPrincipal();

            LoginUser user = new LoginUser(sysUser);
            result.put("user", user);
            result.put("x-token", createToken(new LoginUser(sysUser)));
            success = true;
            return ResponseInfo.success(result, "login success!");
        } catch (AuthenticationException e) {
            throw new UserPasswordNotMatcherException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //增加登录日志，更新最后登录时间
            addLoginLog(loginUser, success);
        }
    }



    @Transactional
    @Override
    public ResponseInfo register(LoginUser user) {
        try {
            SysUser sysUser = sysUserRepository.findUserByUsername(user.getUsername());
            if (sysUser != null) {
                return ResponseInfo.fail("user is exist!");
            }
            sysUser = new SysUser();
            BeanUtils.copyProperties(user, sysUser);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
            sysUser.setStatus(1);
            sysUser.setCreateTime(new Date());
            sysUser.setUpdateTime(new Date());
            sysUserRepository.save(sysUser);
            return ResponseInfo.success("register success!");
        } catch (BeansException e) {
            log.error("register error\n\r {}", e.getMessage());
            return ResponseInfo.fail("register error!");
        }
    }

    /**
     * 添加登录日志
     * @param loginUser
     */
    private void addLoginLog(LoginUser loginUser, boolean success) {
        String ip = IpAddrUtils.getIpAddress();
        UserAgent userAgent = IpAddrUtils.getUserAgent();
        LocalDateTime loginTime = LocalDateTime.now();
        SysUser sysUser = success ? (SysUser)SecurityUtils.getSubject().getPrincipal() : null;
        Executors.newVirtualThreadPerTaskExecutor().execute(()->{
            LoginLog loginLog = new LoginLog();
            loginLog.setIp(ip);
            loginLog.setUsername(loginUser.getUsername());
            loginLog.setSuccess(success);
            loginLog.setDeviceInfo(userAgent.getOperatingSystem().getName() + userAgent.getBrowser());
            loginLog.setLoginTime(loginTime);
            RLock rLock = redisService.getRLock(RedisConstant.LOCK_LOGIN_LOG + ip + loginUser.getUsername());
            if (rLock.tryLock()) {
                try {
                    loginLogRepository.save(loginLog);
                    if (success) {
                        transactionTemplate.execute((status)->{
                            try {
                                sysUser.setLastLoginTime(Date.from(loginTime.atZone(ZoneId.systemDefault()).toInstant()));
                                sysUserRepository.save(sysUser);
                            } catch (Exception e) {
                                status.setRollbackOnly();
                                return false;
                            }
                            return true;
                        });
                    }
                } catch (TransactionException e) {
                    throw new RuntimeException(e);
                } finally {
                    rLock.unlock();
                }
            }
        });


    }

    /**
     * 生成token
     * 二次加密
     * @param user
     * @return
     */
    public String createToken(LoginUser user) {
        //获取用户特征
        UserAgent userAgent = IpAddrUtils.getUserAgent();
        //生成token,二次加密，loginId做key
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String encoding = jwtUtils.encoding(claims, JwtUtils.EXPIRATION_TIME);
        claims = new HashMap<>();
        claims.put("token", encoding);
        String x_token = jwtUtils.encoding(claims);
        //存入redis
        redisService.put(RedisConstant.JWT + x_token, user, Duration.ofDays(7));
        LoginUser user1 = redisService.<LoginUser>get(RedisConstant.JWT + x_token);

        //异步线程维护一个用户登录token表，释放多次登录redis空间
        Executors.newVirtualThreadPerTaskExecutor().execute(()->{
            String key = userAgent.getId() + user.getId();
            RList<String> rList = redisService.getRList(RedisConstant.JWT_USER + key);
            RLock rLock = redisService.getRLock(RedisConstant.LOCK + key);
            //加锁更新
            try {
                if (rLock.tryLock()) {
                    rList.addFirst(x_token);
                    while (rList.size() > 2) {
                        redisService.remove(RedisConstant.JWT + rList.getLast());
                        rList.removeLast();
                    }
                    rList.expire(Duration.ofDays(7));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                rLock.unlock();
            }
        });
        return x_token;
    }
}
