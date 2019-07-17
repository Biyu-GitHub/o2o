package com.biyu.o2o.service;

import com.biyu.o2o.dto.ShopExecution;
import com.biyu.o2o.entity.Shop;

import java.io.File;

public interface ShopService {
    /**
     * 添加店铺
     * @param shop shop对象
     * @param shopImg 店铺图片
     * @return
     */
    ShopExecution addShop(Shop shop, File shopImg);
}
