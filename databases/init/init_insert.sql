-- init insert

-- 역할
INSERT INTO `ROLE` (`role_uid`, `role_name`)
VALUES (UUID(), 'ROLE_ADMIN'),
       (UUID(), 'ROLE_WAREHOUSE_MANAGER'),
       (UUID(), 'ROLE_STORE_MANAGER'),
       (UUID(), 'ROLE_ACCOUNT_MANAGER'),
       (UUID(), 'ROLE_SALES_MANAGER'),
       (UUID(), 'ROLE_HR_MANAGER'),
       (UUID(), 'ROLE_ORDER_MANAGER');

-- 유저 추가
INSERT INTO `USER` (`user_uid`, `user_name`, `user_id`, `user_pw`, `user_email`, `user_tel`)
VALUES (UUID(), '관리자', 'admin', '$2b$12$G.FBR8kI/A9ekBqe.mcfmuzuyUXNjgysEBiEJeIJZXVMLeqF7xa3a', 'admin@example.com',
        '010-0000-0000');

-- 다음으로 ACCOUNT 테이블에 USER와 ROLE을 연결합니다.
INSERT INTO `ACCOUNT` (`account_uid`, `role_uid`, `user_uid`)
VALUES (UUID(),
        (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_ADMIN'),
        (SELECT `user_uid` FROM `USER` WHERE `user_id` = 'admin'));


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
-- 수산물
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '수산물'), '참치', 2, 25000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '수산물'), '연어', 1, 20000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '수산물'), '굴비', 3, 15000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '수산물'), '오징어', 1, 7000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '수산물'), '홍합', 1, 5000),
-- 축산물
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '축산물'), '목살', 2, 13000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '축산물'), '갈비살', 3, 16000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '축산물'), '불고기용 소고기', 1, 14000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '축산물'), '오리 훈제', 1, 12000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '축산물'), '베이컨', 1, 8000),
-- 가공식품
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '가공식품'), '즉석 카레', 1, 2000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '가공식품'), '즉석 미역국', 1, 2500),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '가공식품'), '식빵', 3, 3000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '가공식품'), '우유', 2, 2000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '가공식품'), '치즈', 1, 5000),
-- 생필품
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '생필품'), '면도기', 1, 15000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '생필품'), '치약', 1, 3000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '생필품'), '휴대용 손소독제', 1, 2000),
(UUID(), (SELECT `category_uid` FROM `CATEGORY` WHERE `category_name` = '생필품'), '비닐봉지', 1, 1000);


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

INSERT INTO `STORE` (`store_uid`, `role_uid`, `store_name`, `store_address`)
VALUES (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_STORE_MANAGER'), '서울 강남점',
        '서울특별시 강남구 강남대로 396'),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_STORE_MANAGER'), '서울 잠실점',
        '서울특별시 송파구 올림픽로 240'),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_STORE_MANAGER'), '부산 해운대점',
        '부산광역시 해운대구 해운대로 620'),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_STORE_MANAGER'), '인천 송도점',
        '인천광역시 연수구 송도국제대로 123'),
       (UUID(), (SELECT `role_uid` FROM `ROLE` WHERE `role_name` = 'ROLE_STORE_MANAGER'), '대구 동성로점', '대구광역시 중구 동성로 23');

INSERT INTO `TRANSPORT` (`transport_uid`, `transport_type`, `transport_capacity`)
VALUES (UUID(), '1톤 트럭', 1000),
       (UUID(), '5톤 트럭', 5000),
       (UUID(), '10톤 트럭', 10000),
       (UUID(), '25톤 트럭', 25000);


INSERT INTO WAREHOUSE_STATUS (warehouse_status_uid, warehouse_uid, warehouse_amount, warehouse_tx, warehouse_date, product_uid)
VALUES (UUID(), (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = '중구 물류센터'), 500, '입고', '2024-01-15 09:30:00',
        (SELECT product_uid FROM PRODUCT WHERE product_name = '양파')),
       (UUID(), (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = '중구 물류센터'), 300, '입고', '2024-02-20 14:00:00',
        (SELECT product_uid FROM PRODUCT WHERE product_name = '참치')),
       (UUID(), (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = '서울1센터'), 1000, '입고', '2024-03-05 11:15:00',
        (SELECT product_uid FROM PRODUCT WHERE product_name = '목살')),
       (UUID(), (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = '부산 물류센터'), 200, '입고', '2024-04-10 16:45:00',
        (SELECT product_uid FROM PRODUCT WHERE product_name = '즉석 카레'));

INSERT INTO SUPPLIER_STATUS (supplier_status_uid, supplier_uid, supplier_amount, product_uid)
VALUES (UUID(), (SELECT supplier_uid FROM SUPPLIER WHERE supplier_name = '농협'), 1000,
        (SELECT product_uid FROM PRODUCT WHERE product_name = '양파')),
       (UUID(), (SELECT supplier_uid FROM SUPPLIER WHERE supplier_name = '수협'), 2000,
        (SELECT product_uid FROM PRODUCT WHERE product_name = '참치')),
       (UUID(), (SELECT supplier_uid FROM SUPPLIER WHERE supplier_name = '팜한농'), 1500,
        (SELECT product_uid FROM PRODUCT WHERE product_name = '호박'));

INSERT INTO STORE_STATUS (store_status_uid, store_uid, store_amount, store_tx, store_date, product_uid)
VALUES (UUID(), (SELECT store_uid FROM STORE WHERE store_name = '서울 강남점'), 300, '입고', '2024-01-10 08:00:00',
        (SELECT product_uid FROM PRODUCT WHERE product_name = '대파')),
       (UUID(), (SELECT store_uid FROM STORE WHERE store_name = '서울 잠실점'), 150, '입고', '2024-02-15 13:00:00',
        (SELECT product_uid FROM PRODUCT WHERE product_name = '오징어')),
       (UUID(), (SELECT store_uid FROM STORE WHERE store_name = '부산 해운대점'), 200, '입고', '2024-03-20 10:30:00',
        (SELECT product_uid FROM PRODUCT WHERE product_name = '치즈'));

INSERT INTO ORDER_TABLE (order_uid, order_status, order_by_tpye, order_by_uid, supply_by_type, supply_by_uid)
VALUES (UUID(), '요청', 'STORE', (SELECT store_uid FROM STORE WHERE store_name = '서울 강남점'), 'WAREHOUSE',
        (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = '중구 물류센터')),
       (UUID(), '승인', 'STORE', (SELECT store_uid FROM STORE WHERE store_name = '부산 해운대점'), 'WAREHOUSE',
        (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = '서울1센터'));

INSERT INTO ORDER_DETAIL (order_detail_uid, order_date, order_amount, product_uid, order_uid)
VALUES (UUID(), '2024-01-12 10:00:00', 100, (SELECT product_uid FROM PRODUCT WHERE product_name = '대파'),
        (SELECT order_uid FROM ORDER_TABLE WHERE order_by_uid = (SELECT store_uid FROM STORE WHERE store_name = '서울 강남점'))),
       (UUID(), '2024-02-18 12:00:00', 50, (SELECT product_uid FROM PRODUCT WHERE product_name = '오징어'),
        (SELECT order_uid FROM ORDER_TABLE WHERE order_by_uid = (SELECT store_uid FROM STORE WHERE store_name = '부산 해운대점')));

INSERT INTO DISTRIBUTION_LOG (distribution_log_uid, product_uid, source_type, source_uid, detination_type, destination_uid, amount, log_date, transport_uid)
VALUES (UUID(), (SELECT product_uid FROM PRODUCT WHERE product_name = '양파'), 'SUPPLIER',
        (SELECT supplier_uid FROM SUPPLIER WHERE supplier_name = '농협'), 'WAREHOUSE',
        (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = '중구 물류센터'), 500, '2024-01-14 09:00:00',
        (SELECT transport_uid FROM TRANSPORT WHERE transport_type = '1톤 트럭')),
       (UUID(), (SELECT product_uid FROM PRODUCT WHERE product_name = '참치'), 'SUPPLIER',
        (SELECT supplier_uid FROM SUPPLIER WHERE supplier_name = '수협'), 'WAREHOUSE',
        (SELECT warehouse_uid FROM WAREHOUSE WHERE warehouse_name = '서울1센터'), 200, '2024-02-22 14:30:00',
        (SELECT transport_uid FROM TRANSPORT WHERE transport_type = '5톤 트럭'));
