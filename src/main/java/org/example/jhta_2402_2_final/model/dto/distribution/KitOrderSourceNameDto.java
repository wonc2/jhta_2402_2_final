package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.Data;

import java.util.UUID;

@Data
public class KitOrderSourceNameDto {
    private UUID kitOrderId;
    private String mealkitName;
    private UUID sourceId;
    private String sourceName;
    private int Quantity;

}
