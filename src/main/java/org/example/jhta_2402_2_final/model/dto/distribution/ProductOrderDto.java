package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;


@Data
public class ProductOrderDto {
    private UUID productOrderId;
    private UUID sourcePriceId;
    private int quantity;
    private Date productOrderDate;
    private int statusId;
}
