package org.example.jhta_2402_2_final.model.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ProductOrderViewDto {
    private int rowNum;
    private String productCompanyName;
    private String sourceName;
    private int sourcePrice;
    private int quantity;
    private int totalPrice;
    private String productOrderDate;
    private String status;
}
