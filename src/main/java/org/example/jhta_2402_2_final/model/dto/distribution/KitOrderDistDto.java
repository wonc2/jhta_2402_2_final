package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.Data;

import java.util.UUID;

@Data
public class KitOrderDistDto {
    private UUID kitOrderId;
    private UUID kitCompanyId;
    private UUID mealkitId;
    private int price;
    private int quantity;
    private String orderDate;
    private int statusId;
}
