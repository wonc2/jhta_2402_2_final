package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.Data;

import java.util.UUID;

@Data
public class MinPriceOrderDto {
    private UUID kitOrderId;
    private String orderDate;
    private String mealkitName;
    private int mealkitQuantity;
    private int rawCost;
    private int totalCost;
    private UUID sourceId;
    private String sourceName;
    private int warehouseQuantity;
    private String productCompanyName;
    private String ingredientDetails;

}
