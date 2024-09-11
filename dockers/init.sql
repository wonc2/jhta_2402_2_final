DROP TABLE IF EXISTS KIT_ORDER_LOG;
DROP TABLE IF EXISTS PRODUCT_ORDER_LOG;
DROP TABLE IF EXISTS PRODUCT_ORDER;
DROP TABLE IF EXISTS KIT_ORDER;
DROP TABLE IF EXISTS SOURCE_PRICE_HISTORY;
DROP TABLE IF EXISTS SOURCE_WAREHOUSE;
DROP TABLE IF EXISTS SOURCE_PRICE;
DROP TABLE IF EXISTS KIT_SOURCE;
DROP TABLE IF EXISTS LOGISTICS_WAREHOUSE_STACK;
DROP TABLE IF EXISTS LOGISTICS_WAREHOUSE;
DROP TABLE IF EXISTS PRODUCT_COMPANY_MEMBER;
DROP TABLE IF EXISTS PRODUCT_COMPANY;
DROP TABLE IF EXISTS KIT_STORAGE;
DROP TABLE IF EXISTS KIT_COMPANY;
DROP TABLE IF EXISTS MEALKIT;
DROP TABLE IF EXISTS USER;
DROP TABLE IF EXISTS STATUS;
DROP TABLE IF EXISTS SOURCE;
DROP TABLE IF EXISTS ROLE;

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


-- 1. 밀키트 테이블 데이터 삽입
INSERT INTO MEALKIT (MEALKIT_ID, NAME, PRICE)
VALUES (UUID(), '비빔밥', 1000),
       (UUID(), '된장찌개', 2000),
       (UUID(), '김치찌개', 5000);

-- 2. 재료 테이블 데이터 삽입
INSERT INTO SOURCE (SOURCE_ID, NAME)
VALUES (UUID(), '쌀'),
       (UUID(), '김치'),
       (UUID(), '된장'),
       (UUID(), '두부'),
       (UUID(), '소고기');

-- 3. 밀키트 재료 테이블 데이터 삽입
INSERT INTO KIT_SOURCE (KIT_SOURCE_ID, MEALKIT_ID, SOURCE_ID, QUANTITY)
VALUES (UUID(), (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '비빔밥'), (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '쌀'),
        1),
       (UUID(), (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '비빔밥'), (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '김치'),
        1),
       (UUID(), (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '비빔밥'), (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '된장'),
        1),
       (UUID(), (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '된장찌개'), (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '된장'),
        1),
       (UUID(), (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '된장찌개'), (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '두부'),
        1),
       (UUID(), (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '된장찌개'), (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '소고기'),
        1),
       (UUID(), (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '김치찌개'), (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '김치'),
        1),
       (UUID(), (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '김치찌개'), (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '두부'),
        1),
       (UUID(), (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '김치찌개'), (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '소고기'),
        1);


-- 4. 밀키트 판매 업체 테이블 데이터 삽입
INSERT INTO KIT_COMPANY (KIT_COMPANY_ID, NAME, ADDRESS)
VALUES (UUID(), '한식명가', '서울시 강남구'),
       (UUID(), '정통한식', '서울시 종로구'),
       (UUID(), '맛있는집', '서울시 송파구');

-- 5. 생산업체 테이블 데이터 삽입
INSERT INTO PRODUCT_COMPANY (PRODUCT_COMPANY_ID, NAME, ADDRESS)
VALUES (UUID(), '농협', '서울시 중구'),
       (UUID(), '한림', '서울시 서초구'),
       (UUID(), '동서식품', '서울시 강북구');

-- 6. 생산업체별 재료 가격 테이블 데이터 삽입
INSERT INTO SOURCE_PRICE (SOURCE_PRICE_ID, PRODUCT_COMPANY_ID, SOURCE_ID, PRICE)
VALUES (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '농협'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '쌀'), 5000),
       (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '농협'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '김치'), 3000),
       (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '농협'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '된장'), 3000),
       (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '농협'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '두부'), 1000),
       (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '농협'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '소고기'), 4000),
       (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '한림'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '쌀'), 4500),
       (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '한림'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '김치'), 2500),
       (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '한림'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '된장'), 2500),
       (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '한림'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '두부'), 1100),
       (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '한림'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '소고기'), 3700),
       (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '동서식품'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '쌀'), 5100),
       (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '동서식품'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '김치'), 2700),
       (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '동서식품'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '된장'), 2800),
       (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '동서식품'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '두부'), 800),
       (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '동서식품'),
        (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '소고기'), 4100);



-- 7. 상태 테이블 데이터 삽입
INSERT INTO STATUS (STATUS_ID, STATUS)
VALUES (1, '처리전'),
       (2, '처리중'),
       (3, '처리완료'),
       (4, '취소'),
       (5, '입고대기'),
       (6, '출고대기'),
       (7, '입고'),
       (8, '출고')
;


-- 8. 밀키트 주문 테이블 데이터 삽입

INSERT INTO KIT_ORDER (KIT_ORDER_ID, KIT_COMPANY_ID, MEALKIT_ID,PRICE, QUANTITY, PRODUCT_ORDER_DATE, STATUS_ID)
VALUES
    -- 1. 비빔밥에 대한 주문
    (UUID(), (SELECT KIT_COMPANY_ID FROM KIT_COMPANY WHERE NAME = '한식명가'),
     (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '비빔밥'),1000, 100, '2024-08-15 00:00:00',
     (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전')),
    -- 2. 된장찌개에 대한 주문
    (UUID(), (SELECT KIT_COMPANY_ID FROM KIT_COMPANY WHERE NAME = '정통한식'),
     (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '된장찌개'),2000, 200, '2024-08-16 00:00:00',
     (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전')),
    -- 3. 김치찌개에 대한 주문
    (UUID(), (SELECT KIT_COMPANY_ID FROM KIT_COMPANY WHERE NAME = '맛있는집'),
     (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '김치찌개'),5000, 150, '2024-08-17 00:00:00',
     (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전'));

-- 9. 밀키트 주문로그 테이블 데이터 삽입

INSERT INTO KIT_ORDER_LOG (KIT_ORDER_LOG_ID, KIT_ORDER_ID, STATUS_ID, PRODUCT_ORDER_DATE)
VALUES
    -- 1. 비빔밥 주문의 상태 변경 로그
    (UUID(), (SELECT KIT_ORDER_ID
              FROM KIT_ORDER
              WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '비빔밥')
                AND KIT_COMPANY_ID = (SELECT KIT_COMPANY_ID FROM KIT_COMPANY WHERE NAME = '한식명가')
                AND QUANTITY = 100
                AND PRODUCT_ORDER_DATE = '2024-08-15 00:00:00'), (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전'),
     '2024-08-15 12:00:00'),
    -- 2. 된장찌개 주문의 상태 변경 로그
    (UUID(), (SELECT KIT_ORDER_ID
              FROM KIT_ORDER
              WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '된장찌개')
                AND KIT_COMPANY_ID = (SELECT KIT_COMPANY_ID FROM KIT_COMPANY WHERE NAME = '정통한식')
                AND QUANTITY = 200
                AND PRODUCT_ORDER_DATE = '2024-08-16 00:00:00'), (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전'),
     '2024-08-16 15:00:00'),
    -- 3. 김치찌개 주문의 상태 변경 로그
    (UUID(), (SELECT KIT_ORDER_ID
              FROM KIT_ORDER
              WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '김치찌개')
                AND KIT_COMPANY_ID = (SELECT KIT_COMPANY_ID FROM KIT_COMPANY WHERE NAME = '맛있는집')
                AND QUANTITY = 150
                AND PRODUCT_ORDER_DATE = '2024-08-17 00:00:00'), (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전'),
     '2024-08-17 09:00:00');


-- 10. 생산주문(발주) 테이블 데이터 삽입

INSERT INTO PRODUCT_ORDER (PRODUCT_ORDER_ID, PRODUCT_COMPANY_ID, SOURCE_ID, KIT_ORDER_ID, QUANTITY, PRICE, PRODUCT_ORDER_DATE, STATUS_ID)
VALUES
                           -- 1. 비빔밥에 대한 발주 (KIT_ORDER_ID: 비빔밥 주문)
                           (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '농협'),
                            (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '쌀'),
                            (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '비빔밥')),
                            100, (SELECT PRICE FROM SOURCE_PRICE WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '쌀') ORDER BY PRICE ASC LIMIT 1),
    '2024-08-15 00:00:00', (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1)),

    (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '농협'),
     (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '김치'),
     (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '비빔밥')),
     100, (SELECT PRICE FROM SOURCE_PRICE WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '김치') ORDER BY PRICE ASC LIMIT 1),
     '2024-08-15 00:00:00', (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1)),

    (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '농협'),
     (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '된장'),
     (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '비빔밥')),
     100, (SELECT PRICE FROM SOURCE_PRICE WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '된장') ORDER BY PRICE ASC LIMIT 1),
     '2024-08-15 00:00:00', (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1)),

    -- 2. 된장찌개에 대한 발주 (KIT_ORDER_ID: 된장찌개 주문)
    (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '한림'),
     (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '된장'),
     (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '된장찌개')),
     200, (SELECT PRICE FROM SOURCE_PRICE WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '된장') ORDER BY PRICE ASC LIMIT 1),
     '2024-08-16 00:00:00', (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1)),

    (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '한림'),
     (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '두부'),
     (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '된장찌개')),
     200, (SELECT PRICE FROM SOURCE_PRICE WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '두부') ORDER BY PRICE ASC LIMIT 1),
     '2024-08-16 00:00:00', (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1)),

    (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '한림'),
     (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '소고기'),
     (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '된장찌개')),
     200, (SELECT PRICE FROM SOURCE_PRICE WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '소고기') ORDER BY PRICE ASC LIMIT 1),
     '2024-08-16 00:00:00', (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1)),

    -- 3. 김치찌개에 대한 발주 (KIT_ORDER_ID: 김치찌개 주문)
    (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '동서식품'),
     (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '김치'),
     (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '김치찌개')),
     100, (SELECT PRICE FROM SOURCE_PRICE WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '김치') ORDER BY PRICE ASC LIMIT 1),
     '2024-08-17 00:00:00', (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1)),

    (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '동서식품'),
     (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '두부'),
     (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '김치찌개')),
     100, (SELECT PRICE FROM SOURCE_PRICE WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '두부') ORDER BY PRICE ASC LIMIT 1),
     '2024-08-17 00:00:00', (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1)),

    (UUID(), (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '동서식품'),
     (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '소고기'),
     (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '김치찌개')),
     100, (SELECT PRICE FROM SOURCE_PRICE WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '소고기') ORDER BY PRICE ASC LIMIT 1),
     '2024-08-17 00:00:00', (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1));

-- 11 PRODUCT_ORDER_LOG 데이터 삽입 (상태: 처리전)
INSERT INTO PRODUCT_ORDER_LOG (PRODUCT_ORDER_LOG_ID, PRODUCT_ORDER_ID, STATUS_ID, PRODUCT_ORDER_DATE)
VALUES
    -- 1. 비빔밥 발주 (상태: 처리전)
    (UUID(),
     (SELECT PRODUCT_ORDER_ID
      FROM PRODUCT_ORDER
      WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '쌀')
        AND KIT_ORDER_ID = (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '비빔밥'))
        AND PRODUCT_COMPANY_ID = (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '농협')
        AND QUANTITY = 100
        AND PRODUCT_ORDER_DATE = '2024-08-15 00:00:00'),
     (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1),
    '2024-08-15 00:00:00'),

    (UUID(),
     (SELECT PRODUCT_ORDER_ID
      FROM PRODUCT_ORDER
      WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '김치')
        AND KIT_ORDER_ID = (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '비빔밥'))
        AND PRODUCT_COMPANY_ID = (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '농협')
        AND QUANTITY = 100
        AND PRODUCT_ORDER_DATE = '2024-08-15 00:00:00'),
     (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1),
     '2024-08-15 00:00:00'),

    (UUID(),
     (SELECT PRODUCT_ORDER_ID
      FROM PRODUCT_ORDER
      WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '된장')
        AND KIT_ORDER_ID = (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '비빔밥'))
        AND PRODUCT_COMPANY_ID = (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '농협')
        AND QUANTITY = 100
        AND PRODUCT_ORDER_DATE = '2024-08-15 00:00:00'),
     (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1),
     '2024-08-15 00:00:00'),

    -- 2. 된장찌개 발주 (상태: 처리전)
    (UUID(),
     (SELECT PRODUCT_ORDER_ID
      FROM PRODUCT_ORDER
      WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '된장')
        AND KIT_ORDER_ID = (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '된장찌개'))
        AND PRODUCT_COMPANY_ID = (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '한림')
        AND QUANTITY = 200
        AND PRODUCT_ORDER_DATE = '2024-08-16 00:00:00'),
     (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1),
     '2024-08-16 00:00:00'),

    (UUID(),
     (SELECT PRODUCT_ORDER_ID
      FROM PRODUCT_ORDER
      WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '두부')
        AND KIT_ORDER_ID = (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '된장찌개'))
        AND PRODUCT_COMPANY_ID = (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '한림')
        AND QUANTITY = 200
        AND PRODUCT_ORDER_DATE = '2024-08-16 00:00:00'),
     (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1),
     '2024-08-16 00:00:00'),

    (UUID(),
     (SELECT PRODUCT_ORDER_ID
      FROM PRODUCT_ORDER
      WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '소고기')
        AND KIT_ORDER_ID = (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '된장찌개'))
        AND PRODUCT_COMPANY_ID = (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '한림')
        AND QUANTITY = 200
        AND PRODUCT_ORDER_DATE = '2024-08-16 00:00:00'),
     (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1),
     '2024-08-16 00:00:00'),

    -- 3. 김치찌개 발주 (상태: 처리전)
    (UUID(),
     (SELECT PRODUCT_ORDER_ID
      FROM PRODUCT_ORDER
      WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '김치')
        AND KIT_ORDER_ID = (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '김치찌개'))
        AND PRODUCT_COMPANY_ID = (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '동서식품')
        AND QUANTITY = 100
        AND PRODUCT_ORDER_DATE = '2024-08-17 00:00:00'),
     (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1),
     '2024-08-17 00:00:00'),

    (UUID(),
     (SELECT PRODUCT_ORDER_ID
      FROM PRODUCT_ORDER
      WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '두부')
        AND KIT_ORDER_ID = (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '김치찌개'))
        AND PRODUCT_COMPANY_ID = (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '동서식품')
        AND QUANTITY = 100
        AND PRODUCT_ORDER_DATE = '2024-08-17 00:00:00'),
     (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1),
     '2024-08-17 00:00:00'),

    (UUID(),
     (SELECT PRODUCT_ORDER_ID
      FROM PRODUCT_ORDER
      WHERE SOURCE_ID = (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '소고기')
        AND KIT_ORDER_ID = (SELECT KIT_ORDER_ID FROM KIT_ORDER WHERE MEALKIT_ID = (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '김치찌개'))
        AND PRODUCT_COMPANY_ID = (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '동서식품')
        AND QUANTITY = 100
        AND PRODUCT_ORDER_DATE = '2024-08-17 00:00:00'),
     (SELECT STATUS_ID FROM STATUS WHERE STATUS = '처리전' LIMIT 1),
     '2024-08-17 00:00:00');


-- 12. 권한 테이블 데이터 삽입
INSERT INTO ROLE (ROLE_ID, ROLE_NAME)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_LOGISTICS_MANAGER'),
       (3, 'ROLE_SALES_MANAGER'),
       (4, 'ROLE_PRODUCT_MANAGER');
-- 13 판매업체 창고 데이터 삽입
INSERT INTO KIT_STORAGE (KIT_STORAGE_ID, KIT_COMPANY_ID, MEALKIT_ID, QUANTITY)
VALUES
    -- 비빔밥 밀키트 재고
    (UUID(), (SELECT KIT_COMPANY_ID FROM KIT_COMPANY WHERE NAME = '한식명가'),
     (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '비빔밥'), 100),

    -- 된장찌개 밀키트 재고
    (UUID(), (SELECT KIT_COMPANY_ID FROM KIT_COMPANY WHERE NAME = '정통한식'),
     (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '된장찌개'), 100),

    -- 김치찌개 밀키트 재고
    (UUID(), (SELECT KIT_COMPANY_ID FROM KIT_COMPANY WHERE NAME = '맛있는집'),
     (SELECT MEALKIT_ID FROM MEALKIT WHERE NAME = '김치찌개'), 100);
-- 14 유통저장공간 ID (예: 중앙 물류 창고)
INSERT INTO LOGISTICS_WAREHOUSE (LOGISTICS_WAREHOUSE_ID, WAREHOUSE_NAME)
VALUES (UUID(), '중앙 물류 창고');

-- 15 재료들을 중앙 물류 창고에 1000개씩 추가
INSERT INTO LOGISTICS_WAREHOUSE_STACK (LOGISTICS_WAREHOUSE_STACK_ID, SOURCE_ID, LOGISTICS_WAREHOUSE_ID, QUANTITY, created_at)
VALUES
    (UUID(), (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '쌀'), (SELECT LOGISTICS_WAREHOUSE_ID FROM LOGISTICS_WAREHOUSE WHERE WAREHOUSE_NAME = '중앙 물류 창고'), 1000, NOW()),
    (UUID(), (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '김치'), (SELECT LOGISTICS_WAREHOUSE_ID FROM LOGISTICS_WAREHOUSE WHERE WAREHOUSE_NAME = '중앙 물류 창고'), 1000, NOW()),
    (UUID(), (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '된장'), (SELECT LOGISTICS_WAREHOUSE_ID FROM LOGISTICS_WAREHOUSE WHERE WAREHOUSE_NAME = '중앙 물류 창고'), 1000, NOW()),
    (UUID(), (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '두부'), (SELECT LOGISTICS_WAREHOUSE_ID FROM LOGISTICS_WAREHOUSE WHERE WAREHOUSE_NAME = '중앙 물류 창고'), 1000, NOW()),
    (UUID(), (SELECT SOURCE_ID FROM SOURCE WHERE NAME = '소고기'), (SELECT LOGISTICS_WAREHOUSE_ID FROM LOGISTICS_WAREHOUSE WHERE WAREHOUSE_NAME = '중앙 물류 창고'), 1000, NOW());

       -- 유저 생성
INSERT INTO USER (USER_PK, USER_NAME, USER_ID, USER_PASSWORD, USER_EMAIL, USER_TEL, ROLE_NAME)
VALUES (UUID(), '한림직원01', 'han01', '$2a$12$tgn0.b1tPCvD2NSj3QlyCOHQXLFlqkS1c4motKSgs5mPk0qszn4sC', 'han01@email.com', '010-1144-5511', 'ROLE_PRODUCT_MANAGER'),
       (UUID(), '한림직원02', 'han02', '$2a$12$tgn0.b1tPCvD2NSj3QlyCOHQXLFlqkS1c4motKSgs5mPk0qszn4sC', 'han02@email.com', '010-1143-5512', 'ROLE_PRODUCT_MANAGER'),
       (UUID(), '관리자2', 'admin2', '$2a$12$tgn0.b1tPCvD2NSj3QlyCOHQXLFlqkS1c4motKSgs5mPk0qszn4sC', 'admin2@email.com', '010-0101-0002', 'ROLE_ADMIN');

-- 한림 회사의 PRODUCT_COMPANY_ID와 유저의 USER_PK를 가져와서 PRODUCT_COMPANY_MEMBER에 삽입
INSERT INTO PRODUCT_COMPANY_MEMBER (PRODUCT_COMPANY_MEMBER_ID, USER_ID, PRODUCT_COMPANY_ID)
VALUES (UUID(), 
       (SELECT USER_PK FROM USER WHERE USER_ID = 'han01'), 
       (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '한림')),
       (UUID(), 
       (SELECT USER_PK FROM USER WHERE USER_ID = 'han02'), 
       (SELECT PRODUCT_COMPANY_ID FROM PRODUCT_COMPANY WHERE NAME = '한림'));