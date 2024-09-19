package org.example.jhta_2402_2_final.model.dto.productCompany;

import lombok.Builder;
import lombok.Getter;

// 주문 처리 dto
@Getter
@Builder(toBuilder = true)
public class ProductCompanyOrderProcessDto {
    private String companyId;
    private String orderId;
    private Integer orderStatus;
    private int sourceQuantity;
    private String sourcePriceId;
    private int stockBalance;
}
