package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;


@Data
public class ProductOrderLogDto {
    private UUID productOrderLogId;
    private UUID productOrderId;
    private int statusId;
    private Date productOrderDate;

}
