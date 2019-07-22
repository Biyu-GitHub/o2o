package com.biyu.o2o.web.shopadmin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "shopadmin", method = {RequestMethod.GET})
public class ShopAdminController {

    /**
     * 因为在spring-web中配置了如下信息：
     * <property name="prefix" value="/WEB-INF/html/"></property>
     * <property name="suffix" value=".html"></property>
     * 所以可以给返回值自动添加前缀和后缀
     *
     * @return
     */
    @RequestMapping(value = "/shopoperation")
    public String shopOperation() {
        return "shop/shopoperation";
    }
}
