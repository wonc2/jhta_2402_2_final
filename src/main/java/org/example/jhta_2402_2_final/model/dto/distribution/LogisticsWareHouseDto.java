package org.example.jhta_2402_2_final.model.dto.distribution;

import java.util.UUID;

public class LogisticsWareHouseDto {

//    진짜 LogisticsWareHouse 테이블에서 select하면 나오는 값들,
    private UUID logisticsWareHouseSourcePK;
    private String sourceUUID;
    private String logisticsWareHouseUUID;
    private int quantity;

//    내가 필요해서 가공한 것들
    private String wareHouseName;
    private String sourceName;
    private String ProductCompanyName;


}
