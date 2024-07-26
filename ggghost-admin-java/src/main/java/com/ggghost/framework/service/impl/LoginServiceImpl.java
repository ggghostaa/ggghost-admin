package com.ggghost.framework.service.impl;

import com.ggghost.framework.constant.RedisConstant;
import com.ggghost.framework.dto.LoginUser;
import com.ggghost.framework.dto.ResponseInfo;
import com.ggghost.framework.entity.SysUser;
import com.ggghost.framework.exception.user.UserPasswordNotMatcherException;
import com.ggghost.framework.repository.SysUserRepository;
import com.ggghost.framework.service.ILoginService;
import com.ggghost.framework.service.ISysUserService;
import com.ggghost.framework.utlis.IpAddrUtils;
import com.ggghost.framework.utlis.JwtUtils;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

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

    /**
     * 登录
     *
     * @param loginUser
     * @return
     */
    @Override
    public ResponseInfo<?> login(LoginUser loginUser) {
        try {
            Subject subject = SecurityUtils.getSubject();
            HashMap<String, Object> result = new HashMap<>();
            subject.login(new UsernamePasswordToken(loginUser.getUsername(), loginUser.getPassword()));
            SysUser sysUser = (SysUser) subject.getPrincipal();

            //更新最后登录时间
            transactionTemplate.execute((status)->{
                sysUser.setLastLoginTime(new Date());
                sysUserRepository.save(sysUser);
               return null;
            });
            sysUser.setSalt(null);
            sysUser.setPassword(null);
            LoginUser user = new LoginUser(sysUser);
            result.put("user", user);
            //生成token,二次加密，loginId做key
            String tokenSeq = sysUserRepository.getUserTokenSeq();
            String token = jwtUtils.generateToken(new LoginUser(sysUser));
            String loginId = UUID.randomUUID().toString() + tokenSeq;
            String xToken = jwtUtils.generateToken(token, loginId);
            //获取用户特征
            UserAgent userAgent = IpAddrUtils.getUserAgent();
            redisService.putString(userAgent.getId() + loginId, xToken);

            //维护一个token list，删除重复登录的redis token


            return ResponseInfo.success(result, "登录成功");
        } catch (AuthenticationException e) {
            throw new UserPasswordNotMatcherException();
        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseInfo register(SysUser sysUser) {
        throw new UserPasswordNotMatcherException();
    }
}
