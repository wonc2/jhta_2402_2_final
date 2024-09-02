-- 1. 밀키트 테이블 데이터 삽입
INSERT INTO MEALKIT (MEALKIT_ID, NAME, PRICE) VALUES
                                                  ('mealkit-001', '비빔밥', 15000),
                                                  ('mealkit-002', '된장찌개', 12000),
                                                  ('mealkit-003', '김치찌개', 13000);

-- 2. 밀키트 판매 업체 테이블 데이터 삽입
INSERT INTO KIT_COMPANY (KIT_COMPANY_ID, NAME, ADDRESS) VALUES
                                                            ('company-001', '한식명가', '서울시 강남구'),
                                                            ('company-002', '정통한식', '서울시 종로구'),
                                                            ('company-003', '맛있는집', '서울시 송파구');

-- 3. 생산업체 테이블 데이터 삽입
INSERT INTO PRODUCT_COMPANY (PRODUCT_COMPANY_ID, NAME, ADDRESS) VALUES
                                                                    ('product-001', '농협', '서울시 중구'),
                                                                    ('product-002', '한림', '서울시 서초구'),
                                                                    ('product-003', '동서식품', '서울시 강북구');

-- 4. 재료 테이블 데이터 삽입
INSERT INTO SOURCE (SOURCE_ID, NAME) VALUES
                                         ('source-001', '쌀'),
                                         ('source-002', '김치'),
                                         ('source-003', '된장'),
                                         ('source-004', '두부'),
                                         ('source-005', '소고기');

-- 5. 생산업체별 재료 가격 테이블 데이터 삽입
INSERT INTO SOURCE_PRICE (SOURCE_PRICE_ID, PRODUCT_COMPANY_ID, SOURCE_ID, PRICE) VALUES
                                                                                     ('price-001', 'product-001', 'source-001', 5000),
                                                                                     ('price-002', 'product-001', 'source-002', 3000),
                                                                                     ('price-003', 'product-002', 'source-003', 2000),
                                                                                     ('price-004', 'product-003', 'source-004', 2500),
                                                                                     ('price-005', 'product-003', 'source-005', 10000);

-- 6. 밀키트 재료 테이블 데이터 삽입
INSERT INTO KIT_SOURCE (KIT_SOURCE_ID, MEALKIT_ID, SOURCE_ID, QUANTITY) VALUES
                                                                            ('kit-source-001', 'mealkit-001', 'source-001', 1),
                                                                            ('kit-source-002', 'mealkit-001', 'source-002', 2),
                                                                            ('kit-source-003', 'mealkit-002', 'source-003', 1),
                                                                            ('kit-source-004', 'mealkit-003', 'source-004', 3),
                                                                            ('kit-source-005', 'mealkit-003', 'source-005', 2);

-- 7. 상태 테이블 데이터 삽입
INSERT INTO STATUS (STATUS_ID, STATUS) VALUES
                                           (1, '처리전'),
                                           (2, '처리중'),
                                           (3, '처리완료'),
                                           (4, '취소');

-- 8. 생산주문(발주) 테이블 데이터 삽입
INSERT INTO PRODUCT_ORDER (PRODUCT_ORDER_ID, KIT_SOURCE_ID, QUANTITY, PRODUCT_ORDER_DATE, STATUS_ID) VALUES
                                                                                                         ('order-001', 'kit-source-001', 10, '2024-09-01 10:00:00', 1),
                                                                                                         ('order-002', 'kit-source-003', 5, '2024-09-02 11:00:00', 2);

-- 9. 생산주문(발주) 로그 테이블 데이터 삽입
INSERT INTO PRODUCT_ORDER_LOG (PRODUCT_ORDER_LOG_ID, PRODUCT_ORDER_ID, STATUS_ID, PRODUCT_ORDER_DATE) VALUES
                                                                                                          ('order-log-001', 'order-001', 1, '2024-09-01 10:00:00'),
                                                                                                          ('order-log-002', 'order-002', 2, '2024-09-02 11:00:00');

-- 10. 밀키트 주문 테이블 데이터 삽입
INSERT INTO KIT_ORDER (KIT_ORDER_ID, KIT_COMPANY_ID, MEALKIT_ID, QUANTITY, PRODUCT_ORDER_DATE, STATUS_ID) VALUES
                                                                                                              ('kit-order-001', 'company-001', 'mealkit-001', 20, '2024-09-01 09:00:00', 1),
                                                                                                              ('kit-order-002', 'company-002', 'mealkit-003', 15, '2024-09-02 10:00:00', 2);

-- 11. 밀키트 주문로그 테이블 데이터 삽입
INSERT INTO KIT_ORDER_LOG (KIT_ORDER_LOG_ID, KIT_ORDER_ID, STATUS_ID, PRODUCT_ORDER_DATE) VALUES
                                                                                              ('kit-order-log-001', 'kit-order-001', 1, '2024-09-01 09:00:00'),
                                                                                              ('kit-order-log-002', 'kit-order-002', 2, '2024-09-02 10:00:00');


INSERT INTO ROLE (ROLE_ID, ROLE_NAME)
VALUES (1, 'ROLE_ADMIN'),
       (2, 'ROLE_KIT_MANAGER'),
       (3, 'ROLE_PRODUCT_MANAGER');

INSERT INTO USER (USER_PK, USER_NAME, USER_ID, USER_PASSWORD, USER_EMAIL, USER_TEL, ROLE_ID)
VALUES (UUID(), '김철수', 'chulsoo', 'password123', 'chulsoo@example.com', '010-1234-5678', 1),
       (UUID(), '이영희', 'younghee', 'password456', 'younghee@example.com', '010-2345-6789', 2),
       (UUID(), '박영수', 'youngsoo', 'password789', 'youngsoo@example.com', '010-3456-7890', 3),
       (UUID(), 'Admin', 'admin', '$2a$12$o9M5nC6PUyvUpy2uY71vJuzinkjUjl2YAGASLOnefnPAM/nIvNLN2', 'admin@example.com', '010-0000-1313', 1);
