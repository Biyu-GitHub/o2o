package com.biyu.o2o.dao;

import com.biyu.o2o.BaseTest;
import com.biyu.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AreaDaoTest extends BaseTest {

    @Autowired
    private AreaDao areaDao;

    @Test
    public void queryArea() {
        List<Area> areaList = areaDao.queryArea();
        assert 4 == areaList.size();
        for (Area area : areaList) {
            System.out.println(area);
        }
    }
}