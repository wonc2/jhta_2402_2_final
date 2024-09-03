package org.example.jhta_2402_2_final.model.dto.sales;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class KitOrderLogDto {
    private UUID kitOrderLogId;
    private String kitOrderId;
    private String companyName;
    private String mealkitName;
    private String status;
    private String productOrderDate;
}
