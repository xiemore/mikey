package com.example.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RedisUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    public static RedisTemplate use_redisTemplate;
    public static StringRedisTemplate use_stringRedisTemplate;
    @PostConstruct
    public void set_redisTemplate(){
        use_redisTemplate = this.redisTemplate;
    }
    @PostConstruct
    public void set_stringRedisTemplate(){
        use_stringRedisTemplate = this.stringRedisTemplate;
    }
}
