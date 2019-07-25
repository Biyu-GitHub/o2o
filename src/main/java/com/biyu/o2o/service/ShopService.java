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

    /**
     * 查询指定店铺信息
     *
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);

    /**
     * 更新店铺信息（从店家角度）
     *
     * @param shop
     * @param shopImgInputStream
     * @param filename
     * @return
     * @throws RuntimeException
     */
    ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String filename) throws RuntimeException;
}
