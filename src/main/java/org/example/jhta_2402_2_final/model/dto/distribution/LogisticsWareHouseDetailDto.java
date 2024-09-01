package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogisticsWareHouseDetailDto {

    private Date logDate;
    private String status;
    private int quantity;
    private String sourceName;
    private String productCompany;

    


}
