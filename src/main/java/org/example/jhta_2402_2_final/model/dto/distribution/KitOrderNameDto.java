package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.Data;

import java.util.UUID;

@Data
public class KitOrderNameDto {
    private UUID kitOrderId;
    private String companyId;
    private String mealkitName;
    private int price;
    private int quantity;
    private int totalPrice;
    private String orderDate;
    private String status;
}
