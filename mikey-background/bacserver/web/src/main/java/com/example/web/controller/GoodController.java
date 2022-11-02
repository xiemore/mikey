package com.example.web.controller;

import com.example.web.modules.Msg;
import com.example.web.modules.User;
import com.example.web.service.GoodService;
import com.example.web.modules.Good;
import com.example.web.service.UserService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/good")
public class GoodController extends BaseController{

    @Autowired
    private GoodService goodService;
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/selectAllGoods")
    public Msg selectAllGoods(@RequestBody Map map){
        return Msg.success("查询成功").add("goods", goodService.selectAllGoods(map.get("type").toString(),
                map.get("good_name").toString(), Integer.parseInt(map.get("size").toString()),
                Integer.parseInt(map.get("page").toString())));
    }


    @RequiresPermissions(value = "查看自己的商品")
    @PostMapping("/selectAllGoodsByUser")
    public Msg selectAllGoodsByUser(HttpServletRequest request, @RequestBody Map map){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        User user = userService.find_by_user_name(user_name);
        return Msg.success("查询成功").add("goods", goodService.selectAllGoodsByUser(user.getId(),
                Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString()),
                map.get("type").toString()));
    }


    @RequiresPermissions(value = "查看自己的商品")
    @PostMapping("/selectTotalByUser")
    public Msg selectTotalByUser(HttpServletRequest request, @RequestBody Map map){
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        User user = userService.find_by_user_name(user_name);
        return Msg.success("查询成功").add("total", goodService.selectTotalByUser(user.getId(), map.get("type").toString()));
    }

    @PostMapping("/selectTotal")
    public Msg selectTotal(@RequestBody Map map) {
        return Msg.success("查询成功").add("total", goodService.selectTotal(map.get("type").toString(),
                map.get("good_name").toString()));
    }

    @PostMapping("/findGoodById")
    public Msg findGoodById(@RequestBody int good_id){
        return Msg.success("查询成功").add("good", goodService.findGoodById(good_id));
    }


    @RequiresPermissions("冻结商品")
    @PostMapping("/lock_good")
    public Msg lockGood(@RequestBody int good_id){
        return goodService.lockGood(good_id);
    }


    @RequiresPermissions("解冻商品")
    @PostMapping("/unlock_good")
    public Msg unlockGood(@RequestBody int good_id){
        return goodService.unlockGood(good_id);
    }


    @RequiresPermissions("下架商品")
    @PostMapping("/delete_good_by_id")
    public Msg deleteGoodById(@RequestBody int good_id){
        return goodService.deleteGoodById(good_id);
    }


    @RequiresPermissions("修改商品")
    @PostMapping("/change_good")
    public Msg changeGood(@RequestBody Good good){
        return goodService.resetGoodById(good.getId(), good.getGood_num(), good.getGood_price());
    }


    @RequiresPermissions("上架商品")
    @PostMapping("/addGood")
    public Msg addGood(HttpServletRequest request, @RequestParam("file") MultipartFile[] files,
                          @RequestParam("good_name") String good_name, @RequestParam("good_type") int good_type,
                          @RequestParam("good_price") float good_price, @RequestParam("good_num") int good_num,
                          @RequestParam("good_log") String good_log) throws IOException {
        String token = request.getHeader("token");
        String user_name = stringRedisTemplate.opsForValue().get(token);
        User user = userService.find_by_user_name(user_name);
        return goodService.addGood(files, good_name, good_type, good_price, good_num, good_log, user.getId());
    }

}
