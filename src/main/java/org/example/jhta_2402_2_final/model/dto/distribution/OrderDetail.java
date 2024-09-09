package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDetail {
    private String sourceName;
    private String supplierName;
    private int minPrice;
    private int insufficientQuantity;
}
