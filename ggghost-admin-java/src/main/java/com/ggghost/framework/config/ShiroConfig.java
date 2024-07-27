package com.ggghost.framework.config;

import com.ggghost.framework.shiro.*;
import jakarta.servlet.Filter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *@Author: ggghost
 *@CreateTime: 2024-07-12
 *@Description: shiro配置类
 *@Version: 1.0
 */
@Configuration
public class ShiroConfig {

    /**
     * 配置拦截路径和放心路径
     * @param securityManager
     * @return
     */
    @Bean("shiroFilterFactoryBean")
    public ShiroFilterFactoryBean filterFactoryBean(DefaultSecurityManager securityManager) {
        // shiro过滤器工厂类
        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();

        // 设置 SecurityManager
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        /**
         * 注册自定义过滤器
         */
        Map<String, Filter> filterMap = new LinkedHashMap<>();
        filterMap.put("jwtFilter", new JwtFilter());
        shiroFilterFactoryBean.setFilters(filterMap);

        //拦截器----Map集合
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();

        //配置退出 过滤器,其中的具体的退出代码Shiro已经替我们实现了
        filterChainDefinitionMap.put("/login", "anon");
        filterChainDefinitionMap.put("/favicon.ico", "anon");
        //   /** 匹配所有的路径
        //  通过Map集合组成了一个拦截器链 ，自顶向下过滤，一旦匹配，则不再执行下面的过滤
        //  如果下面的定义与上面冲突，那按照了谁先定义谁说了算
        //  /** 一定要配置在最后
        filterChainDefinitionMap.put("/**", "jwtFilter");

        // 将拦截器链设置到shiro中
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }


    /**
     * 设置securityManager
     * @param securityManager
     * @return
     */
    @Bean
    public InitializingBean shiroInitialization(SecurityManager securityManager) {
        return () -> SecurityUtils.setSecurityManager(securityManager);
    }

    /**
     * 配置安全管理柒，注入realm域
     * @param realm
     * @return
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager(GRealm realm, JwtRealm jwtRealm) {
        ArrayList<Realm> realms = new ArrayList<>();
        realms.add(realm);
        realms.add(jwtRealm);
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealms(realms);
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        // 不需要将ShiroSession中的东西存到任何地方包括Http Session中）
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }


    /**
     * 密码比较器
     * @return
     */
    @Bean
    public GCredentialsMatcher gCredentialsMatcher() {
        return new GCredentialsMatcher();
    }

    /**
     * 配置Realm域，注入密码比较器
     * @param gCredentialsMatcher
     * @return
     */
    @Bean
    public GRealm realm(GCredentialsMatcher gCredentialsMatcher) {
        GRealm gRealm = new GRealm();
        gRealm.setCredentialsMatcher(gCredentialsMatcher);
        return gRealm;
    }

    @Bean
    public JwtRealm jwtRealm(JwtCredentialsMatcher jwtCredentialsMatcher) {
        JwtRealm jwtRealm = new JwtRealm();
        jwtRealm.setCredentialsMatcher(jwtCredentialsMatcher);
        return new JwtRealm();
    }

    @Bean
    public JwtCredentialsMatcher jwtCredentialsMatcher() {
        return new JwtCredentialsMatcher();
    }



    /**
     * 开启shiro aop注解支持
     * 使用代理方式;所以需要开启代码支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }


    /**
     * 开启cglib代理
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        creator.setProxyTargetClass(true);
        return creator;
    }

}
