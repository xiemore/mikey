package com.example.web.controller;

import com.example.web.modules.Msg;
import com.example.web.service.OrderService;
import com.example.web.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping(value = "/order")
public class OrderController extends BaseController{

    @Autowired
    OrderService orderService;
    @Autowired
    UserService userService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @RequiresPermissions(value = "购买商品")
    @PostMapping("/createOneOrder")
    public Msg createOneOrder(HttpServletRequest request, @RequestBody Map map){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        return orderService.createOneOrder(user_name, Integer.parseInt(map.get("good_id").toString()));
    }


    @RequiresPermissions(value = "查询订单")
    @PostMapping("/selectOrdersCreated")
    public Msg getOrdersCreated(HttpServletRequest request, @RequestBody Map map){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        if ( userService.check_user_type(user_name) == 1 ){
            return Msg.success("查询成功").add("orders", orderService.getOrderCreatedBus(user_name,
                    Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString())));
        } else {
            return Msg.success("查询成功").add("orders", orderService.getOrderCreatedCus(user_name,
                    Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString())));
        }
    }

    @RequiresPermissions(value = "查询订单")
    @PostMapping("/selectOrdersDone")
    public Msg getOrdersDone(HttpServletRequest request, @RequestBody Map map){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        if ( userService.check_user_type(user_name) == 1 ){
            return Msg.success("查询成功").add("orders", orderService.getOrderDoneBus(user_name,
                    Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString())));
        } else {
            return Msg.success("查询成功").add("orders", orderService.getOrderDoneCus(user_name,
                    Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString())));
        }
    }


    @RequiresPermissions(value = "查询订单")
    @PostMapping("/selectOrdersDoneTotal")
    public Msg getOrdersDoneTotal(HttpServletRequest request){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        if ( userService.check_user_type(user_name) == 1 ){
            return Msg.success("查询成功").add("total", orderService.getTotalDoneBus(user_name));

        } else {
            return Msg.success("查询成功").add("total", orderService.getTotalDoneCus(user_name));

        }
    }


    @RequiresPermissions(value = "查询订单")
    @PostMapping("/selectOrdersCreatedTotal")
    public Msg getOrdersCreatedTotal(HttpServletRequest request){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        if ( userService.check_user_type(user_name) == 1 ){
            return Msg.success("查询成功").add("total", orderService.getTotalCreatedBus(user_name));
        } else {
            return Msg.success("查询成功").add("total", orderService.getTotalCreatedCus(user_name));
        }
    }


    @RequiresPermissions(value = "取消订单")
    @PostMapping("/cancelOrder")
    public Msg cancelOrder(@RequestBody Map map){
        return orderService.cancelOrder(Integer.parseInt(map.get("order_id").toString()),
                Integer.parseInt(map.get("good_id").toString()));
    }

    @RequiresPermissions(value = "完成订单")
    @PostMapping("/completeOrder")
    public Msg completeOrder(@RequestBody Map map){
        return orderService.completeOrder(Integer.parseInt(map.get("order_id").toString()),
                Integer.parseInt(map.get("good_id").toString()));
    }
}
