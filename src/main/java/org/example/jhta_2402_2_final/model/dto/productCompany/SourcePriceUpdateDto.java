package org.example.jhta_2402_2_final.model.dto.productCompany;

import lombok.Builder;
import lombok.Getter;

// 등록된 생산품 가격 수정용 dto
@Getter
@Builder(toBuilder = true)
public class SourcePriceUpdateDto {
    private String companyId;
    private String companySourceId;
    private int sourcePrice;
    private int oldPrice;
}
