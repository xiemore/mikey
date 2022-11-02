package com.example.web.service;

import com.example.web.mapper.OrderMapper;
import com.example.web.mapper.UserMapper;
import com.example.web.modules.Msg;
import com.example.web.modules.Orders;
import com.example.web.modules.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("OrderService")
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Msg createOneOrder(String user_name, int good_id){
        User user = userMapper.check_login(user_name);
        orderMapper.createOneOrder(good_id, user.getId());
        int result = orderMapper.lock(good_id);
        if ( result == 1)
            return Msg.success("操作成功");
        else
            return Msg.fail("操作失败");
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Orders> getOrderCreatedBus(String user_name, Integer page, Integer size){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        return orderMapper.getOrderCreatedBus(user_name, page, size);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Orders> getOrderCreatedCus(String user_name, Integer page, Integer size){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        return orderMapper.getOrderCreatedCus(user_name, page, size);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Orders> getOrderDoneBus(String user_name, Integer page, Integer size){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        return orderMapper.getOrderDoneBus(user_name, page, size);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<Orders> getOrderDoneCus(String user_name, Integer page, Integer size){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        return orderMapper.getOrderDoneCus(user_name, page, size);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long getTotalCreatedBus(String user_name){
        return orderMapper.getTotalCreatedBus(user_name);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long getTotalCreatedCus(String user_name){
        return orderMapper.getTotalCreatedCus(user_name);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long getTotalDoneBus(String user_name){
        return orderMapper.getTotalDoneBus(user_name);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long getTotalDoneCus(String user_name){
        return orderMapper.getTotalDoneCus(user_name);
    }
    @Transactional(isolation = Isolation.DEFAULT)
    public Msg cancelOrder(int order_id, int good_id){
        orderMapper.cancelOrderFirst(order_id);
        int result = orderMapper.cancelOrderSecond(good_id);
        if (result == 1)
            return Msg.success("操作成功");
        else
            return Msg.fail("操作失败");
    }

    @Transactional(isolation = Isolation.DEFAULT)
    public Msg completeOrder(int order_id, int good_id){
        orderMapper.completeOrderFirst(order_id);
        int result = orderMapper.completeOrderSecond(good_id);
        if (result == 1)
            return Msg.success("操作成功");
        else
            return Msg.fail("操作失败");
    }
}
