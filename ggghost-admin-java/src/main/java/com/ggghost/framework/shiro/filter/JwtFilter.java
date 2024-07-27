package com.ggghost.framework.shiro.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggghost.framework.constant.RedisConstant;
import com.ggghost.framework.dto.LoginUser;
import com.ggghost.framework.dto.ResponseInfo;
import com.ggghost.framework.enums.ExceptionEnum;
import com.ggghost.framework.exception.user.UserAuthenticationException;
import com.ggghost.framework.service.ILoginService;
import com.ggghost.framework.service.impl.RedisService;
import com.ggghost.framework.shiro.JwtToken;
import com.ggghost.framework.utlis.IpAddrUtils;
import com.ggghost.framework.utlis.JwtUtils;
import com.ggghost.framework.utlis.SpringBeanUtils;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.redisson.api.RList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-22
 * @Description: jwt拦截器
 * @Version: 1.0
 */
public class JwtFilter extends AuthenticatingFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String X_TOKEN = "x-token";


    /**
     * 创建token
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        String token = this.getToken((HttpServletRequest) servletRequest);
        if (ObjectUtils.isEmpty(token)) {
            throw new UserAuthenticationException();
        }
        return new JwtToken(token);
    }

    /**
     * 兼容跨域
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) {
        return ((HttpServletRequest) servletRequest).getMethod().equals(RequestMethod.OPTIONS.name());
    }

    /**
     * onAccessDenied()是没有携带JwtToken的时候进行账号密码登录，登录成功允许访问，登录失败拒绝访问
     *
     * @param servletRequest
     * @param servletResponse
     * @return 返回结果为true表明登录通过
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        if (!validToken(servletRequest, servletResponse)) {
            log.error("token valid failed");
            return false;
        }
        log.info("token valid success");
        return executeLogin(servletRequest, servletResponse);
    }

    /**
     * 校验token
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     */
    private boolean validToken(ServletRequest servletRequest, ServletResponse servletResponse) {
        try {
            //注入bean
            RedisService redisService = SpringBeanUtils.getBean(RedisService.class);
            JwtUtils jwtUtils = SpringBeanUtils.getBean(JwtUtils.class);
            ILoginService loginService = SpringBeanUtils.getBean(ILoginService.class);
            //获取用户特征
            UserAgent userAgent = IpAddrUtils.getUserAgent();
            String x_token = getToken((HttpServletRequest) servletRequest);
            if (x_token == null) {
                throw new UserAuthenticationException();
            }

            LoginUser user = redisService.<LoginUser>get(RedisConstant.JWT + x_token);
            if (user == null) {
                return false;
            }

            RList<String> rList = redisService.getRList(RedisConstant.JWT_USER + userAgent.getId() + user.getId());
            //同一客户端多请求并发下,一个请求更新了token,但其它请求未更新token,返回最新token
            if (rList.size() > 1 && x_token.equals(rList.getLast())) {
                onLoginToken(servletResponse, rList.getFirst());
                return false;
            }

            //刷新token
            String token = jwtUtils.getClaimFiled(x_token, "token");
            if (jwtUtils.isRefresh(token)) {
                onLoginToken(servletResponse, loginService.createToken(user));
                return false;
            }
            return true;
        } catch (IOException e) {
            log.error("token valid failed!\n\r {}", e.getMessage());
            return false;
        }
    }

    /**
     * 登录成功执行方法
     *
     * @param response
     * @param data
     * @throws IOException
     */
    private void onLoginSuccess(ServletResponse response, Object data) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        ResponseInfo<Object> success = ResponseInfo.success(data);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(success));
    }

    /**
     * 返回刷新token
     *
     * @param response
     * @param token
     * @throws IOException
     */
    private void onLoginToken(ServletResponse response, String token) throws IOException {
        Map data = new HashMap<>();
        data.put("x-token", token);
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        ResponseInfo<Object> success = ResponseInfo.success(data);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(success));
    }

    /**
     * 对跨域访问提供支持
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域发送一个option请求
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(200);
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 获取token
     * 优先从header获取
     * 如果没有，则从parameter获取
     *
     * @param request request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        String token = request.getHeader(X_TOKEN);
        if (ObjectUtils.isEmpty(token)) {
            token = request.getParameter(X_TOKEN);
        }
        return token;
    }

}
