package com.biyu.o2o.dao;

import com.biyu.o2o.entity.Shop;

public interface ShopDao {
    /**
     * 新增店铺
     *
     * @param shop
     * @return effectedNum
     */
    int insertShop(Shop shop);

    /**
     * 更新店铺信息
     *
     * @param shop
     * @return effectedNum
     */
    int updateShop(Shop shop);

    /**
     * 通过owner id 查询店铺
     *
     * @param shopId
     * @return shop
     */
    Shop queryByShopId(long shopId);
}
