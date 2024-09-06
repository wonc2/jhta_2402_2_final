package org.example.jhta_2402_2_final.model.dto.sales;

import lombok.Data;

import java.util.UUID;

@Data
public class ProductOrderLogDetailDto {
    private UUID productOrderLogId;
    private UUID productOrderId;
    private String sourceName;
    private String companyName;
    private int quantity;
    private int price;
    private String orderDate;
    private String status;
}
