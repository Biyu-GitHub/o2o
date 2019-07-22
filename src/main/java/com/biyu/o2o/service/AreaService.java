package com.biyu.o2o.service;

import com.biyu.o2o.entity.Area;

import java.util.List;

public interface AreaService {
    /**
     * 列出区域列表
     *
     * @return
     */
    List<Area> getAreaList();
}