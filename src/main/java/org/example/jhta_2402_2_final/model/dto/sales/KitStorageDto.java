package org.example.jhta_2402_2_final.model.dto.sales;

import lombok.Data;

import java.util.UUID;

@Data
public class KitStorageDto {
    private UUID kitStorageId;
    private String companyName;
    private String mealkitName;
    private int quantity;

}
