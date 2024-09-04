package org.example.jhta_2402_2_final.dao.distribution;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.distribution.LogisticsWareHouseDto;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Mapper
public interface DistributionDao {

    List<LogisticsWareHouseDto> selectAllLogisticsWarehouse();

    int insertWarehouseStackForCompletedOrders();

    int updateProductOrderStatus();

    List<Map<String, Object>> selectRequiredStack();

    int updateStack(Map<String,Object> map);

    int updateKitOrderStatus();
    int updateStackFirstRecord(Map<String,Object> map);

    int deleteZeroQuantityRecords();

    List<Map<String,Object>> selectKitOrderLogDetailsBySourceId(String sourceID);
}
