package com.example.web.service;

import com.example.web.mapper.MsgMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service("MsgService")
public class MsgService {
    @Autowired
    private MsgMapper msgMapper;

    @Transactional(isolation = Isolation.DEFAULT)
    public void addMsg(int from_user_id, int to_user_id, String message, String date_str){
        msgMapper.addMsg(from_user_id, to_user_id, message, date_str);
    }
}
