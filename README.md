## 开发环境

- JDK 8
- Maven 3.3
- MySQL 5.5
- Google Chrome
- Tomcat 8
- IDEA
- Postman
- Json Viewer

## SSM重点知识

* SpringMVC：DispatcherServlet
* Spring：IOC（DI）和AOP（JDK，cglib）
* MyBatis：ORM

## 资源网站

* [MVNRepository](https://mvnrepository.com/)

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

## 3. LogBack配置与使用

* 主要模块：
  * logback-access
  * logback-classic
  * logback-core
* 主要标签：
  * logger
  * appender
  * layout

### 3.1 配置

* **main/resources/logback.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<configuration scan="true" scanPeriod="60 seconds" debug="false">
    <!-- 定义参数常量 -->
    <!--
    logback日志级别
    TRACE < DEBUG < INFO < WARN < ERROR
    一般将DEBUG，INFO，ERROR三个级别的日志存储在3个文件当中
    -->
    <property name="log.level" value="debug"/>
    <!--最多保存30个-->
    <property name="log.maxHistory" value="30"/>
    <!--日志存储路径-->
    <property name="log.filePath" value="${catalina.base}/logs/webapps"/>
    <!--日志展示格式-->
    <property name="log.pattern"
              value="%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n"/>

    <!--定义日志的输出媒介，控制台设置-->
    <appender name="consoleAppender" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>${log.pattern}</pattern>
        </encoder>
    </appender>

    <!--DEBUG-->
    <appender name="debugAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--文件路径-->
        <file>${log.filePath}/debug.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--文件名称-->
            <fileNamePattern>${log.filePath}/debug/debug.%d{yyyy-MM-dd}.log.gz</fileNamePattern>
            <!--文件最大保存历史数量-->
            <maxHistory>${log.maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>${log.pattern}</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>DEBUG</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!--INFO-->
    <appender name="infoAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--文件路径-->
        <file>${log.filePath}/info.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--文件名称-->
            <fileNamePattern>${log.filePath}/info/info.%d{yyyy-MM-dd}.log.gz</fileNamePattern>
            <!--文件最大保存历史数量-->
            <maxHistory>${log.maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>${log.pattern}</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!--ERROR-->
    <appender name="errorAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!--文件路径-->
        <file>${log.filePath}/error.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--文件名称-->
            <fileNamePattern>${log.filePath}/error/error.%d{yyyy-MM-dd}.log.gz</fileNamePattern>
            <!--文件最大保存历史数量-->
            <maxHistory>${log.maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>${log.pattern}</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <logger name="com.biyu.o2o" level="${log.level}" additivity="true">
        <appender-ref ref="debugAppender"/>
        <appender-ref ref="infoAppender"/>
        <appender-ref ref="errorAppender"/>
    </logger>

    <root level="info">
        <appender-ref ref="consoleAppender"/>
    </root>
</configuration>
```

### 3.2 使用

* 启动tomcat会显示**CATALINA_BASE**信息：

```bash
"C:\Users\BiYu\.IntelliJIdea2019.1\system\tomcat\index_jsp_o2o"
```

* 会在CATALINA_BASE/logs/webapps/下生成日志文件
* 第二天会将第一天的日志打包成压缩文件

## 4. 店铺注册

* 数据库链接
* Mybatis数据库映射关系配置
* dao -> service -> conyroller层代码的编写，Junit的使用
* Session，图片处理工具Thumbnailator的使用
* suimobile前端设计与开发

### 4.1 插入

```xml
<insert id="insertShop" useGeneratedKeys="true" keyColumn="shop_id" keyProperty="shopId">
    INSERT INTO tb_shop (owner_id, area_id, shop_category_id, shop_name, shop_desc, shop_addr, phone, shop_img,
    priority, create_time, last_edit_time, enable_status, advice)
    VALUES (#{owner.userId}, #{area.areaId}, #{shopCategory.shopCategoryId}, #{shopName}, #{shopDesc}, #{shopAddr},
    #{phone}, #{shopImg}, #{priority}, #{createTime}, #{lastEditTime}, #{enableStatus}, #{advice});
</insert>
```

### 4.2 更新

```xml
<update id="updateShop" parameterType="com.biyu.o2o.entity.Shop">
    update tb_shop
    <set>
        <if test="shopName != null">shop_name=#{shopName},</if>
        <if test="shopDesc != null">shop_desc=#{shopDesc},</if>
        <if test="shopAddr != null">shop_addr=#{shopAddr},</if>
        <if test="phone != null">phone=#{phone},</if>
        <if test="shopImg != null">shop_img=#{shopImg},</if>
        <if test="priority != null">priority=#{priority},</if>
        <if test="lastEditTime != null">last_edit_time=#{lastEditTime},</if>
        <if test="enableStatus != null">enable_status=#{enableStatus},</if>
        <if test="advice != null">advice=#{advice},</if>
        <if test="area != null">area_id=#{area.areaId},</if>
        <if test="shopCategory != null">shop_category_id=#{shopCategory.shopCategoryId}</if>
    </set>
    where shop_id=#{shopId}
</update>
```



