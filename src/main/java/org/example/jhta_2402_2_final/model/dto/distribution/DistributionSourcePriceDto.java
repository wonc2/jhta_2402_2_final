package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 생산업체가 적은 재료가격 가져오기
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DistributionSourcePriceDto {
    private String mealkitId;
    private String materialName;
    private int price;
    private String supplierName;
}
