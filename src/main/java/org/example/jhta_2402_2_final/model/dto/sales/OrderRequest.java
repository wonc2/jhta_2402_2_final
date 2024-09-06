package org.example.jhta_2402_2_final.model.dto.sales;

import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private String mealkitName;
    private int quantity;
    private List<OrderDetail> orderDetails;

    // Getters and Setters

    @Data
    public static class OrderDetail {
        private String sourceName;
        private int quantity;
        private int stackQuantity;
        private int minPrice;
        private String companyName;

        // Getters and Setters
    }
}
