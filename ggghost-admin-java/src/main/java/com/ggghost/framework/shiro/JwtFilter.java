package com.ggghost.framework.shiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ggghost.framework.dto.ResponseInfo;
import com.ggghost.framework.enums.ExceptionEnum;
import com.ggghost.framework.exception.user.UserAuthenticationException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * @Author: ggghost
 * @CreateTime: 2024-07-22
 * @Description: jwt拦截器
 * @Version: 1.0
 */
@Component
public class JwtFilter extends AuthenticatingFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String X_TOKEN = "X-Token";

    /**
     * 创建token
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
     * @param servletRequest
     * @param servletResponse
     * @throws Exception
     * @return 返回结果为true表明登录通过
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        return executeLogin(servletRequest, servletResponse);
//        log.info("onAccessDenied");
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        String token = request.getHeader("x-token");
//        if (token == null) {
//            // 在这里处理token不存在的情况，例如返回HTTP 401 Unauthorized
//            onLoginFail(servletResponse);
//            return false;
//        }
//        JwtToken jwtToken = new JwtToken(token);
//        try {
//            getSubject(servletRequest, servletResponse).login(jwtToken);
//        } catch (AuthenticationException e) {
//            log.error("Subject login error:", e);
//            onLoginFail(servletResponse);
//            return false;
//        }
//        return true;
    }

    //登录失败要执行的方法
    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseInfo<Object> data = ResponseInfo.fail(ExceptionEnum.NOT_LOGIN_ERROR);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(data));
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
     * @param request request
     * @return token
     */
    private String getToken(HttpServletRequest request){
        String token = request.getHeader(X_TOKEN);
        if(ObjectUtils.isEmpty(token)){
            token = request.getParameter(X_TOKEN);
        }
        return token;
    }

}
