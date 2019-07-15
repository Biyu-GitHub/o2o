-- ----------------------------------------------------------------------------------------------------------------地区表
CREATE TABLE `tb_area`
(
    `area_id`        INT(2)       NOT NULL auto_increment,
    `area_name`      VARCHAR(200) NOT NULL,
    `priority`       INT(2)       NOT NULL DEFAULT 0,
    `create_time`    datetime              DEFAULT NULL,
    `last_edit_time` datetime              DEFAULT NULL,
    PRIMARY KEY (`area_id`),
    UNIQUE KEY `UK_AREA` (`area_name`)
) ENGINE = INNODB
  auto_increment = 1
  DEFAULT charset = utf8;


-- ------------------------------------------------------------------------------------------------------------用户信息表
create table `tb_person_info`
(
    `user_id`        int(10) not null auto_increment,
    `name`           varchar(32)      default null,
    `profile_img`    varchar(32)      default null,
    `email`          varchar(1024)    default null,
    `gender`         varchar(2)       default null,
    `enable_status`  int(2)  not null default '0' comment '0-禁止使用商城, 1-允许使用商城',
    `user_type`      int(2)  not null default '1' comment '1-顾客, 2-店家, 3-超级管理员',
    `create_time`    datetime         default null,
    `last_edit_time` datetime         default null,
    primary key (`user_id`)
) engine = InnoDB
  auto_increment = 1
  default charset = utf8;


-- ------------------------------------------------------------------------------------------------------------微信账号表
create table `tb_wechat_auth`
(
    `wechat_auth_id` int(10)       not null auto_increment,
    `user_id`        int(10)       not null,
    `open_id`        varchar(1024) not null,
    `create_time`    datetime default null,
    primary key (`wechat_auth_id`),
    constraint `fk_wechatauth_profile` foreign key (`user_id`) references `tb_person_info` (`user_id`)
) engine = InnoDB
  auto_increment = 1
  DEFAULT charset = utf8;

alter table tb_wechat_auth
    add unique index (open_id);


-- ------------------------------------------------------------------------------------------------------------本地账号表
create table `tb_local_auth`
(
    `local_auth_id`  int(10)      not null auto_increment,
    `user_id`        int(10)      not null,
    `username`       varchar(128) not null,
    `password`       varchar(128) not null,
    `create_time`    datetime default null,
    `last_edit_time` datetime default null,
    primary key (`local_auth_id`),
    unique key `uk_local_profile` (`username`),
    constraint `fk_localauth_profile` foreign key (`user_id`) references `tb_person_info` (`user_id`)
) engine = InnoDB
  auto_increment = 1
  DEFAULT charset = utf8;


-- ----------------------------------------------------------------------------------------------------------------头条表
CREATE TABLE `tb_head_line`
(
    `line_id`        int(100)      NOT NULL AUTO_INCREMENT,
    `line_name`      varchar(1000)          DEFAULT NULL,
    `line_link`      varchar(2000) NOT NULL,
    `line_img`       varchar(2000) NOT NULL,
    `priority`       int(2)                 DEFAULT NULL,
    `enable_status`  int(2)        NOT NULL DEFAULT '0',
    `create_time`    datetime               DEFAULT NULL,
    `last_edit_time` datetime               DEFAULT NULL,
    PRIMARY KEY (`line_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


-- ------------------------------------------------------------------------------------------------------------商店类别表
CREATE TABLE `tb_shop_category`
(
    `shop_category_id`   int(11)      NOT NULL AUTO_INCREMENT,
    `shop_category_name` varchar(100) NOT NULL DEFAULT '',
    `shop_category_desc` varchar(1000)         DEFAULT '',
    `shop_category_img`  varchar(2000)         DEFAULT NULL,
    `priority`           int(2)       NOT NULL DEFAULT '0',
    `create_time`        datetime              DEFAULT NULL,
    `last_edit_time`     datetime              DEFAULT NULL,
    `parent_id`          int(11)               DEFAULT NULL,
    PRIMARY KEY (`shop_category_id`),
    CONSTRAINT `fk_shop_category_self` FOREIGN KEY (`parent_id`) REFERENCES `tb_shop_category` (`shop_category_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


-- ---------------------------------------------------------------------------------------------------------------商店表
CREATE TABLE `tb_shop`
(
    `shop_id`          int(10)                              NOT NULL AUTO_INCREMENT,
    `owner_id`         int(10)                              NOT NULL COMMENT '店铺创建人',
    `area_id`          int(5)                                        DEFAULT NULL,
    `shop_category_id` int(11)                                       DEFAULT NULL,
    `shop_name`        varchar(256) COLLATE utf8_unicode_ci NOT NULL,
    `shop_desc`        varchar(1024) COLLATE utf8_unicode_ci         DEFAULT NULL,
    `shop_addr`        varchar(200) COLLATE utf8_unicode_ci          DEFAULT NULL,
    `phone`            varchar(128) COLLATE utf8_unicode_ci          DEFAULT NULL,
    `shop_img`         varchar(1024) COLLATE utf8_unicode_ci         DEFAULT NULL,
    `priority`         int(3)                                        DEFAULT '0',
    `create_time`      datetime                                      DEFAULT NULL,
    `last_edit_time`   datetime                                      DEFAULT NULL,
    `enable_status`    int(2)                               NOT NULL DEFAULT '0',
    `advice`           varchar(255) COLLATE utf8_unicode_ci          DEFAULT NULL,
    PRIMARY KEY (`shop_id`),
    CONSTRAINT `fk_shop_area` FOREIGN KEY (`area_id`) REFERENCES `tb_area` (`area_id`),
    CONSTRAINT `fk_shop_profile` FOREIGN KEY (`owner_id`) REFERENCES `tb_person_info` (`user_id`),
    CONSTRAINT `fk_shop_shopcate` FOREIGN KEY (`shop_category_id`) REFERENCES `tb_shop_category` (`shop_category_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


-- ------------------------------------------------------------------------------------------------------------产品类别表
CREATE TABLE `tb_product_category`
(
    `product_category_id`   int(11)      NOT NULL AUTO_INCREMENT,
    `product_category_name` varchar(100) NOT NULL,
    `priority`              int(2)                DEFAULT '0',
    `create_time`           datetime              DEFAULT NULL,
    `shop_id`               int(20)      NOT NULL DEFAULT '0',
    PRIMARY KEY (`product_category_id`),
    CONSTRAINT `fk_procate_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


-- ----------------------------------------------------------------------------------------------------------------产品表
CREATE TABLE `tb_product`
(
    `product_id`          int(100)     NOT NULL AUTO_INCREMENT,
    `product_name`        varchar(100) NOT NULL,
    `product_desc`        varchar(2000)         DEFAULT NULL,
    `img_addr`            varchar(2000)         DEFAULT '',
    `normal_price`        varchar(100)          DEFAULT NULL,
    `promotion_price`     varchar(100)          DEFAULT NULL,
    `priority`            int(2)       NOT NULL DEFAULT '0',
    `create_time`         datetime              DEFAULT NULL,
    `last_edit_time`      datetime              DEFAULT NULL,
    `enable_status`       int(2)       NOT NULL DEFAULT '0',
    `product_category_id` int(11)               DEFAULT NULL,
    `shop_id`             int(20)      NOT NULL DEFAULT '0',
    PRIMARY KEY (`product_id`),
    CONSTRAINT `fk_product_procate` FOREIGN KEY (`product_category_id`) REFERENCES `tb_product_category` (`product_category_id`),
    CONSTRAINT `fk_product_shop` FOREIGN KEY (`shop_id`) REFERENCES `tb_shop` (`shop_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;


-- ------------------------------------------------------------------------------------------------------------产品图片表
CREATE TABLE `tb_product_img`
(
    `product_img_id` int(20)       NOT NULL AUTO_INCREMENT,
    `img_addr`       varchar(2000) NOT NULL,
    `img_desc`       varchar(2000) DEFAULT NULL,
    `priority`       int(2)        DEFAULT '0',
    `create_time`    datetime      DEFAULT NULL,
    `product_id`     int(20)       DEFAULT NULL,
    PRIMARY KEY (`product_img_id`),
    CONSTRAINT `fk_proimg_product` FOREIGN KEY (`product_id`) REFERENCES `tb_product` (`product_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;