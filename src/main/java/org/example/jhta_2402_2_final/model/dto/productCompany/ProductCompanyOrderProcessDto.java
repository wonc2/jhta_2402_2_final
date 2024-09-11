package org.example.jhta_2402_2_final.model.dto.productCompany;

import lombok.Getter;

// 주문 처리 dto
@Getter
public class ProductCompanyOrderProcessDto {
    private String orderId;
    private Integer orderStatus;
    private int sourceQuantity;
    private String sourcePriceId;
    private int stockBalance;
}
