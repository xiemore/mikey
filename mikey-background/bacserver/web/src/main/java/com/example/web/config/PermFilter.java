package com.example.web.config;

import com.example.web.modules.Msg;
import com.google.gson.Gson;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class PermFilter extends PermissionsAuthorizationFilter {

    Gson gson=new Gson();


    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String[] perms = (String[]) mappedValue;
        Subject subject = this.getSubject(request, response);
        boolean isPermitted = true;
        //允许OPTION请求通过，否则会报错
        if (((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())){
            return true;
        }
        if (perms != null && perms.length > 0) {
            System.out.println(Arrays.toString(perms));
            if (perms.length == 1) {
                if (!subject.isPermitted(perms[0])) {
                    isPermitted = false;
                }
            } else if (!subject.isPermittedAll(perms)) {
                isPermitted = false;
            }
        }
        return isPermitted;
    }

    @Override
    public boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest httpServletRequest= (HttpServletRequest) request;
        HttpServletResponse httpServletResponse= (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Origin",httpServletRequest.getHeader("Origin") );
        httpServletResponse.setCharacterEncoding("UTF-8");
        Msg msg= Msg.denyAccess("没有权限,请先登录");
        httpServletResponse.getWriter().write(gson.toJson(msg));
        //由于axios是ajax请求，无法重定向，所以重定向在前端进行
//            saveRequest(httpServletRequest); //将请求保存起来，如登录成功后再重定向回该请求
//            try {
//                redirectToLogin(httpServletRequest, httpServletResponse);
//            } catch (IOException ex) {
//                throw new RuntimeException(ex);
//            }
        return false;
    }
}
