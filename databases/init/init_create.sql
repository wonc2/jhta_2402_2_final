-- 1. 밀키트 테이블 생성
CREATE TABLE MEALKIT (
                         MEALKIT_ID VARCHAR(50) PRIMARY KEY, -- 밀키트PK
                         NAME VARCHAR(50) NOT NULL UNIQUE ,-- 밀키트이름
                         PRICE INT NOT NULL -- 밀키트 가격
);
-- 2. 재료 테이블 생성
CREATE TABLE SOURCE (
                        SOURCE_ID VARCHAR(50) PRIMARY KEY, -- 재료PK
                        NAME VARCHAR(50) NOT NULL UNIQUE -- 재료이름
);
-- 3. 밀키트 재료 테이블 생성
CREATE TABLE KIT_SOURCE (
                            KIT_SOURCE_ID VARCHAR(50) PRIMARY KEY,
                            MEALKIT_ID VARCHAR(50) NOT NULL,
                            SOURCE_ID VARCHAR(50) NOT NULL,
                            QUANTITY INT NOT NULL,
                            FOREIGN KEY (MEALKIT_ID) REFERENCES MEALKIT(MEALKIT_ID),
                            FOREIGN KEY (SOURCE_ID) REFERENCES SOURCE(SOURCE_ID)
);
-- 4. 밀키트 판매 업체 테이블
CREATE TABLE KIT_COMPANY (
                             KIT_COMPANY_ID VARCHAR(50) PRIMARY KEY, -- 밀키트판매업체PK
                             NAME VARCHAR(50) NOT NULL UNIQUE , -- 판매업체이름
                             ADDRESS VARCHAR(50) NOT NULL UNIQUE -- 판매업체주소
);
-- 5. 생산업체 테이블
CREATE TABLE PRODUCT_COMPANY (
                                 PRODUCT_COMPANY_ID VARCHAR(50) PRIMARY KEY, -- 생산업체PK
                                 NAME VARCHAR(50) NOT NULL UNIQUE , -- 생산업체이름
                                 ADDRESS VARCHAR(50) NOT NULL unique -- 생산업체주소
);
-- 6. 생산 업체별 재료 가격 테이블
CREATE TABLE SOURCE_PRICE (
                              SOURCE_PRICE_ID VARCHAR(50) PRIMARY KEY, -- 가격PK
                              PRODUCT_COMPANY_ID VARCHAR(50) NOT NULL, -- 생산업체FK
                              SOURCE_ID VARCHAR(50) NOT NULL, -- 재료FK
                              PRICE INT NOT NULL, -- 가격
                              FOREIGN KEY (PRODUCT_COMPANY_ID) REFERENCES PRODUCT_COMPANY(PRODUCT_COMPANY_ID),
                              FOREIGN KEY (SOURCE_ID) REFERENCES SOURCE(SOURCE_ID),
                              CONSTRAINT unique_company_source UNIQUE (PRODUCT_COMPANY_ID, SOURCE_ID)
);
-- 7 가격 변경 로그 테이블
CREATE TABLE SOURCE_PRICE_HISTORY(
                                     SOURCE_PRICE_HISTORY_ID VARCHAR(50) PRIMARY KEY,
                                     SOURCE_PRICE_ID VARCHAR(50) NOT NULL,
                                     OLD_PRICE INT NOT NULL,
                                     NEW_PRICE INT NOT NULL,
                                     CHANGE_DATE DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
                                     FOREIGN KEY (SOURCE_PRICE_ID) REFERENCES SOURCE_PRICE(SOURCE_PRICE_ID)
);
-- 8. 상태 테이블
CREATE TABLE STATUS (
                        STATUS_ID INT PRIMARY KEY, -- 상태 PK
                        STATUS VARCHAR(50) UNIQUE NOT NULL -- 상태
);

-- 9. 밀키트 주문 테이블
CREATE TABLE KIT_ORDER (
                           KIT_ORDER_ID VARCHAR(50) PRIMARY KEY, -- 주문PK
                           KIT_COMPANY_ID VARCHAR(50) NOT NULL, -- 밀키트판매업체FK
                           MEALKIT_ID VARCHAR(50) NOT NULL, -- 밀키트FK
                           PRICE    INT NOT NULL,  -- 밀키트 가격
                           QUANTITY INT NOT NULL, -- 개수
                           PRODUCT_ORDER_DATE DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 주문 일자
                           STATUS_ID INT NOT NULL, -- 상태FK
                           FOREIGN KEY (KIT_COMPANY_ID) REFERENCES KIT_COMPANY(KIT_COMPANY_ID),
                           FOREIGN KEY (MEALKIT_ID) REFERENCES MEALKIT(MEALKIT_ID),
                           FOREIGN KEY (STATUS_ID) REFERENCES STATUS(STATUS_ID)
);
-- 10. 밀키트 주문로그 테이블
CREATE TABLE KIT_ORDER_LOG (
                               KIT_ORDER_LOG_ID VARCHAR(50) PRIMARY KEY, -- 주문로그PK
                               KIT_ORDER_ID VARCHAR(50) NOT NULL, -- 주문FK
                               STATUS_ID INT NOT NULL, -- 상태FK
                               PRODUCT_ORDER_DATE DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 날짜
                               FOREIGN KEY (KIT_ORDER_ID) REFERENCES KIT_ORDER(KIT_ORDER_ID),
                               FOREIGN KEY (STATUS_ID) REFERENCES STATUS(STATUS_ID)
);
-- 11. 생산주문(발주) 테이블
CREATE TABLE PRODUCT_ORDER (
                               PRODUCT_ORDER_ID VARCHAR(50) PRIMARY KEY, -- 발주 PK
                               PRODUCT_COMPANY_ID VARCHAR(50) NOT NULL, -- 생산업체 FK
                               SOURCE_ID VARCHAR(50) NOT NULL, -- 재료 FK
                               KIT_ORDER_ID VARCHAR(50) NOT NULL, -- 밀키트 주문 FK
                               QUANTITY INT NOT NULL, -- 개수
                               PRICE INT NOT NULL, -- 가격
                               PRODUCT_ORDER_DATE DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 주문 일자
                               STATUS_ID INT NOT NULL, -- 상태 FK
                               FOREIGN KEY (PRODUCT_COMPANY_ID) REFERENCES PRODUCT_COMPANY(PRODUCT_COMPANY_ID), -- 회사 참조
                               FOREIGN KEY (SOURCE_ID) REFERENCES SOURCE(SOURCE_ID), -- 재료 참조
                               FOREIGN KEY (KIT_ORDER_ID) REFERENCES KIT_ORDER(KIT_ORDER_ID), -- 밀키트 주문 참조
                               FOREIGN KEY (STATUS_ID) REFERENCES STATUS(STATUS_ID) -- 상태 참조
);

-- 12. 생산주문(발주) 로그 테이블
CREATE TABLE PRODUCT_ORDER_LOG (
                                   PRODUCT_ORDER_LOG_ID VARCHAR(50) PRIMARY KEY, -- 발주로그PK
                                   PRODUCT_ORDER_ID VARCHAR(50) NOT NULL, -- 발주FK
                                   STATUS_ID INT NOT NULL, -- 상태FK
                                   PRODUCT_ORDER_DATE DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL, -- 날짜
                                   FOREIGN KEY (PRODUCT_ORDER_ID) REFERENCES PRODUCT_ORDER(PRODUCT_ORDER_ID),
                                   FOREIGN KEY (STATUS_ID) REFERENCES STATUS(STATUS_ID)
);
-- 13. 권한 테이블
CREATE TABLE ROLE (
                      ROLE_ID INT PRIMARY KEY, -- 권한PK
                      ROLE_NAME VARCHAR(50) NOT NULL UNIQUE -- 권한이름
);
-- 14. 유저 테이블
CREATE TABLE USER (
                      USER_PK VARCHAR(50) PRIMARY KEY, -- 유저PK
                      USER_NAME VARCHAR(50) NOT NULL , -- 유저이름
                      USER_ID VARCHAR(50) NOT NULL UNIQUE , -- 유저아이디
                      USER_PASSWORD VARCHAR(128) NOT NULL, -- 유저 비밀번호
                      USER_EMAIL VARCHAR(50) NOT NULL UNIQUE, -- 유저 이메일
                      USER_TEL VARCHAR(50) NOT NULL UNIQUE, -- 전화번호
                      ROLE_NAME VARCHAR(50) NOT NULL -- 권한FK
);
-- 15 생산 유저 테이블
CREATE TABLE PRODUCT_COMPANY_MEMBER(
                                       PRODUCT_COMPANY_MEMBER_ID VARCHAR(50) PRIMARY KEY NOT NULL,
                                       USER_ID                   VARCHAR(50) NOT NULL,
                                       PRODUCT_COMPANY_ID        VARCHAR(50) NOT NULL,
                                       FOREIGN KEY (USER_ID) REFERENCES USER (USER_PK),
                                       FOREIGN KEY (PRODUCT_COMPANY_ID) REFERENCES PRODUCT_COMPANY (PRODUCT_COMPANY_ID)
);

-- 16. 밀키트 업체 별 창고 테이블
CREATE TABLE KIT_STORAGE (
                             KIT_STORAGE_ID VARCHAR(50) PRIMARY KEY,    -- 창고번호 PK
                             KIT_COMPANY_ID VARCHAR(50) NOT NULL,                   -- 판매업체 FK
                             MEALKIT_ID VARCHAR(50) NOT NULL,                       -- 밀키트 FK
                             QUANTITY INT NOT NULL ,                      -- 재고 개수

    -- 판매업체와 밀키트의 조합을 유니크하게 설정
                             CONSTRAINT unique_company_mealkit UNIQUE (KIT_COMPANY_ID, MEALKIT_ID),

    -- 외래키 설정
                             FOREIGN KEY (KIT_COMPANY_ID) REFERENCES KIT_COMPANY(KIT_COMPANY_ID),
                             FOREIGN KEY (MEALKIT_ID) REFERENCES MEALKIT(MEALKIT_ID)
);


-- 웨어하우스 관련
-- 17. LOGISTICS_WAREHOUSE 테이블 생성

CREATE TABLE `LOGISTICS_WAREHOUSE`
(
    `LOGISTICS_WAREHOUSE_ID` VARCHAR(50) PRIMARY KEY NOT NULL,
    `WAREHOUSE_NAME`         VARCHAR(50) NOT NULL UNIQUE
);

-- 18. LOGISTICS_WAREHOUSE_SOURCE 테이블 생성
CREATE TABLE LOGISTICS_WAREHOUSE_STACK (
    LOGISTICS_WAREHOUSE_STACK_ID VARCHAR(50) PRIMARY KEY,
    SOURCE_ID VARCHAR(50),
    LOGISTICS_WAREHOUSE_ID VARCHAR(50),
    QUANTITY INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 19 농장별 생산 재고 테이블
CREATE TABLE `SOURCE_WAREHOUSE`
(
    `SOURCE_WAREHOUSE_ID`   VARCHAR(50),
    `QUANTITY`              INT NOT NULL,
    `PRODUCE_DATE`          DATETIME NOT NULL,
    `SOURCE_PRICE_ID`       VARCHAR(50) ,
    PRIMARY KEY(`SOURCE_WAREHOUSE_ID`),
    FOREIGN KEY (`SOURCE_PRICE_ID`) REFERENCES `SOURCE_PRICE` (`SOURCE_PRICE_ID`)
);
