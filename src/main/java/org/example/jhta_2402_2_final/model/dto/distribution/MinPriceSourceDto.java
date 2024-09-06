package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.Data;

import java.util.UUID;
@Data
public class MinPriceSourceDto {
    private UUID kitOrderId; // 주문 ID
    private String mealkitName; // 밀키트 이름
    private int mealkitQuantity; // 밀키트 주문 수량
    private UUID sourceId; // 재료 ID
    private String sourceName; // 재료 이름
    private int ingredientQuantity; // 재료 수량
    private int sourcePrice; // 재료의 최소 가격
    private String productCompanyName; // 판매업체 이름
    private UUID productCompanyId; // 판매업체 이름
    private int totalQuantity; // 총 주문해야 하는 재료 개수


}
