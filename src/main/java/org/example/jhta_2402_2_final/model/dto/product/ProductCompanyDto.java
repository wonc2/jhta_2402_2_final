package org.example.jhta_2402_2_final.model.dto.product;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class ProductCompanyDto {
    private UUID productCompanyId;
    private String productCompanyName;
    private String productCompanyAddress;

    @Builder
    public ProductCompanyDto(String productCompanyName, String productCompanyAddress) {
        this.productCompanyName = productCompanyName;
        this.productCompanyAddress = productCompanyAddress;
    }
}
