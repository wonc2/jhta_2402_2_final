package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.*;

import java.util.Date;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogisticsWareHouseDetailDto {

    private UUID logPk; //나중에 UUID로 할 때는 UUID로 바꿔야함
    private Date logDate;
    private int quantity;
    private UUID sourcePk; //나중에 UUID로 할 때는 UUID로 바꿔야함
    private String sourceName;
    private String productCompany;

    

}
