package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class wareHouseChartDto {

    private String startDate;
    private String endDate;
    private String date;
    private int price;
    private String sourceName;
    private String companyName;
}
