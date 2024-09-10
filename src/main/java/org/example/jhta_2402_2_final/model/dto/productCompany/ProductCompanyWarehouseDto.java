package org.example.jhta_2402_2_final.model.dto.productCompany;

import lombok.Getter;

import java.util.Date;

// Warehouse 테이블 리스트 뿌려주는 용
@Getter
public class ProductCompanyWarehouseDto {
    private String sourceWarehouseId;
    private String sourceName;
    private String type; // 입고 출고
    private String produceDate; // @@: String -> Date
    private int sourceQuantity;
    private int rowNum;
}
