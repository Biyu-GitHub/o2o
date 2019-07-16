## 开发环境

- JDK 8
- Maven 3.3
- MySQL 5.5
- Google Chrome
- Tomcat 8
- IDEA
- Postman
- Json Viewer

## 1. 项目初始化

新建MavenWebApp项目，并在web.xml中修改servlet版本为3.1

```xml
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee
                      http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1"
         metadata-complete="true">
</web-app>
```

## 2. 项目设计和框架搭建

### 2.1 系统功能模块划分

* 前端展示系统
* 店家系统
* 超级管理员系统

### 2.2 实体类设计与数据库表创建

* **Area** -- tb_area

```java
private Long areaId;
private String areaName;
private Integer priority; // 权重，用来排序
private Date createTime;
private Date lastEditTime;
```

* **PersonInfo** -- tb_person_info

```java
private Long userId;
private String name;
private String profileImg;
private String email;
private String gender;
private Integer enableStatus; // 用户状态, 例如被禁用无法登录
private Integer userType; // 1-顾客 2-店家 3-超级管理员
private Date createTime;
private Date lastEditTime;
```

* **WechatAuth** -- tb_wechat_auth

```java
private Long wechatAuthId;
private Long userId;
private String openId;
private Date createTime;

private PersonInfo personInfo;
```

* **LocalAuth** -- tb_local_auth

```java
private Long localAuthId;
private String userName;
private String password;
private Date createTime;
private Date lastEditTime;

private PersonInfo personInfo;
```

* **HeadLine** -- tb_head_line

```java
private Long lineId;
private String lineName;
private String lineLink;
private String lineImg;
private Integer priority;
private Integer enableStatus;
private Date createTime;
private Date lastEditTime;
```

* **Shop** -- tb_shop

```java
private Long shopId;
private String shopName;
private String shopDesc;
private String shopAddr;
private String phone;
private String shopImg;
private Integer priority;
private Date createTime;
private Date lastEditTime;
private Integer enableStatus; // -1-不可用 0-审核中 1-可用
private String advice;

private Area area;
private PersonInfo owner;
private ShopCategory shopCategory;
```

* **ShopCategory** -- tb_shop_category

```java
private Long shopCategoryId;
private String shopCategoryName;
private String shopCategoryDesc;
private String shopCategoryImg;
private Integer priority;
private Date createTime;
private Date lastEditTime;

private ShopCategory parent;
```

* **Product** -- tb_product

```java
private Long productId;
private String productName;
private String productDesc;
private String imgAddr;// 简略图
private String normalPrice;
private String promotionPrice;
private Integer priority;
private Date createTime;
private Date lastEditTime;
private Integer enableStatus; // 0-下架 1-可在前端展示

private List<ProductImg> productImgList;
private ProductCategory productCategory;
private Shop shop;
```

* **ProductCategory** -- tb_product_category

```java
private Long productCategoryId;
private Long shopId;
private String productCategoryName;
private Integer priority;
private Date createTime;
```

* **ProductImg** -- tb_product_img

```java
private Long productImgId;
private String imgAddr;
private String imgDesc;
private Integer priority;
private Date createTime;
private Long productId;
```

### 2.3 单元测试

* 编写接口
* 编写实现类
* 编写测试类

```java
// 自动注入依赖
@AutoWired

// 表明这是一个Service
@Service

// 单元测试的时候使用，配置spring和junit整合，junit在启动时加载springIOC容器
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
    "classpath:spring/spring-dao.xml",
    "classpath:spring/spring-service.xml"})
```

#### 2.3.1 Dao层单元测试

* 接口

```java
public interface AreaDao {
    /**
     * 列出区域列表
     * @return areaList
     */
    List<Area> queryArea();
}
```

* 使用Mybatis不需要实现类，直接编写配置文件 **resources/mapper/AreaDao.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<!-- namespace指定接口 -->
<mapper namespace="com.biyu.o2o.dao.AreaDao">
    
    <!-- id指定方法  resultType指定返回值类型-->
    <select id="queryArea" resultType="com.biyu.o2o.entity.Area">
        SELECT
            area_id,
            area_name,
            priority,
            create_time,
            last_edit_time
        FROM
            tb_area
        ORDER BY
            priority DESC;
    </select>
</mapper>
```

#### 2.3.2 Srevice层单元测试

* 接口

```java
public interface AreaService {
    List<Area> getAreaList();
}
```

* 实现类 -- 直接调用Dao

```java
@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;

    @Override
    public List<Area> getAreaList() {
        return areaDao.queryArea();
    }
}
```

* 测试类

```java
public class AreaServiceTest extends BaseTest {

    @Autowired
    private AreaService areaService;

    @Test
    public void getAreaList() {
        List<Area> areaList = areaService.getAreaList();
        assert "东苑".equals(areaList.get(0).getAreaName());
    }
}
```

#### 2.3.3 Web层验证

* 实现类 -- 直接调用Service

```java
@Controller
@RequestMapping("/superadmin") // servlet映射
public class AreaController {

    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/listarea", method = RequestMethod.GET)
    @ResponseBody // 将返回值自动转为json对象
    private Map<String, Object> listArea() {
        
        Map<String, Object> modelMap = new HashMap<>();
        List<Area> list = new ArrayList<>();

        try {
            list = areaService.getAreaList();
            modelMap.put("rows", list);
            modelMap.put("total", list.size());

        } catch (Exception e) {
            e.printStackTrace();
            modelMap.put("success", false);
            modelMap.put("reeMsg", e.toString());
        }
        return modelMap;
    }
}
```

> map的key使用**rows**与**total**是为了搭配后面要用到的前端框架
>
> @RequestMapping：设置路由
>
>  @ResponseBody：将返回值自动转为json对象

* 测试 -- 直接启动浏览器，并输入以下地址

```http
http://localhost:8080/o2o/superadmin/listarea
```

* 显示结果

```json
{
    "total": 1,
    "rows": [
        {
            "areaId": 1,
            "areaName": "东苑",
            "priority": 12,
            "createTime": 1496603578000,
            "lastEditTime": 1496603578000
        }
    ]
}
```

