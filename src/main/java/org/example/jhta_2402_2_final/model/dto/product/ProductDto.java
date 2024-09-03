package org.example.jhta_2402_2_final.model.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ProductDto {
    private UUID productUid;
    private String productName;

    public ProductDto(UUID productUid, String productName) {
        this.productUid = productUid;
        this.productName = productName;
    }
}
