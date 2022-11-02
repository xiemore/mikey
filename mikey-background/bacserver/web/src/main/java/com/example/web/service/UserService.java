package com.example.web.service;


import com.example.web.config.RedisUtil;
import com.example.web.mapper.UserMapper;
import com.example.web.modules.Actions;
import com.example.web.modules.Msg;
import com.example.web.modules.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service("UserService")
public class UserService {

    //注入mapper
    @Autowired
    private UserMapper userMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public User find_by_user_name(String user_name){
        return userMapper.check_login(user_name);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Msg login(HttpServletRequest request, User user){
        User user1 = userMapper.check_login(user.getUser_name());
        if (user1 == null){
            return Msg.fail("用户名不存在");
        } else if (!user1.getPwd().equals(user.getPwd())) {
            return Msg.fail("密码错误!");
        } else {
            String token = request.getHeader("token");
            if (token!=null && !token.equals("")){
                String user_name = stringRedisTemplate.opsForValue().get(token);
                if ( user_name != null ) {
                    stringRedisTemplate.delete(token);
                    stringRedisTemplate.delete(user_name);
                }
            }
            //向redis中存入sessionID
            HttpSession session = request.getSession();
            stringRedisTemplate.opsForValue().set(user1.getUser_name(), session.getId());


            //存放token
            String token2 = UUID.randomUUID().toString().replaceAll("-","");
            stringRedisTemplate.opsForValue().set(token2,user1.getUser_name(),3600, TimeUnit.SECONDS);
            return Msg.success("登录成功").add("user",token2).add("type", user1.getType());
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Msg logout(HttpServletRequest request){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        if ( user_name != null ) {
            stringRedisTemplate.delete(token);
            stringRedisTemplate.delete(user_name);
        }
        return Msg.success("成功退出登录");
    }

    public List<String> select_user_role(String user_name){
        return userMapper.select_user_role(user_name);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public User signBus(User user){
        System.out.println(userMapper.check_login(user.getUser_name()));
        if ( userMapper.check_login(user.getUser_name()) == null ){
            userMapper.sign(user);
            User user1 = userMapper.check_login(user.getUser_name());
            if (user1 != null){
                userMapper.set_sign_user_role(user1.getId(), 1);
            }
            return user1;
        }
        else {
            return null;
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public User signCus(User user){
        System.out.println(userMapper.check_login(user.getUser_name()));
        if ( userMapper.check_login(user.getUser_name()) == null ){
            userMapper.sign(user);
            User user1 = userMapper.check_login(user.getUser_name());
            if (user1 != null){
                userMapper.set_sign_user_role(user1.getId(), 2);
            }
            return user1;
        }
        else {
            return null;
        }
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public User change_pwd(String user_name, String pwd1, String pwd2){
        User user = userMapper.check_login(user_name);
        if (user.getPwd().equals(pwd1)){
            userMapper.change_pwd(user_name, pwd2);
            return userMapper.check_login(user_name);
        }
        else
            return null;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int selectUrlsByUser(String user_name, String url_name){
        if ( userMapper.selectUrlsByUser(user_name, url_name) == null )
            return 0;
        else
            return 1;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Actions> selectActionsByUser(String user_name){
        return userMapper.selectActionsByUser(user_name);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int check_user_type(String user_name){
        return userMapper.check_user_type(user_name);
    }
}
