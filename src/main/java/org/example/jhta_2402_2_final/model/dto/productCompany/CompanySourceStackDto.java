package org.example.jhta_2402_2_final.model.dto.productCompany;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class CompanySourceStackDto {
    private String sourcePriceId;
    @Positive(message = "fail: 1 이상의 숫자만 입력 가능")
    private int sourceQuantity;
    @PositiveOrZero
    private int checkQuantity;
}
