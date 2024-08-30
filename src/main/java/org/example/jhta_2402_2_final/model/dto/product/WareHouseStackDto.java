package org.example.jhta_2402_2_final.model.dto.product;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class WareHouseStackDto {
    private UUID wareHouseStackId;
//    private ProductCompanyDto productCompanyDto;
    private ProductDto productDto;
    private int quanity;
}
