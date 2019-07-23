package com.biyu.o2o.dao;

import com.biyu.o2o.BaseTest;
import com.biyu.o2o.entity.ShopCategory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class ShopCategoryDaoTest extends BaseTest {

    @Autowired
    private ShopCategoryDao shopCategoryDao;

    @Test
    public void queryShopCategory() {


        ShopCategory test = new ShopCategory();
        ShopCategory parent = new ShopCategory();

        parent.setShopCategoryId(1L);
        test.setParent(parent);

        List<ShopCategory> shopCategoryList = shopCategoryDao.queryShopCategory(test);

        for (ShopCategory shopCategory : shopCategoryList) {
            System.out.println(shopCategory);
        }
    }
}