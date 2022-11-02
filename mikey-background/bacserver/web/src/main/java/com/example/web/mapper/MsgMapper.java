package com.example.web.mapper;

import org.springframework.stereotype.Component;

@Component(value = "MsgMapper")
public interface MsgMapper {
    void addMsg(int from_user_id, int to_user_id, String message, String date_str);
}
