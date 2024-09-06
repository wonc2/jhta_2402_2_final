package org.example.jhta_2402_2_final.model.dto.sales;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductOrderDetailDto {
    private UUID productOrderId;
    private String companyName;
    private String sourceName;
    private int sourcePrice;
    private int quantity;
    private String orderDate;
    private String status;
}
