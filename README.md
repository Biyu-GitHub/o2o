## 开发环境

- JDK 8
- Maven 3.3
- MySQL 5.5
- Google Chrome
- Tomcat 8
- IDEA

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

