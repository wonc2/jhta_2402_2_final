package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DistributionMaterialDto {
    private String sourceId;
    private String materialName;
    private int price;
    private String supplierName;
    private int lowestPrice;
    private String bestSupplierName;

    private int quantity; // 수량
    private String orderDate; // 주문 날짜
}
