package org.example.jhta_2402_2_final.model.dto.sales;

import lombok.Data;

import java.util.UUID;

@Data
public class SourcePriceDto {
    private UUID sourcePriceId;
    private String companyName;
    private String sourceName;
    private int price;

}
