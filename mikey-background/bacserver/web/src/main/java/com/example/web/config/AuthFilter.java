package com.example.web.config;

import com.example.web.modules.Msg;
import com.google.gson.Gson;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


//自定义拦截器
public class AuthFilter extends AuthenticatingFilter {

    Gson gson=new Gson();
    //配置并发用户数限制
    int maxSession = 1;
    SessionManager sessionManager;


    //生成自定义token
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest= (HttpServletRequest) request;
        //从header中获取token
        String token=httpServletRequest.getHeader("token");
        return new AuthToken(token);
    }

    //所有请求全部拒绝访问
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        //允许option请求通过
        return ((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name());
    }

    //拒绝访问的请求，onAccessDenied方法先获取 token，再调用executeLogin方法
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest= (HttpServletRequest) request;
        HttpServletResponse httpServletResponse= (HttpServletResponse) response;
        String token=httpServletRequest.getHeader("token");     //获取请求token
        //StringUtils.isBlank(String str)  判断str字符串是否为空或者长度是否为0或者是"null"
        if(token == null || token.length() == 0 || token.equals("null")){
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpServletResponse.setHeader("Access-Control-Allow-Origin",httpServletRequest.getHeader("Origin") );
            httpServletResponse.setCharacterEncoding("UTF-8");
            Msg msg= Msg.denyAccess("请先登录");
            httpServletResponse.getWriter().write(gson.toJson(msg));
            //由于axios是ajax请求，无法重定向，所以重定向在前端进行
//            saveRequest(httpServletRequest); //将请求保存起来，如登录成功后再重定向回该请求
//            try {
//                redirectToLogin(httpServletRequest, httpServletResponse);
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
            return false;
        } else {
            HttpSession session = httpServletRequest.getSession();
            //根据token去缓存里查找用户名
            String user_name = RedisUtil.use_stringRedisTemplate.opsForValue().get(token);
            String sess = RedisUtil.use_stringRedisTemplate.opsForValue().get(user_name);
            if (!sess.equals(session.getId())){
                //请求重定向
                RedisUtil.use_redisTemplate.opsForList().remove(user_name, 0, session.getId());
                httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
                httpServletResponse.setHeader("Access-Control-Allow-Origin",httpServletRequest.getHeader("Origin") );
                httpServletResponse.setCharacterEncoding("UTF-8");
                Msg msg= Msg.denyAccess("账号异地登录,请重新登录");
                httpServletResponse.getWriter().write(gson.toJson(msg));
                return false;
            }
        }
        return executeLogin(request,response);
    }

    //token失效时调用
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {
        HttpServletRequest httpServletRequest= (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpResponse.setCharacterEncoding("UTF-8");
        //处理登录失败的异常
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        //由于axios是ajax请求，无法重定向，所以重定向在前端进行
//        saveRequest(httpServletRequest); //将请求保存起来，如登录成功后再重定向回该请求
//        try {
//            redirectToLogin(httpServletRequest, httpResponse);
//        } catch (IOException ex) {
//            throw new RuntimeException(ex);
//        }
        Msg msg=Msg.denyAccess("登录凭证已失效，请重新登录");
        try {
            httpResponse.getWriter().write(gson.toJson(msg));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return false;
    }
}
