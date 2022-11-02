package com.example.web.controller;

import com.example.web.modules.Msg;
import com.example.web.modules.User;
import com.example.web.service.UserService;
import com.example.web.service.WantService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

//这是基线需求所需的controller
@RestController
@RequestMapping(value = "/want")
public class WantController {

    @Autowired
    private WantService wantService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserService userService;
    //基线需求的购买商品
    @PostMapping("/basicCreateWant")
    public Msg basicCreateWant(@RequestBody Map map){
        return wantService.createWant(map.get("true_name").toString(), map.get("phone_number").toString(),
                Integer.parseInt(map.get("id").toString()));
    }

    //选择订单
    @RequiresPermissions(value = "完成订单")
    @PostMapping("/basicChooseWant")
    public Msg chooseWant(@RequestBody Map map){
        return wantService.chooseWant(Integer.parseInt(map.get("id").toString()),
                Integer.parseInt(map.get("good_id").toString()));
    }

    //订单成功
    @RequiresPermissions(value = "完成订单")
    @PostMapping("/basicCompleteWant")
    public Msg completeWant(@RequestBody Map map){
        return wantService.completeWant(Integer.parseInt(map.get("id").toString()),
                Integer.parseInt(map.get("good_id").toString()));
    }

    //订单失败
    @RequiresPermissions(value = "取消订单")
    @PostMapping("/basicFailWant")
    public Msg failWant(@RequestBody Map map){
        return wantService.failWant(Integer.parseInt(map.get("id").toString()),
                Integer.parseInt(map.get("good_id").toString()));
    }


    //以下是查看意向和订单
    @RequiresPermissions(value = "查询订单")
    @PostMapping("/lookWantsNow")
    public Msg lookWantsNow(HttpServletRequest request, @RequestBody Map map){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        return Msg.success("成功").add("wants", wantService.myWantsNow(user_name,
                Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString())));
    }
    @RequiresPermissions(value = "查询订单")
    @PostMapping("/lookWantsDone")
    public Msg lookWantsDone(HttpServletRequest request, @RequestBody Map map){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        return Msg.success("成功").add("wants", wantService.myWantsDone(user_name,
                Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString())));
    }
    @RequiresPermissions(value = "查询订单")
    @PostMapping("/lookOrNow")
    public Msg lookOrNow(HttpServletRequest request, @RequestBody Map map){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        return Msg.success("成功").add("wants", wantService.myOrNow(user_name,
                Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString())));
    }
    @RequiresPermissions(value = "查询订单")
    @PostMapping("/lookOrDone")
    public Msg lookOrDone(HttpServletRequest request, @RequestBody Map map){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        return Msg.success("成功").add("wants", wantService.myOrDone(user_name,
                Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString())));
    }


    //获取对应的数量
    @RequiresPermissions(value = "查询订单")
    @PostMapping("/lookWantsNowTotal")
    public Msg lookWantsNowTotal(HttpServletRequest request){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        return Msg.success("成功").add("total", wantService.myWantsNowTotal(user_name));
    }
    @RequiresPermissions(value = "查询订单")
    @PostMapping("/lookWantsDoneTotal")
    public Msg lookWantsDoneTotal(HttpServletRequest request){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        return Msg.success("成功").add("total", wantService.myWantsDoneTotal(user_name));
    }
    @RequiresPermissions(value = "查询订单")
    @PostMapping("/lookOrNowTotal")
    public Msg lookOrNowTotal(HttpServletRequest request){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        return Msg.success("成功").add("total", wantService.myOrNowTotal(user_name));
    }
    @RequiresPermissions(value = "查询订单")
    @PostMapping("/lookOrDoneTotal")
    public Msg lookOrDoneTotal(HttpServletRequest request){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        return Msg.success("成功").add("total", wantService.myOrDoneTotal(user_name));
    }
}
