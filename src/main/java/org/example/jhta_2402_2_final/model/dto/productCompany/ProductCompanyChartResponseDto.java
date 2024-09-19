package org.example.jhta_2402_2_final.model.dto.productCompany;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ProductCompanyChartResponseDto {
    private List<ProductCompanyChartDto> warehouseChart;
    private List<ProductCompanyChartDto> salesChart;
}
