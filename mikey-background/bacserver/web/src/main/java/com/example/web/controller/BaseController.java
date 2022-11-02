package com.example.web.controller;

import com.example.web.modules.Msg;
import com.google.gson.Gson;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public abstract class BaseController {
    @ExceptionHandler({ UnauthorizedException.class, AuthorizationException.class })
    public void authorizationException(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Gson gson=new Gson();
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin",response.getHeader("Origin") );
        response.setCharacterEncoding("UTF-8");
        Msg msg= Msg.denyAccess("没有权限,请先登录");
        response.getWriter().write(gson.toJson(msg));
    }
//    @ExceptionHandler({ SQLException.class})
//    public void sqlException(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        Gson gson=new Gson();
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Origin",response.getHeader("Origin") );
//        response.setCharacterEncoding("UTF-8");
//        Msg msg= Msg.sqlWrong("数据库错误");
//        response.getWriter().write(gson.toJson(msg));
//    }
}
