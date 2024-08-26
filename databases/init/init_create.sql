-- init db drop & create


ALTER TABLE IF EXISTS `DISTRIBUTION_LOG` DROP FOREIGN KEY `FK_PRODUCT_TO_DISTRIBUTION_LOG_1`;
ALTER TABLE IF EXISTS `DISTRIBUTION_LOG` DROP FOREIGN KEY `FK_TRANSPORT_TO_DISTRIBUTION_LOG_1`;

-- DROP FROM ORDER_DETAIL
ALTER TABLE IF EXISTS `ORDER_DETAIL` DROP FOREIGN KEY `FK_PRODUCT_TO_ORDER_DETAIL_1`;
ALTER TABLE IF EXISTS `ORDER_DETAIL` DROP FOREIGN KEY `FK_ORDER_TABLE_TO_ORDER_DETAIL_1`;

-- DROP FROM STORE_STATUS
ALTER TABLE IF EXISTS `STORE_STATUS` DROP FOREIGN KEY `FK_STORE_TO_STORE_STATUS_1`;
ALTER TABLE IF EXISTS `STORE_STATUS` DROP FOREIGN KEY `FK_PRODUCT_TO_STORE_STATUS_1`;

-- DROP FROM SUPPLIER_STATUS
ALTER TABLE IF EXISTS `SUPPLIER_STATUS` DROP FOREIGN KEY `FK_SUPPLIER_TO_SUPPLIER_STATUS_1`;
ALTER TABLE IF EXISTS `SUPPLIER_STATUS` DROP FOREIGN KEY `FK_PRODUCT_TO_SUPPLIER_STATUS_1`;

-- DROP FROM STORE
ALTER TABLE IF EXISTS `STORE` DROP FOREIGN KEY `FK_ROLE_TO_STORE_1`;

-- DROP FROM WAREHOUSE_STATUS
ALTER TABLE IF EXISTS `WAREHOUSE_STATUS` DROP FOREIGN KEY `FK_WAREHOUSE_TO_WAREHOUSE_STATUS_1`;
ALTER TABLE IF EXISTS `WAREHOUSE_STATUS` DROP FOREIGN KEY `FK_PRODUCT_TO_WAREHOUSE_STATUS_1`;

-- DROP FROM WAREHOUSE
ALTER TABLE IF EXISTS `WAREHOUSE` DROP FOREIGN KEY `FK_ROLE_TO_WAREHOUSE_1`;

-- DROP FROM USER_ROLE
ALTER TABLE IF EXISTS `USER_ROLE` DROP FOREIGN KEY `FK_ROLE_TO_USER_ROLE_1`;
ALTER TABLE IF EXISTS `USER_ROLE` DROP FOREIGN KEY `FK_USER_TO_USER_ROLE_1`;

-- DROP FROM PRODUCT
ALTER TABLE IF EXISTS `PRODUCT` DROP FOREIGN KEY `FK_CATEGORY_TO_PRODUCT_1`;

-- DROP ALL TABLES IN THE CORRECT ORDER

DROP TABLE IF EXISTS `DISTRIBUTION_LOG`;
DROP TABLE IF EXISTS `ORDER_DETAIL`;
DROP TABLE IF EXISTS `ORDER_TABLE`;
DROP TABLE IF EXISTS `STORE_STATUS`;
DROP TABLE IF EXISTS `SUPPLIER_STATUS`;
DROP TABLE IF EXISTS `STORE`;
DROP TABLE IF EXISTS `SUPPLIER`;
DROP TABLE IF EXISTS `WAREHOUSE_STATUS`;
DROP TABLE IF EXISTS `WAREHOUSE`;
DROP TABLE IF EXISTS `TRANSPORT`;
DROP TABLE IF EXISTS `PRODUCT`;
DROP TABLE IF EXISTS `CATEGORY`;
DROP TABLE IF EXISTS `USER_ROLE`;
DROP TABLE IF EXISTS `ROLE`;
DROP TABLE IF EXISTS `USER`;


-- 테이블 생성 !
CREATE TABLE `PRODUCT`
(
    `product_uid`  VARCHAR(50) NOT NULL,
    `category_uid` VARCHAR(50) NOT NULL,
    `product_name` VARCHAR(50) NOT NULL,
    `product_size` INT         NOT NULL,
    `product_cost` INT         NOT NULL
);

CREATE TABLE `USER_ROLE`
(
    `user_role_uid` VARCHAR(50) NOT NULL,
    `role_uid`      VARCHAR(50) NOT NULL,
    `user_uid`      VARCHAR(50) NOT NULL
);

CREATE TABLE `USER`
(
    `user_uid`   VARCHAR(50)  NOT NULL,
    `user_name`  VARCHAR(50)  NOT NULL,
    `user_id`    VARCHAR(50)  NOT NULL UNIQUE,
    `user_pw`    VARCHAR(255) NOT NULL,
    `user_email` VARCHAR(50)  NOT NULL UNIQUE,
    `user_tel`   VARCHAR(50)  NOT NULL unique
);

CREATE TABLE `ROLE`
(
    `role_uid`  VARCHAR(50) NOT NULL,
    `role_name` VARCHAR(50) NOT NULL
);

CREATE TABLE `WAREHOUSE`
(
    `warehouse_uid`      VARCHAR(50)  NOT NULL,
    `role_uid`           VARCHAR(50)  NOT NULL,
    `warehouse_name`     VARCHAR(50)  NOT NULL,
    `warehouse_address`  VARCHAR(255) NOT NULL,
    `warehouse_capacity` INT          NOT NULL
);

CREATE TABLE `SUPPLIER`
(
    `supplier_uid`     VARCHAR(50)  NOT NULL,
    `supplier_name`    VARCHAR(50)  NOT NULL,
    `supplier_address` VARCHAR(255) NOT NULL
);

CREATE TABLE `WAREHOUSE_STATUS`
(
    `warehouse_status_uid` VARCHAR(50) NOT NULL,
    `warehouse_uid`        VARCHAR(50) NOT NULL,
    `warehouse_amount`     INT         NOT NULL,
    `warehouse_tx`         VARCHAR(50) NOT NULL COMMENT '입고 or 출고',
    `warehouse_date`       DATETIME    NOT NULL,
    `product_uid`          VARCHAR(50) NOT NULL
);

CREATE TABLE `TRANSPORT`
(
    `transport_uid`      VARCHAR(50) NOT NULL,
    `transport_type`     VARCHAR(50) NOT NULL,
    `transport_capacity` INT         NOT NULL
);

CREATE TABLE `CATEGORY`
(
    `category_uid`  VARCHAR(50) NOT NULL,
    `category_name` VARCHAR(50) NOT NULL
);

CREATE TABLE `STORE`
(
    `store_uid`     VARCHAR(50)  NOT NULL,
    `role_uid`      VARCHAR(50)  NOT NULL,
    `store_name`    VARCHAR(50)  NOT NULL,
    `store_address` VARCHAR(255) NOT NULL
);

CREATE TABLE `SUPPLIER_STATUS`
(
    `supplier_status_uid` VARCHAR(50) NOT NULL,
    `supplier_uid`        VARCHAR(50) NOT NULL,
    `supplier_amount`     INT         NOT NULL,
    `product_uid`         VARCHAR(50) NOT NULL
);

CREATE TABLE `STORE_STATUS`
(
    `store_status_uid` VARCHAR(50) NOT NULL,
    `store_uid`        VARCHAR(50) NOT NULL,
    `store_amount`     INT         NOT NULL,
    `store_tx`         VARCHAR(50) NOT NULL COMMENT '입고 or 출고',
    `store_date`       DATETIME    NOT NULL,
    `product_uid`      VARCHAR(50) NOT NULL
);

CREATE TABLE `ORDER_TABLE`
(
    `order_uid`      VARCHAR(50) NOT NULL,
    `order_status`   VARCHAR(50) NOT NULL COMMENT '요청/승인/취소',
    `order_by_type`  VARCHAR(50) NOT NULL,
    `order_by_uid`   VARCHAR(50) NOT NULL,
    `supply_by_type` VARCHAR(50) NOT NULL,
    `supply_by_uid`  VARCHAR(50) NOT NULL
);

CREATE TABLE `ORDER_DETAIL`
(
    `order_detail_uid` VARCHAR(50) NOT NULL,
    `order_date`       DATETIME    NOT NULL,
    `order_amount`     INT         NOT NULL,
    `product_uid`      VARCHAR(50) NOT NULL,
    `order_uid`        VARCHAR(50) NOT NULL
);

CREATE TABLE `DISTRIBUTION_LOG`
(
    `distribution_log_uid` VARCHAR(50) NOT NULL,
    `product_uid`          VARCHAR(50) NOT NULL,
    `source_type`          VARCHAR(50) NOT NULL,
    `source_uid`           VARCHAR(50) NOT NULL,
    `destination_type`     VARCHAR(50) NOT NULL,
    `destination_uid`      VARCHAR(50) NOT NULL,
    `amount`               INT         NOT NULL,
    `log_date`             DATETIME    NOT NULL,
    `transport_uid`        VARCHAR(50) NOT NULL
);

ALTER TABLE `PRODUCT`
    ADD CONSTRAINT `PK_PRODUCT` PRIMARY KEY (
                                             `product_uid`
        );

ALTER TABLE `USER_ROLE`
    ADD CONSTRAINT `PK_USER_ROLE` PRIMARY KEY (
                                               `user_role_uid`
        );

ALTER TABLE `USER`
    ADD CONSTRAINT `PK_USER` PRIMARY KEY (
                                          `user_uid`
        );

ALTER TABLE `ROLE`
    ADD CONSTRAINT `PK_ROLE` PRIMARY KEY (
                                          `role_uid`
        );

ALTER TABLE `WAREHOUSE`
    ADD CONSTRAINT `PK_WAREHOUSE` PRIMARY KEY (
                                               `warehouse_uid`
        );

ALTER TABLE `SUPPLIER`
    ADD CONSTRAINT `PK_SUPPLIER` PRIMARY KEY (
                                              `supplier_uid`
        );

ALTER TABLE `WAREHOUSE_STATUS`
    ADD CONSTRAINT `PK_WAREHOUSE_STATUS` PRIMARY KEY (
                                                      `warehouse_status_uid`
        );

ALTER TABLE `TRANSPORT`
    ADD CONSTRAINT `PK_TRANSPORT` PRIMARY KEY (
                                               `transport_uid`
        );

ALTER TABLE `CATEGORY`
    ADD CONSTRAINT `PK_CATEGORY` PRIMARY KEY (
                                              `category_uid`
        );

ALTER TABLE `STORE`
    ADD CONSTRAINT `PK_STORE` PRIMARY KEY (
                                           `store_uid`
        );

ALTER TABLE `SUPPLIER_STATUS`
    ADD CONSTRAINT `PK_SUPPLIER_STATUS` PRIMARY KEY (
                                                     `supplier_status_uid`
        );

ALTER TABLE `STORE_STATUS`
    ADD CONSTRAINT `PK_STORE_STATUS` PRIMARY KEY (
                                                  `store_status_uid`
        );

ALTER TABLE `ORDER_TABLE`
    ADD CONSTRAINT `PK_ORDER_TABLE` PRIMARY KEY (
                                                 `order_uid`
        );

ALTER TABLE `ORDER_DETAIL`
    ADD CONSTRAINT `PK_ORDER_DETAIL` PRIMARY KEY (
                                                  `order_detail_uid`
        );

ALTER TABLE `DISTRIBUTION_LOG`
    ADD CONSTRAINT `PK_DISTRIBUTION_LOG` PRIMARY KEY (
                                                      `distribution_log_uid`
        );

ALTER TABLE `PRODUCT`
    ADD CONSTRAINT `FK_CATEGORY_TO_PRODUCT_1` FOREIGN KEY (
                                                           `category_uid`
        )
        REFERENCES `CATEGORY` (
                               `category_uid`
            );

ALTER TABLE `USER_ROLE`
    ADD CONSTRAINT `FK_ROLE_TO_USER_ROLE_1` FOREIGN KEY (
                                                         `role_uid`
        )
        REFERENCES `ROLE` (
                           `role_uid`
            );

ALTER TABLE `USER_ROLE`
    ADD CONSTRAINT `FK_USER_TO_USER_ROLE_1` FOREIGN KEY (
                                                         `user_uid`
        )
        REFERENCES `USER` (
                           `user_uid`
            );

ALTER TABLE `WAREHOUSE`
    ADD CONSTRAINT `FK_ROLE_TO_WAREHOUSE_1` FOREIGN KEY (
                                                         `role_uid`
        )
        REFERENCES `ROLE` (
                           `role_uid`
            );

ALTER TABLE `WAREHOUSE_STATUS`
    ADD CONSTRAINT `FK_WAREHOUSE_TO_WAREHOUSE_STATUS_1` FOREIGN KEY (
                                                                     `warehouse_uid`
        )
        REFERENCES `WAREHOUSE` (
                                `warehouse_uid`
            );

ALTER TABLE `WAREHOUSE_STATUS`
    ADD CONSTRAINT `FK_PRODUCT_TO_WAREHOUSE_STATUS_1` FOREIGN KEY (
                                                                   `product_uid`
        )
        REFERENCES `PRODUCT` (
                              `product_uid`
            );

ALTER TABLE `STORE`
    ADD CONSTRAINT `FK_ROLE_TO_STORE_1` FOREIGN KEY (
                                                     `role_uid`
        )
        REFERENCES `ROLE` (
                           `role_uid`
            );

ALTER TABLE `SUPPLIER_STATUS`
    ADD CONSTRAINT `FK_SUPPLIER_TO_SUPPLIER_STATUS_1` FOREIGN KEY (
                                                                   `supplier_uid`
        )
        REFERENCES `SUPPLIER` (
                               `supplier_uid`
            );

ALTER TABLE `SUPPLIER_STATUS`
    ADD CONSTRAINT `FK_PRODUCT_TO_SUPPLIER_STATUS_1` FOREIGN KEY (
                                                                  `product_uid`
        )
        REFERENCES `PRODUCT` (
                              `product_uid`
            );

ALTER TABLE `STORE_STATUS`
    ADD CONSTRAINT `FK_STORE_TO_STORE_STATUS_1` FOREIGN KEY (
                                                             `store_uid`
        )
        REFERENCES `STORE` (
                            `store_uid`
            );

ALTER TABLE `STORE_STATUS`
    ADD CONSTRAINT `FK_PRODUCT_TO_STORE_STATUS_1` FOREIGN KEY (
                                                               `product_uid`
        )
        REFERENCES `PRODUCT` (
                              `product_uid`
            );

ALTER TABLE `ORDER_DETAIL`
    ADD CONSTRAINT `FK_PRODUCT_TO_ORDER_DETAIL_1` FOREIGN KEY (
                                                               `product_uid`
        )
        REFERENCES `PRODUCT` (
                              `product_uid`
            );

ALTER TABLE `ORDER_DETAIL`
    ADD CONSTRAINT `FK_ORDER_TABLE_TO_ORDER_DETAIL_1` FOREIGN KEY (
                                                                   `order_uid`
        )
        REFERENCES `ORDER_TABLE` (
                                  `order_uid`
            );

ALTER TABLE `DISTRIBUTION_LOG`
    ADD CONSTRAINT `FK_PRODUCT_TO_DISTRIBUTION_LOG_1` FOREIGN KEY (
                                                                   `product_uid`
        )
        REFERENCES `PRODUCT` (
                              `product_uid`
            );

ALTER TABLE `DISTRIBUTION_LOG`
    ADD CONSTRAINT `FK_TRANSPORT_TO_DISTRIBUTION_LOG_1` FOREIGN KEY (
                                                                     `transport_uid`
        )
        REFERENCES `TRANSPORT` (
                                `transport_uid`
            );

