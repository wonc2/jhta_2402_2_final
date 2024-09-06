package org.example.jhta_2402_2_final.dao.distribution;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderDetailLogDTO;

import org.example.jhta_2402_2_final.model.dto.distribution.LogisticsWareHouseDto;
import org.example.jhta_2402_2_final.model.dto.distribution.ProductOrderLogDTO;

import java.util.List;
import java.util.Map;

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

    List<KitOrderDetailLogDTO> selectKitOrderLogDetailsBySourceId(String sourceID);

    List<ProductOrderLogDTO> selectProductOrderLogDetailsBySourceId(String sourceId);


    List<LogisticsWareHouseDto> selectBySourceNameLogisticsWarehouse(String keyword);

    List<String> selectProductOrderIdByStatus(int i);

    int insertProductOrderLog(List<String> productOrderIdList);

    List<String> selectKitOrderIdByStatus(int i);

    int insertKitOrderLog(List<String> kitOrderIdList);
}
