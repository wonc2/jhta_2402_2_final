package org.example.jhta_2402_2_final.model.dto.productCompany;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

// 생산품 등록용 dto
@Getter
@Builder(toBuilder = true)
public class AddSourceDto {
    private String companyId;
    private String sourceId;
    private String sourceName;
    private int sourcePrice;
}
// sourceId 나 sourceName 중 하나만 넘어옴 둘 중 하나는 항상 null 값