package com.biyu.o2o.service.impl;

import com.biyu.o2o.dao.ShopDao;
import com.biyu.o2o.dto.ShopExecution;
import com.biyu.o2o.entity.Shop;
import com.biyu.o2o.enums.ShopStateEnum;
import com.biyu.o2o.exceptions.ShopOperatorException;
import com.biyu.o2o.service.ShopService;
import com.biyu.o2o.util.ImageUtil;
import com.biyu.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Date;

@Service
public class ShopServiceImpl implements ShopService {

    @Autowired
    private ShopDao shopDao;

    /**
     * 添加店铺信息：
     * 1. 向数据库插入店铺
     * 2. 添加图片
     * 3. 更新shop中的店铺图片地址
     *
     * @param shop    shop对象
     * @param shopImg 店铺图片
     * @return
     */
    @Override
    @Transactional // 事务支持
    public ShopExecution addShop(Shop shop, File shopImg) {

        if (shop == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOP_INFO);
        }

        try {
            // 初始化店铺信息
            shop.setEnableStatus(0); // 设置为未审核状态
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());

            // 添加店铺
            int effectedNum = shopDao.insertShop(shop);

            // 插入失败，影响行数为0
            if (effectedNum <= 0) {
                throw new ShopOperatorException("店铺创建失败");
            } else {
                // 如果传入了图片，则向店铺插入图片
                if (shopImg != null) {
                    try {
                        addShopImg(shop, shopImg);
                    } catch (Exception e) {
                        throw new ShopOperatorException("addShopImgError: " + e.getMessage());
                    }

                    effectedNum = shopDao.updateShop(shop);
                    if (effectedNum <= 0) {
                        throw new ShopOperatorException("跟新图片地址失败");
                    }
                }
            }
        } catch (Exception e) {
            throw new ShopOperatorException("addShopError: " + e.getMessage());
        }

        return new ShopExecution(ShopStateEnum.CHECK, shop);
    }

    /**
     * 添加店铺图片：
     * 1. 获取shop图片存储到本地服务器的相对路径
     * 2. 将该图片处理成缩略图后保存在该路径，并返回路径地址
     * 3. 更新shopImgAddr
     *
     * @param shop
     * @param shopImg
     */
    private void addShopImg(Shop shop, File shopImg) {
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(shopImg, dest);
        shop.setShopImg(shopImgAddr);
    }
}
