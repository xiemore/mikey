package com.example.web.mapper;

import com.example.web.modules.Good;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(value = "GoodMapper")
public interface GoodMapper {
    List<Good> selectAllGoods(String good_type, String good_name, Integer size, Integer page);
    List<Good> selectAllGoodsByUser(int id, Integer size, Integer page, String type);
    Long selectTotal(String good_type, String good_name);
    Long selectTotalByUser(int id, String type);
    Good findGoodById(int id);
    int deleteGoodById(int id);
    int resetGoodById(int id, int num, float price, String price_log);

    void addGoodFirst(Good good);

    int addGoodSecond(int id, String good_pics);
}
