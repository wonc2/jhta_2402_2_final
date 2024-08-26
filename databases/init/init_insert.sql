-- init insert

-- 역할
INSERT INTO `ROLE` (`role_uid`, `role_name`)
VALUES (UUID(), 'ROLE_ADMIN'),
       (UUID(), 'ROLE_WAREHOUSE_MANAGER'),

       (UUID(), 'ROLE_ACCOUNT_MANAGER'),
       (UUID(), 'ROLE_SALES_MANAGER'),
       (UUID(), 'ROLE_HR_MANAGER'),
       (UUID(), 'ROLE_ORDER_MANAGER');

-- 유저 추가
INSERT INTO `USER` (`user_uid`, `user_name`, `user_id`, `user_pw`, `user_email`, `user_tel`)
VALUES (UUID(), 'Admin', 'admin', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'admin@example.com','010-0000-0000'),
       (UUID(), 'John Doe', 'john1', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'john1@example.com', '010-0000-0001'),
       (UUID(), 'Jane Doe', 'jane2', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'jane2@example.com', '010-0000-0002'),
       (UUID(), 'Michael Scott', 'michael3', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'michael3@example.com', '010-0000-0003'),
       (UUID(), 'Dwight Schrute', 'dwight4', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'dwight4@example.com', '010-0000-0004'),
       (UUID(), 'Jim Halpert', 'jim5', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'jim5@example.com', '010-0000-0005'),
       (UUID(), 'Pam Beesly', 'pam6', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'pam6@example.com', '010-0000-0006'),
       (UUID(), 'Stanley Hudson', 'stanley7', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'stanley7@example.com', '010-0000-0007'),
       (UUID(), 'Kevin Malone', 'kevin8', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'kevin8@example.com', '010-0000-0008'),
       (UUID(), 'Oscar Martinez', 'oscar9', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'oscar9@example.com', '010-0000-0009'),
       (UUID(), 'Toby Flenderson', 'toby10', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'toby10@example.com', '010-0000-0010'),
       (UUID(), 'Meredith Palmer', 'meredith11', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'meredith11@example.com', '010-0000-0011'),
       (UUID(), 'Ryan Howard', 'ryan12', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'ryan12@example.com', '010-0000-0012'),
       (UUID(), 'Kelly Kapoor', 'kelly13', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'kelly13@example.com', '010-0000-0013'),
       (UUID(), 'Andy Bernard', 'andy14', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'andy14@example.com', '010-0000-0014'),
       (UUID(), 'Angela Martin', 'angela15', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'angela15@example.com', '010-0000-0015');


-- USER_ROLE 테이블에 USER와 ROLE을 연결
INSERT INTO `USER_ROLE` (`user_role_uid`, `role_uid`, `user_uid`)
VALUES (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_ADMIN'), (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'admin')),

       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'jane2')),

       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_ACCOUNT_MANAGER'), (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'dwight4')),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_SALES_MANAGER'), (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'jim5')),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_HR_MANAGER'), (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'pam6')),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_ORDER_MANAGER'), (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'stanley7')),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'kevin8')),

       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_ACCOUNT_MANAGER'), (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'toby10')),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_SALES_MANAGER'), (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'meredith11')),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_HR_MANAGER'), (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'ryan12')),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_ORDER_MANAGER'), (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'kelly13')),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_SALES_MANAGER'), (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'andy14')),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_HR_MANAGER'), (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'angela15'));


-- 카테고리
INSERT INTO `CATEGORY` (`category_uid`, `category_name`)
VALUES (UUID(), '농산물'),
       (UUID(), '수산물'),
       (UUID(), '축산물'),
       (UUID(), '가공식품'),
       (UUID(), '생필품');

INSERT INTO `PRODUCT` (`product_uid`, `category_uid`, `product_name`, `product_size`, `product_cost`)
VALUES
-- 농산물
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '농산물'), '양파', 3, 3000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '농산물'), '호박', 1, 2500),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '농산물'), '대파', 2, 2000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '농산물'), '마늘', 1, 3500),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '농산물'), '오이', 1, 1500),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '농산물'), '상추', 1, 1000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '농산물'), '배추', 2, 2500),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '농산물'), '당근', 1, 2000),
-- 수산물
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '수산물'), '참치', 2, 25000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '수산물'), '연어', 1, 20000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '수산물'), '굴비', 3, 15000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '수산물'), '오징어', 1, 7000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '수산물'), '홍합', 1, 5000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '수산물'), '새우', 1, 10000),

-- 축산물
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '축산물'), '목살', 2, 13000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '축산물'), '갈비살', 3, 16000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '축산물'), '불고기용 소고기', 1, 14000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '축산물'), '오리 훈제', 1, 12000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '축산물'), '베이컨', 1, 8000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '축산물'), '닭가슴살', 2, 12000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '축산물'), '양고기', 1, 20000),

-- 가공식품
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '가공식품'), '즉석 카레', 1, 2000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '가공식품'), '즉석 미역국', 1, 2500),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '가공식품'), '식빵', 3, 3000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '가공식품'), '우유', 2, 2000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '가공식품'), '치즈', 1, 5000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '가공식품'), '통조림', 1, 1500),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '가공식품'), '라면', 5, 5000),
-- 생필품
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '생필품'), '면도기', 1, 15000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '생필품'), '치약', 1, 3000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '생필품'), '휴대용 손소독제', 1, 2000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '생필품'), '비닐봉지', 1, 1000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '생필품'), '세제', 2, 4000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '생필품'), '스폰지', 1, 1000);


INSERT INTO `WAREHOUSE` (`warehouse_uid`, `role_uid`, `warehouse_name`, `warehouse_address`, `warehouse_capacity`)
VALUES (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '중구 물류센터',
        '서울특별시 중구 의주로1가 29-1', 100000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), 'MEGA 물류센터',
        '인천광역시 서구 갑문4로 25', 200000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '부천 물류센터',
        '경기도 부천시 신흥로511번길 80', 150000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '인천1센터',
        '인천광역시 중구 축항대로165번길 20', 250000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '인천2센터',
        '인천광역시 서구 갑문3로 34', 180000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '제주 1센터',
        '제주특별자치도 제주시 한북로 112', 100000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '부산 물류센터',
        '부산광역시 금정구 공단동로41번길 21', 220000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '고양 물류센터',
        '경기도 고양시 덕양구 권율대로 570', 170000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '서울1센터',
        '서울 송파구 송파대로 55 서울복합물류 E동 쿠팡 서울1센터', 300000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '인천3센터',
        '인천광역시 서구 오류동 1545-2', 120000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '안양1센터',
        '경기도 안양시 동안구 흥안대로439번길 30', 140000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '곤지암 2센터',
        '경기도 광주시 곤지암읍 신대길 134-14', 160000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '광주 1센터',
        '광주광역시 서구 매월1로63번길 16', 180000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '평택 1센터',
        '경기도 평택시 포승읍 하만호길 153', 220000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '시흥 1센터',
        '경기도 시흥시 정왕동 2123-3', 140000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '창원 센터',
        '경상남도 창원시 진해구 두동남로 52', 170000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '부산 2센터',
        '부산광역시 기장군 정관면 달산리 1039-4', 200000),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_WAREHOUSE_MANAGER'), '천안 센터',
        '충청남도 천안시 서북구 입장면 용정리2길 22', 210000);

INSERT INTO `SUPPLIER` (`supplier_uid`, `supplier_name`, `supplier_address`)
VALUES (UUID(), '농협', '서울특별시 중구 충정로 1'),
       (UUID(), '수협', '서울특별시 송파구 오금로 55'),
       (UUID(), '팜한농', '경기도 성남시 중원구 둔촌대로 245'),
       (UUID(), '동원산업', '서울특별시 서초구 서초대로 511'),
       (UUID(), '롯데제과', '서울특별시 영등포구 양평로 21');


INSERT INTO `TRANSPORT` (`transport_uid`, `transport_type`, `transport_capacity`)
VALUES (UUID(), '1톤 트럭', 1000),
       (UUID(), '5톤 트럭', 5000),
       (UUID(), '10톤 트럭', 10000),
       (UUID(), '25톤 트럭', 25000);


INSERT INTO WAREHOUSE_STATUS (warehouse_status_uid, warehouse_uid, warehouse_amount, warehouse_tx, warehouse_date, product_uid)
VALUES
-- 중구 물류센터
(UUID(), (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = '중구 물류센터'), 200, '출고', '2024-08-01 10:00:00', (SELECT product_uid FROM PRODUCT WHERE product_name = '양파')),
(UUID(), (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = '중구 물류센터'), 300, '입고', '2024-08-02 14:00:00', (SELECT product_uid FROM PRODUCT WHERE product_name = '오징어')),
-- MEGA 물류센터
(UUID(), (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = 'MEGA 물류센터'), 150, '입고', '2024-08-03 11:00:00', (SELECT product_uid FROM PRODUCT WHERE product_name = '참치')),
(UUID(), (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = 'MEGA 물류센터'), 100, '출고', '2024-08-04 09:00:00', (SELECT product_uid FROM PRODUCT WHERE product_name = '베이컨')),
-- 부천 물류센터
(UUID(), (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = '부천 물류센터'), 250, '입고', '2024-08-05 13:00:00', (SELECT product_uid FROM PRODUCT WHERE product_name = '닭가슴살')),
(UUID(), (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = '부천 물류센터'), 180, '출고', '2024-08-06 15:00:00', (SELECT product_uid FROM PRODUCT WHERE product_name = '치즈')),
-- 인천1센터
(UUID(), (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = '인천1센터'), 300, '입고', '2024-08-07 16:00:00', (SELECT product_uid FROM PRODUCT WHERE product_name = '굴비')),
(UUID(), (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = '인천1센터'), 220, '출고', '2024-08-08 12:00:00', (SELECT product_uid FROM PRODUCT WHERE product_name = '우유'));

INSERT INTO `SUPPLIER_STATUS` (`supplier_status_uid`, `supplier_uid`, `supplier_amount`, `product_uid`)
VALUES
-- 농협
(UUID(), (SELECT `supplier_uid` FROM `SUPPLIER` WHERE `supplier_name` = '농협'), 500, (SELECT `product_uid` FROM `PRODUCT` WHERE `product_name` = '양파')),
(UUID(), (SELECT `supplier_uid` FROM `SUPPLIER` WHERE `supplier_name` = '농협'), 300, (SELECT `product_uid` FROM `PRODUCT` WHERE `product_name` = '당근')),
-- 수협
(UUID(), (SELECT `supplier_uid` FROM `SUPPLIER` WHERE `supplier_name` = '수협'), 400, (SELECT `product_uid` FROM `PRODUCT` WHERE `product_name` = '오징어')),
(UUID(), (SELECT `supplier_uid` FROM `SUPPLIER` WHERE `supplier_name` = '수협'), 250, (SELECT `product_uid` FROM `PRODUCT` WHERE `product_name` = '새우')),
-- 동원산업
(UUID(), (SELECT `supplier_uid` FROM `SUPPLIER` WHERE `supplier_name` = '동원산업'), 600, (SELECT `product_uid` FROM `PRODUCT` WHERE `product_name` = '참치')),
(UUID(), (SELECT `supplier_uid` FROM `SUPPLIER` WHERE `supplier_name` = '동원산업'), 500, (SELECT `product_uid` FROM `PRODUCT` WHERE `product_name` = '굴비')),
-- 롯데제과
(UUID(), (SELECT `supplier_uid` FROM `SUPPLIER` WHERE `supplier_name` = '롯데제과'), 200, (SELECT `product_uid` FROM `PRODUCT` WHERE `product_name` = '식빵')),
(UUID(), (SELECT `supplier_uid` FROM `SUPPLIER` WHERE `supplier_name` = '롯데제과'), 150, (SELECT `product_uid` FROM `PRODUCT` WHERE `product_name` = '치즈'));

INSERT INTO `ORDER_TABLE` (`order_uid`, `order_status`, `order_by_tpye`, `order_by_uid`, `supply_by_type`, `supply_by_uid`)
VALUES
    (UUID(), '요청', 'SUPPLY', (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'admin'), 'WAREHOUSE', (SELECT `supplier_uid` FROM `SUPPLIER` WHERE `supplier_name` = '농협')),
    (UUID(), '승인', 'SUPPLY', (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'john1'), 'WAREHOUSE', (SELECT `supplier_uid` FROM `SUPPLIER` WHERE `supplier_name` = '수협'));

INSERT INTO `ORDER_DETAIL` (`order_detail_uid`, `order_date`, `order_amount`, `product_uid`, `order_uid`)
VALUES
    (UUID(), '2024-08-09 10:00:00', 100, (SELECT `product_uid` FROM `PRODUCT` WHERE `product_name` = '양파'), (SELECT `order_uid` FROM `ORDER_TABLE` WHERE `order_status` = '요청')),
    (UUID(), '2024-08-10 11:00:00', 50, (SELECT `product_uid` FROM `PRODUCT` WHERE `product_name` = '새우'), (SELECT `order_uid` FROM `ORDER_TABLE` WHERE `order_status` = '승인'));

INSERT INTO `DISTRIBUTION_LOG` (`distribution_log_uid`, `product_uid`, `source_type`, `source_uid`, `destination_type`, `destination_uid`, `amount`, `log_date`, `transport_uid`)
VALUES
-- 중구 물류센터 -> MEGA 물류센터
(UUID(), (SELECT `product_uid` FROM `PRODUCT` WHERE `product_name` = '양파'), 'WAREHOUSE', (SELECT `warehouse_uid` FROM `WAREHOUSE` WHERE `warehouse_name` = '중구 물류센터'), 'WAREHOUSE', (SELECT `warehouse_uid` FROM `WAREHOUSE` WHERE `warehouse_name` = 'MEGA 물류센터'), 100, '2024-08-11 12:00:00', (SELECT `transport_uid` FROM `TRANSPORT` WHERE `transport_type` = '5톤 트럭')),
-- MEGA 물류센터 -> 부천 물류센터
(UUID(), (SELECT `product_uid` FROM `PRODUCT` WHERE `product_name` = '새우'), 'WAREHOUSE', (SELECT `warehouse_uid` FROM `WAREHOUSE` WHERE `warehouse_name` = 'MEGA 물류센터'), 'WAREHOUSE', (SELECT `warehouse_uid` FROM `WAREHOUSE` WHERE `warehouse_name` = '부천 물류센터'), 50, '2024-08-12 14:00:00', (SELECT `transport_uid` FROM `TRANSPORT` WHERE `transport_type` = '1톤 트럭')),
-- 부천 물류센터 -> 인천1센터
(UUID(), (SELECT `product_uid` FROM `PRODUCT` WHERE `product_name` = '참치'), 'WAREHOUSE', (SELECT `warehouse_uid` FROM `WAREHOUSE` WHERE `warehouse_name` = '부천 물류센터'), 'WAREHOUSE', (SELECT `warehouse_uid` FROM `WAREHOUSE` WHERE `warehouse_name` = '인천1센터'), 75, '2024-08-13 09:00:00', (SELECT `transport_uid` FROM `TRANSPORT` WHERE `transport_type` = '25톤 트럭')),
-- 인천1센터 -> 중구 물류센터
(UUID(), (SELECT `product_uid` FROM `PRODUCT` WHERE `product_name` = '치즈'), 'WAREHOUSE', (SELECT `warehouse_uid` FROM `WAREHOUSE` WHERE `warehouse_name` = '인천1센터'), 'WAREHOUSE', (SELECT `warehouse_uid` FROM `WAREHOUSE` WHERE `warehouse_name` = '중구 물류센터'), 30, '2024-08-14 15:00:00', (SELECT `transport_uid` FROM `TRANSPORT` WHERE `transport_type` = '1톤 트럭'));
