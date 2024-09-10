package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogisticsWareHouseDto {

//    진짜 LogisticsWareHouse 테이블에서 select하면 나오는 값들,
    private String logisticsWareHouseSourcePK;
    private String sourceUUID;
    private String logisticsWareHouseUUID;
    private int quantity;
    private Date create_at;

//    내가 필요해서 가공한 것들
    private String wareHouseName;
    private String sourceName;
    private String productCompanyName;




}
