package com.example.web.controller;

import com.example.web.modules.Actions;
import com.example.web.modules.Msg;
import com.example.web.modules.User;
import com.example.web.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/user")
public class UserController extends BaseController{

    //注入service
    @Autowired
    UserService userService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisTemplate redisTemplate;

    //登录接口
    @PostMapping("/login")
    public Msg check_login(HttpServletRequest request, @RequestBody User user){
        //        if ( msg.getCode() == 200 ) {
//            userService.logout(request);
//        }
        return userService.login(request, user);
    }

    //检测用户名是否存在接口
    @PostMapping("/check_user_name")
    public Msg check_user_name(@RequestBody User user){
        if (userService.find_by_user_name(user.getUser_name()) == null){
            return Msg.success("用户名可用");
        } else {
            return Msg.fail("用户名已存在");
        }
    }

    //退出登录接口
    @PostMapping("/logout")
    public Msg logout(HttpServletRequest request){
        return userService.logout(request);
    }

    //注册接口
    @PostMapping("/signBus")
    public Msg signBus(@RequestBody User user){
        if ( userService.signBus(user) != null ){
            return Msg.success("注册成功");
        }
        else{
            return Msg.fail("用户名已存在");
        }
    }
    @PostMapping("/signCus")
    public Msg signCus(@RequestBody User user){
        if ( userService.signCus(user) != null ){
            return Msg.success("注册成功");
        }
        else{
            return Msg.fail("用户名已存在");
        }
    }


    @RequiresPermissions(value = "修改密码")
    @PostMapping("/change_pwd")
    public Msg change_pwd(HttpServletRequest request, @RequestBody Map map){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        if ( userService.change_pwd(user_name,
                map.get("pwd1").toString(), map.get("pwd2").toString()) != null ){
            return Msg.success("修改密码成功");
        } else {
            return Msg.fail("旧密码错误");
        }
    }


    @RequiresPermissions(value = "检测路径")
    @PostMapping("/selectUrlsByUser")
    public Msg selectUrlsByUser(HttpServletRequest request, @RequestBody Map map){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        if ( userService.selectUrlsByUser(user_name, map.get("url_name").toString()) == 1)
            return Msg.success("可以访问");
        else
            return Msg.fail("无权访问");
    }


    @RequiresPermissions(value = "查询操作")
    @PostMapping("/selectActionsByUser")
    public Msg selectActionsByUser(HttpServletRequest request){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        List<Actions> action_list = userService.selectActionsByUser(user_name);
        if (action_list != null){
            return Msg.success("查询成功").add("actions", action_list);
        } else {
            return Msg.fail("权限不足");
        }
    }
}
