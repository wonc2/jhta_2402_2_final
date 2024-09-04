package org.example.jhta_2402_2_final.model.dto.sales;

import lombok.Data;

import java.util.UUID;

@Data
public class KitPriceDto {
    private UUID mealkitId;
    private String mealkitName;
    private String sourceName;
    private int minPrice;
    private int quantity;
    private int total;
    private int minMealkitPrice;
    private int currentMealkitPrice;
}
