package org.example.jhta_2402_2_final.model.dto.productCompany;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class CompanySourceStackDto {
    private String sourcePriceId;
    @Positive(message = "0 이하의 수, 문자, 공백 입력 x")
    private int sourceQuantity;
    @PositiveOrZero
    private int checkQuantity;
}
