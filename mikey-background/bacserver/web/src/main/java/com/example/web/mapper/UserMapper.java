package com.example.web.mapper;

import com.example.web.modules.Actions;
import com.example.web.modules.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "UserMapper")
public interface UserMapper {
    User check_login(String user_name);     //可用于登录检测和注册用户名检测
    List<String> select_user_role(String user_name);    //用于获取用户接口权限
    void sign(User user);    //用于用户注册
    void set_sign_user_role(int users_id, int roles_id);     //用于设置注册用户的权限
    void change_pwd(String user_name, String pwd);
    List<String> selectUrlsByUser(String user_name, String url_name);      //用于获取用户可访问url(前端)
    List<Actions> selectActionsByUser(String user_name);        //获取用户在主界面可执行的操作
    int check_user_type(String user_name);             //判断用户是商家还是客户
}
