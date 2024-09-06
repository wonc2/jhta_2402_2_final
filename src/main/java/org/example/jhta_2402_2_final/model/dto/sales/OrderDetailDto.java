package org.example.jhta_2402_2_final.model.dto.sales;

import lombok.Data;

import java.util.UUID;

@Data
public class OrderDetailDto {
    private UUID kitOrderId;      // 주문 ID
    private String mealkitName;     // 밀키트 이름
    private String sourceName;      // 재료 이름
    private int quantity;       // 필요 수량
    private int stackQuantity;  // 창고 재고 수량
    private int minPrice;       // 최소 가격
    private String companyName;     // 회사명
}
