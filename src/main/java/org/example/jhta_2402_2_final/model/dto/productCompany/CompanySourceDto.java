package org.example.jhta_2402_2_final.model.dto.productCompany;

import lombok.Getter;

// CompanySource Table 리스트 뿌려주는용 ( 현재 단독으로 안쓰임 CompanySourceTableDto 에 담아서 사용중 )
@Getter
public class CompanySourceDto {
    private String companySourceId;
    private String sourceName;
    private int sourcePrice;
    private int totalQuantity;
}
