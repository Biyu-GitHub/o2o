package com.biyu.o2o.service;

import com.biyu.o2o.dto.ShopExecution;
import com.biyu.o2o.entity.Shop;
import com.biyu.o2o.exceptions.ShopOperatorException;

import java.io.InputStream;

public interface ShopService {
    /**
     * 添加店铺
     *
     * @param shop               shop对象
     * @param shopImgInputStream 店铺图片输入流
     * @param filename           图片名称
     * @return
     */
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String filename) throws ShopOperatorException;
}
