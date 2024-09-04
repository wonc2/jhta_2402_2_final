package org.example.jhta_2402_2_final.model.dto.sales;

import lombok.Data;
import java.util.UUID;

@Data
public class KitCompletedDto {
    private UUID kitOrderLogId;
    private UUID kitOrderId;
    private String companyName;
    private String mealkitName;
    private int mealkitPrice;
    private int quantity;
    private int total;
    private String productOrderDate;
}
