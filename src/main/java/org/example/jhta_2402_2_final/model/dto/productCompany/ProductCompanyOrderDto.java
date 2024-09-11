package org.example.jhta_2402_2_final.model.dto.productCompany;

import lombok.Getter;

// OrderTable 리스트 뿌려주는 용
@Getter
public class ProductCompanyOrderDto {
    private String orderId;
    private String sourcePriceId;
    private int quantity;
    private int totalPrice;
    private int rowNum;
    private String orderStatus;
    private int stockBalance;
    private String sourceName;
    private String orderDate; // @@: String -> Date
    private int sourcePrice;
}
