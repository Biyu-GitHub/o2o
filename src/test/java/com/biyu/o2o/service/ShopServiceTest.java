package com.biyu.o2o.service;

import com.biyu.o2o.BaseTest;
import com.biyu.o2o.dto.ShopExecution;
import com.biyu.o2o.entity.Area;
import com.biyu.o2o.entity.PersonInfo;
import com.biyu.o2o.entity.Shop;
import com.biyu.o2o.entity.ShopCategory;
import com.biyu.o2o.enums.ShopStateEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Date;

public class ShopServiceTest extends BaseTest {

    @Autowired
    private ShopService shopService;

    @Test
    public void addShop() {
        Shop shop = new Shop();

        PersonInfo owner = new PersonInfo();
        Area area = new Area();
        ShopCategory shopCategory = new ShopCategory();

        owner.setUserId(1L);
        area.setAreaId(2L);
        shopCategory.setShopCategoryId(1L);

        shop.setOwner(owner);
        shop.setArea(area);
        shop.setShopCategory(shopCategory);

        shop.setShopName("测试店铺1");
        shop.setShopDesc("test1");
        shop.setShopAddr("test1");
        shop.setPhone("test1");
        shop.setShopImg("test1");
        shop.setCreateTime(new Date());
        shop.setEnableStatus(ShopStateEnum.CHECK.getState());
        shop.setAdvice("审核中");

        File shopImg = new File("C:\\Users\\BiYu\\Pictures\\frame0000.jpg");
        ShopExecution se = shopService.addShop(shop, shopImg);

        assert ShopStateEnum.CHECK.getState() == se.getState();
    }
}