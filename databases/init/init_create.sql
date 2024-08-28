

-- 1. 밀키트 테이블 생성
CREATE TABLE MEALKIT (
                         MEALKIT_ID VARCHAR(50) PRIMARY KEY, -- 밀키트PK
                         NAME VARCHAR(50) NOT NULL -- 밀키트이름
);
-- 2. 밀키트 판매 업체 테이블
CREATE TABLE KIT_COMPANY (
                             KIT_COMPANY_ID VARCHAR(50) PRIMARY KEY, -- 밀키트PK
                             NAME VARCHAR(50) NOT NULL, -- 판매업체이름
                             ADDRESS VARCHAR(50) NOT NULL -- 판매업체주소,
);
-- 3. 생산업체 테이블
CREATE TABLE PRODUCT_COMPANY (
                                 PRODUCT_COMPANY_ID VARCHAR(50) PRIMARY KEY, -- 생산업체PK
                                 NAME VARCHAR(50) NOT NULL, -- 생산업체이름
                                 ADDRESS VARCHAR(50) NOT NULL -- 생산업체주소,
);
-- 4. 재료 테이블
CREATE TABLE SOURCE (
                        SOURCE_ID VARCHAR(50) PRIMARY KEY, -- 재료PK
                        NAME VARCHAR(50) NOT NULL -- 재료이름
);
-- 5. 생산 업체별 재료 가격 테이블
CREATE TABLE SOURCE_PRICE (
                              SOURCE_PRICE_ID VARCHAR(50) PRIMARY KEY, -- 가격PK
                              PRODUCT_COMPANY_ID VARCHAR(50), -- 생산업체FK
                              SOURCE_ID VARCHAR(50), -- 재료FK
                              PRICE INT, -- 가격
                              FOREIGN KEY (PRODUCT_COMPANY_ID) REFERENCES PRODUCT_COMPANY(PRODUCT_COMPANY_ID),
                              FOREIGN KEY (SOURCE_ID) REFERENCES SOURCE(SOURCE_ID)
);
-- 6. 밀키트 가격 테이블
CREATE TABLE KIT_TOTAL_PRICE (
                                 KIT_TOTAL_PRICE_ID VARCHAR(50) PRIMARY KEY, -- 밀키트가격PK
                                 MEALKIT_ID VARCHAR(50), -- 밀키트FK
                                 PURCHASER_PRICE INT, -- 구매가
                                 SALES_PRICE INT, -- 판매가
                                 FOREIGN KEY (MEALKIT_ID) REFERENCES MEALKIT(MEALKIT_ID)
);
-- 7. 밀키트 재료 가격 테이블
CREATE TABLE KIT_SOURCE_PRICE (
                                  KIT_SOURCE_PRICE_ID VARCHAR(50) PRIMARY KEY, -- 밀키트재료가격PK
                                  MEALKIT_ID VARCHAR(50), -- 밀키트FK
                                  SOURCE_PRICE_ID VARCHAR(50), -- 가격FK
                                  QUENTITY INT, -- 개수
                                  FOREIGN KEY (MEALKIT_ID) REFERENCES MEALKIT(MEALKIT_ID),
                                  FOREIGN KEY (SOURCE_PRICE_ID) REFERENCES SOURCE_PRICE(SOURCE_PRICE_ID)
);
-- 8. 상태 테이블
CREATE TABLE STATUS (
                        STATUS_ID INT PRIMARY KEY, -- 상태 PK
                        STATUS VARCHAR(50) -- 상태
);
-- 9. 생산주문(발주) 테이블
CREATE TABLE PRODUCT_ORDER (
                               PRODUCT_ORDER_ID VARCHAR(50) PRIMARY KEY, -- 발주PK
                               KIT_SOURCE_PRICE_ID VARCHAR(50), -- 밀키트재료가격FK
                               QUENTITY INT, -- 개수
                               PRODUCT_ORDER_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,  -- 주문 일자
                               STATUS_ID INT, -- 상태FK
                               FOREIGN KEY (KIT_SOURCE_PRICE_ID) REFERENCES KIT_SOURCE_PRICE(KIT_SOURCE_PRICE_ID),
                               FOREIGN KEY (STATUS_ID) REFERENCES STATUS(STATUS_ID)
);
-- 10. 생산주문(발주) 로그 테이블
CREATE TABLE PRODUCT_ORDER_LOG (
                                   PRODUCT_ORDER_LOG_ID VARCHAR(50) PRIMARY KEY, -- 발주로그PK
                                   PRODUCT_ORDER_ID VARCHAR(50), -- 발주FK
                                   STATUS_ID INT, -- 상태FK
                                   PRODUCT_ORDER_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,  -- 날짜
                                   FOREIGN KEY (PRODUCT_ORDER_ID) REFERENCES PRODUCT_ORDER(PRODUCT_ORDER_ID),
                                   FOREIGN KEY (STATUS_ID) REFERENCES STATUS(STATUS_ID)
);
-- 11. 밀키트 주문 테이블
CREATE TABLE KIT_ORDER (
                           KIT_ORDER_ID VARCHAR(50) PRIMARY KEY, -- 주문PK
                           KIT_COMPANY_ID VARCHAR(50), -- 밀키트판매업체FK
                           MEALKIT_ID VARCHAR(50), -- 밀키트FK
                           QUENTITY INT, -- 개수
                           PRODUCT_ORDER_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,  -- 주문 일자
                           STATUS_ID INT, -- 상태FK
                           FOREIGN KEY (KIT_COMPANY_ID) REFERENCES KIT_COMPANY(KIT_COMPANY_ID),
                           FOREIGN KEY (MEALKIT_ID) REFERENCES MEALKIT(MEALKIT_ID),
                           FOREIGN KEY (STATUS_ID) REFERENCES STATUS(STATUS_ID)
);
-- 12. 밀키트 주문로그 테이블
CREATE TABLE KIT_ORDER_LOG (
                               KIT_ORDER_LOG_ID VARCHAR(50) PRIMARY KEY, -- 주문로그PK
                               KIT_ORDER_ID VARCHAR(50), -- 주문FK
                               STATUS_ID INT, -- 상태FK
                               PRODUCT_ORDER_DATE DATETIME DEFAULT CURRENT_TIMESTAMP,  -- 날짜
                               FOREIGN KEY (KIT_ORDER_ID) REFERENCES KIT_ORDER(KIT_ORDER_ID),
                               FOREIGN KEY (STATUS_ID) REFERENCES STATUS(STATUS_ID)
);
-- 13. 권한 테이블
CREATE TABLE ROLE (
                      ROLE_ID	INT PRIMARY KEY, -- 권한PK
                      ROLE_NAME VARCHAR(50) NOT NULL -- 권한이름
);
-- 14. 유저 테이블
CREATE TABLE USER (
                      USER_PK	VARCHAR(50) PRIMARY KEY, -- 유저PK
                      USER_NAME VARCHAR(50) NOT NULL, -- 유저이름
                      USER_ID	VARCHAR(50) NOT NULL, -- 유저아이디
                      USER_PASSWORD VARCHAR(50) NOT NULL, -- 유저 비밀번호
                      USER_EMAIL VARCHAR(50) NOT NULL, -- 유저 이메일
                      USER_TEL VARCHAR(50) NOT NULL, -- 전화번호
                      ROLE_ID INT, -- 권한FK
                      FOREIGN KEY (ROLE_ID) REFERENCES ROLE(ROLE_ID)
);
