package com.example.web.config;

import com.example.web.modules.Msg;
import com.example.web.service.MsgService;
import com.example.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class WebSocketUtil {
    @Autowired
    private UserService userService;
    @Autowired
    private MsgService msgService;

    public static UserService use_userService;
    public static MsgService use_msgService;
    @PostConstruct
    public void set_redisTemplate(){
        use_userService = this.userService;
    }
    @PostConstruct
    public void set_stringRedisTemplate(){
        use_msgService = this.msgService;
    }
}
