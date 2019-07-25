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
import java.io.InputStream;
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
     * @param shop               shop对象
     * @param shopImgInputStream 店铺图片
     * @return
     */
    @Override
    @Transactional // 事务支持
    public ShopExecution addShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperatorException {

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
                if (shopImgInputStream != null) {
                    try {
                        addShopImg(shop, shopImgInputStream, fileName);
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
     * @param shopImgInputStream
     */
    private void addShopImg(Shop shop, InputStream shopImgInputStream, String fileName) {
        String dest = PathUtil.getShopImagePath(shop.getShopId());
        String shopImgAddr = ImageUtil.generateThumbnail(shopImgInputStream, fileName, dest);
        shop.setShopImg(shopImgAddr);
    }

    /**
     * 查询指定店铺信息
     *
     * @param shopId
     * @return
     */
    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    /**
     * 更新店铺信息（从店家角度）
     * 1. 是否需要修改图片
     * 2. 更新店铺信息
     *
     * @param shop
     * @param shopImgInputStream
     * @param filename
     * @return
     * @throws RuntimeException
     */
    @Override
    public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String filename) throws RuntimeException {
        if (shop == null || shop.getShopId() == null) {
            return new ShopExecution(ShopStateEnum.NULL_SHOPID);
        } else {
            try {
                // 1. 是否需要修改图片
                if (shopImgInputStream != null && filename != null && !"".equals(filename)) {
                    Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                    if (tempShop.getShopImg() != null) {
                        ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                    }
                    addShopImg(shop, shopImgInputStream, filename);
                }

                // 2. 更新店铺信息
                shop.setLastEditTime(new Date());
                int effectedNum = shopDao.updateShop(shop);
                if (effectedNum <= 0) {
                    return new ShopExecution(ShopStateEnum.INNER_ERROR);
                } else {
                    shop = shopDao.queryByShopId(shop.getShopId());
                    return new ShopExecution(ShopStateEnum.SUCCESS, shop);
                }
            } catch (Exception e) {
                throw new RuntimeException("modifyShop error: " + e.getMessage());
            }
        }
    }
}
