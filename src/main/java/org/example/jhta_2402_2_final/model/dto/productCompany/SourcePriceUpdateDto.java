package org.example.jhta_2402_2_final.model.dto.productCompany;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

// 등록된 생산품 가격 수정용 dto
@Getter
@Builder(toBuilder = true)
public class SourcePriceUpdateDto {
    private String companyId;
    private String companySourceId;
    @Positive(message = "0 이하의 수, 문자, 공백 입력 x")
    private int sourcePrice;
    private int oldPrice;
}
