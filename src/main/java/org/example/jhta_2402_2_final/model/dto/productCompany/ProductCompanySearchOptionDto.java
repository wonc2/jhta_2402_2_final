package org.example.jhta_2402_2_final.model.dto.productCompany;

import lombok.Builder;
import lombok.Getter;

// 검색 옵션 dto
@Getter
@Builder(toBuilder = true)
public class ProductCompanySearchOptionDto {
    private Integer statusId;
    private String sourceName;
    private String companyId;
    private Integer month;
}