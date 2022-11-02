package com.example.web.mapper;

import com.example.web.modules.Type;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "TypeMapper")
public interface TypeMapper {
    List<Type> selectAllType();
}
