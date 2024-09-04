package org.example.jhta_2402_2_final.model.dto.common;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SourcePriceViewDto {
    private int rowNum;
    private String productCompanyName;
    private String sourceName;
    private int sourcePrice;
}
