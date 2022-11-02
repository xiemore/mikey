package com.example.web.config;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean=new ShiroFilterFactoryBean();

        //关联 DefaultWebSecurityManager
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);

        //添加shiro内置的过滤器
        /*
         * anon: 无需认证就可以访问
         * authc: 必须认证才能访问
         * user: 必须拥有记住我功能才能用
         * perms: 拥有对某个资源的权限才能访问
         * role: 拥有某个角色权限才能访问
         * */
        //添加过滤器
        Map<String, Filter> filters=new HashMap<>();
        filters.put("auth",new AuthFilter());        //自定义的认证授权过滤器
        filters.put("perms", new PermFilter());        //自定义权限过滤器
        shiroFilterFactoryBean.setFilters(filters);  //添加自定义的认证授权过滤器

        //要拦截的路径放在map里面
        Map<String,String> filterMap=new LinkedHashMap<String,String>();
        filterMap.put("/user/login","anon");  //放行login接口
        filterMap.put("/user/check_user_name", "anon");  //放行检测用户名重复接口
        filterMap.put("/user/signCus", "anon");  //放行注册接口
        filterMap.put("/user/signBus", "anon");  //放行注册接口
        filterMap.put("/user/logout", "anon");  //放行退出登录接口
        filterMap.put("/type/selectAllType", "anon");   //放行获取商品种类接口
        filterMap.put("/good/selectAllGoods", "anon");  //查找放行商品接口
        filterMap.put("/good/selectTotal", "anon");     //放行取得商品数量接口
        filterMap.put("/good/findGoodById", "anon");    //放行商品详情页接口
        filterMap.put("/chat/**", "anon");    //放行websocket
        //下面的是基线需求中购买商品所需接口
        filterMap.put("/want/basicCreateWant", "anon");    //放行基线需求购买商品接口
        filterMap.put("/**","auth");    //拦截所有路径, 它自动会跑到 AuthFilter这个自定义的过滤器里面
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    //配置securityManager的实现类，变向的配置了securityManager
    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(AuthRealm authRealm){
        DefaultWebSecurityManager defaultWebSecurityManager=new DefaultWebSecurityManager();

        //关联realm
        defaultWebSecurityManager.setRealm(authRealm);

        /*
         * 关闭shiro自带的session
         */
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
        defaultWebSecurityManager.setSubjectDAO(subjectDAO);
        return defaultWebSecurityManager;
    }

    //将自定义realm注入到 DefaultWebSecurityManager
    @Bean
    public AuthRealm authRealm(){
        return new AuthRealm();
    }


    //通过调用Initializable.init()和Destroyable.destroy()方法,从而去管理shiro bean生命周期
    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    //开启shiro权限注解
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager defaultWebSecurityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(defaultWebSecurityManager);
        return advisor;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator(){
        DefaultAdvisorAutoProxyCreator advisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        advisorAutoProxyCreator.setProxyTargetClass(true);
        return advisorAutoProxyCreator;
    }
}
