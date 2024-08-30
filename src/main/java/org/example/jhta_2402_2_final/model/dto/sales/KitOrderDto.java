package org.example.jhta_2402_2_final.model.dto.sales;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class KitOrderDto {

    private UUID kitOrderId;
    private String kitCompanyId;
    private String mealkitId;
    private int quantity;
    private LocalDate productOrderDate;
    private int statusId;
}
