package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DistributionBestSupplierDto {
    private String sourceId;
    private String materialName;
    private int lowestPrice;
    private String bestSupplierName;
}
