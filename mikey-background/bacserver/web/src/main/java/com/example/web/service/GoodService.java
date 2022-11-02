package com.example.web.service;

import com.example.web.mapper.GoodMapper;
import com.example.web.mapper.OrderMapper;
import com.example.web.modules.Good;
import com.example.web.modules.GoodView;
import com.example.web.modules.Msg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("GoodService")
public class GoodService {

    //注入mapper
    @Autowired
    private GoodMapper goodMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Transactional(isolation = Isolation.DEFAULT)
    public Msg lockGood(int id){
        int result = orderMapper.lock(id);
        if (result == 1)
            return Msg.success("操作成功");
        else
            return Msg.fail("操作失败");
    }

    @Transactional(isolation = Isolation.DEFAULT)
    public Msg deleteGoodById(int id){
        List<GoodView> list_good_view = new ArrayList<>();
        Good good = goodMapper.findGoodById(id);
        give_good(list_good_view, good);
        File file;
        //删除对应的图片文件
        for (String pic: list_good_view.get(0).good_pics ){
            String pic_str = "D:/新建文件夹/软件工程实践/mikey/src/assets/" + pic + ".jpg";
            file = new File(pic_str);
            file.delete();
        }
        int result = goodMapper.deleteGoodById(id);
        if (result == 1){
            return Msg.success("操作成功");
        }
        else
            return Msg.fail("操作失败");
    }

    @Transactional(isolation = Isolation.DEFAULT)
    public Msg unlockGood(int id){
        int result = orderMapper.unlock(id);
        if (result == 1)
            return Msg.success("操作成功");
        else
            return Msg.fail("操作失败");
    }

    @Transactional(isolation = Isolation.DEFAULT)
    public Msg resetGoodById(int id, int num, float price){
        List<GoodView> list_good_view = new ArrayList<>();
        Good good = goodMapper.findGoodById(id);
        String date_str = good.getGood_price_log();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(System.currentTimeMillis());
        date_str = date_str + sdf.format(date)+"-"+price+";";
        int result = goodMapper.resetGoodById(id, num, price, date_str);
        if (result == 1){
            return Msg.success("操作成功");
        }
        else
            return Msg.fail("操作失败");
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<GoodView> selectAllGoods(String good_type, String good_name, Integer size, Integer page){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Good> list_good = goodMapper.selectAllGoods(good_type, good_name, size, page);
        List<GoodView> list_good_view = new ArrayList<>();
        for (Good good: list_good){
            give_good(list_good_view, good);
        }
        return list_good_view;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public List<GoodView> selectAllGoodsByUser(int id, Integer page, Integer size, String type){
        if (page != null && size != null) {
            page = (page - 1) * size;
        }
        List<Good> list_good = goodMapper.selectAllGoodsByUser(id, size, page, type);
        List<GoodView> list_good_view = new ArrayList<>();
        for (Good good: list_good){
            give_good(list_good_view, good);
        }
        return list_good_view;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long selectTotalByUser(int id, String type){
        return goodMapper.selectTotalByUser(id, type);
    }
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Long selectTotal(String good_type, String good_name){
        return goodMapper.selectTotal(good_type, good_name);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public GoodView findGoodById(int id){
        List<GoodView> list_good_view = new ArrayList<>();
        Good good = goodMapper.findGoodById(id);
        give_good(list_good_view, good);
        return list_good_view.get(0);
    }

    @Transactional(isolation = Isolation.DEFAULT)
    public Msg addGood(MultipartFile[] files, String good_name, int good_type, float good_price,
                        int good_num, String good_log, int users_id) throws IOException {

        String date_str;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date(System.currentTimeMillis());
        date_str = sdf.format(date)+"-"+good_price+";";
        Good good = new Good();
        good.setGood_name(good_name);
        good.setGood_type_id(good_type);
        good.setGood_price(good_price);
        good.setGood_num(good_num);
        good.setGood_log(good_log);
        good.setUser_name_id(users_id);
        good.setGood_price_log(date_str);
        good.setGood_pics(" ");

        goodMapper.addGoodFirst(good);
        int key = good.getId();

        FileOutputStream fos;
        InputStream is;
        //idea的相对路径很诡异，用绝对路径
        String base_path = "D:/新建文件夹/软件工程实践/mikey/src/assets/";
        String file_name;
        String url_str = "";
        File checkFile;
        int i = 0;
        if ( files == null )
            return Msg.fail("请上传图片");

        for ( MultipartFile file: files ){
            i = i + 1;
            file_name = key + "_" + i;
            String file_path = base_path + file_name + ".jpg";
            checkFile = new File(file_path);
            if(!checkFile.exists())
            {
                checkFile.createNewFile();
            }
            byte[] b = new byte[1024];
            fos = new FileOutputStream(file_path);
            is = file.getInputStream();
            while (is.read(b) != -1) {
                fos.write(b);
            }
            is.close();
            fos.close();
            url_str = url_str + file_name + ";";
        }
        int result = goodMapper.addGoodSecond(key, url_str);
        if (result == 1){
            return Msg.success("操作成功");
        }
        else
            return Msg.fail("操作失败");
    }

    //将Good转换为GoodView
    private void give_good(List<GoodView> list_good_view, Good good) {
        GoodView goodView;
        goodView = new GoodView();
        goodView.setId(good.getId());
        goodView.setGood_name(good.getGood_name());
        goodView.setGood_num(good.getGood_num());
        goodView.setGood_locker(good.getGood_locker());
        goodView.setGood_log(good.getGood_log());
        goodView.setGood_price(good.getGood_price());
        goodView.setUser_name(good.getUser_name());
        goodView.setGood_type(good.getGood_type());
        goodView.setGood_wants(good.getGood_wants());
        String[] pics = good.getGood_pics().split(";");
        goodView.good_pics = new ArrayList<>();
        goodView.good_pics.addAll(Arrays.asList(pics));
        String[] price_logs = good.getGood_price_log().split(";");
        Map<String, String> p_log;
        goodView.good_price_log = new ArrayList<>();
        for ( String price_log: price_logs){
            p_log = new HashMap<>();
            p_log.put("date", price_log.split("-")[0]);
            p_log.put("price", price_log.split("-")[1]);
            goodView.good_price_log.add(p_log);
        }
        list_good_view.add(goodView);
    }
}
