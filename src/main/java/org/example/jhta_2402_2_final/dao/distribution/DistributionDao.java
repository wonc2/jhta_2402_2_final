package org.example.jhta_2402_2_final.dao.distribution;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.distribution.LogisticsWareHouseDto;

import java.util.List;

@Mapper
public interface DistributionDao {

   List<LogisticsWareHouseDto> selectAllLogisticsWarehouse();
}
