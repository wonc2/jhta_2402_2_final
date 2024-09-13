package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.Data;

import java.util.UUID;

//kit oder 테이블 확인
@Data
public class KitOrderDistDto {
    private UUID kitOrderId;
    private UUID kitCompanyId;
    private String kitCompanyName;
    private UUID mealkitId;
    private String mealkitName;
    private int price;
    private int quantity;
    private String productOrderDate;
    private int statusId;
    private String status;
}
