package com.example.web.service;

import com.example.web.mapper.GoodMapper;
import com.example.web.mapper.OrderMapper;
import com.example.web.modules.Msg;
import com.example.web.modules.Want;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("WantService")
public class WantService {
    //以下是基线需求购买商品所用代码

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private GoodMapper goodMapper;

    //选择意向
    @Transactional(isolation = Isolation.DEFAULT)
    public Msg chooseWant(int id, int good_id){
        orderMapper.disableWants(good_id);
        int result = orderMapper.chooseWants(id);
        if (result == 1)
            return Msg.success("操作成功");
        else
            return Msg.fail("操作失败");
    }

    //购买商品(意向)
    @Transactional(isolation = Isolation.DEFAULT)
    public Msg createWant(String true_name, String phone_number, int id){
        orderMapper.createWant(true_name, phone_number, id);
        int result = orderMapper.createWantSecond(id);
        if (result == 1)
            return Msg.success("操作成功");
        else
            return Msg.fail("操作失败");
    }

    //完成订单
    @Transactional(isolation = Isolation.DEFAULT)
    public Msg completeWant(int id, int good_id){
        int result = orderMapper.completeWant(id);
        if (result == 1) {
            orderMapper.completeOrderSecond(good_id);
            goodMapper.deleteGoodById(good_id);
            return Msg.success("操作成功");
        } else
            return Msg.fail("操作失败");
    }

    //订单完成失败
    @Transactional(isolation = Isolation.DEFAULT)
    public Msg failWant(int id, int good_id){
        int result = orderMapper.failWant(id);
        if (result == 1) {
            orderMapper.cancelOrderSecond(good_id);
            return Msg.success("操作成功");
        } else
            return Msg.fail("操作失败");
    }

    //查看当前意向
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Want> myWantsNow(String user_name, Integer page, Integer size){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        return orderMapper.selectWantsNow(user_name, page, size);
    }

    //查看历史意向
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Want> myWantsDone(String user_name, Integer page, Integer size){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        return orderMapper.selectWantsDone(user_name, page, size);
    }

    //查看当前订单
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Want> myOrNow(String user_name, Integer page, Integer size){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        return orderMapper.selectOrNow(user_name, page, size);
    }

    //查看历史订单
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Want> myOrDone(String user_name, Integer page, Integer size){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        return orderMapper.selectOrDone(user_name, page, size);
    }



    //获取对应的数量
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long myWantsNowTotal(String user_name){
        return orderMapper.selectWantsNowTotal(user_name);
    }

    //查看历史意向
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long myWantsDoneTotal(String user_name){

        return orderMapper.selectWantsDoneTotal(user_name);
    }

    //查看当前订单
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long myOrNowTotal(String user_name){
        return orderMapper.selectOrNowTotal(user_name);
    }

    //查看历史订单
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long myOrDoneTotal(String user_name){
        return orderMapper.selectOrDoneTotal(user_name);
    }
}
