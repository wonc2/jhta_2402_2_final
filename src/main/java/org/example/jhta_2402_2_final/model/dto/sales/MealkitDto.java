package org.example.jhta_2402_2_final.model.dto.sales;

import lombok.Data;

import java.util.UUID;

@Data
public class MealkitDto {
    private UUID mealkitId;
    private String name;
    private int price;
}
