package org.example.jhta_2402_2_final.model.dto.productCompany;

import lombok.Builder;
import lombok.Getter;
import org.example.jhta_2402_2_final.model.dto.common.SourceDto;

import java.util.List;

//  CompanySource Table 리스트 뿌려주는용
@Getter
@Builder
public class CompanySourceTableDto {
    private List<CompanySourceDto> companySourceList;
    private List<SourceDto> sources;
}