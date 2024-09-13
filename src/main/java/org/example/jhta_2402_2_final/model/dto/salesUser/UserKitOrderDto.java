package org.example.jhta_2402_2_final.model.dto.salesUser;

import lombok.Data;

@Data
public class UserKitOrderDto {
    private String kitOrderId;
    private String kitCompanyId;
    private String kitCompanyName;
    private String address;
    private String mealkitName;
    private String mealkitId;
    private String tel;
    private String email;
    private String userName;
    private int quantity;
    private int price;
    private int total;
    private int statusId;
    private String status;
    private String orderDate;
}
