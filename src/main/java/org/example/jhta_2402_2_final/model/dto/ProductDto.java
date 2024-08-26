package org.example.jhta_2402_2_final.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ProductDto {
    private UUID productUid;
    private UUID categoryUid;
    private String productName;
    private int productSize;
    private int productCost;

    public ProductDto(UUID productUid, UUID categoryUid, String productName, int productSize, int productCost) {
        this.productUid = productUid;
        this.categoryUid = categoryUid;
        this.productName = productName;
        this.productSize = productSize;
        this.productCost = productCost;
    }
}
