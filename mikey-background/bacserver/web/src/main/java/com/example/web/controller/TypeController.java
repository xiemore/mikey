package com.example.web.controller;

import com.example.web.modules.Msg;
import com.example.web.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/type")
public class TypeController extends BaseController{

    @Autowired
    TypeService typeService;

    @PostMapping("/selectAllType")
    public Msg selectAllType(){
        return Msg.success("").add("types", typeService.selectAllType());
    }
}
