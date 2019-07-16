package com.biyu.o2o.service;

import com.biyu.o2o.BaseTest;
import com.biyu.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AreaServiceTest extends BaseTest {

    @Autowired
    private AreaService areaService;

    @Test
    public void getAreaList() {
        List<Area> areaList = areaService.getAreaList();
        assert "东苑".equals(areaList.get(0).getAreaName());
    }
}