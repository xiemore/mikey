package com.example.web.service;

import com.example.web.mapper.TypeMapper;
import com.example.web.modules.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("TypeService")
public class TypeService {

    @Autowired
    private TypeMapper typeMapper;

    public List<Type> selectAllType(){
        return typeMapper.selectAllType();
    }
}
