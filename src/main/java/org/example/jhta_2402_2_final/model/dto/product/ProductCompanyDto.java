package org.example.jhta_2402_2_final.model.dto.product;

import lombok.Getter;

import java.util.UUID;

@Getter
public class ProductCompanyDto {
    private UUID productCompanyId;
    private String productCompanyName;
    private String productCompanyAddress;
}
