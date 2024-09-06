package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.distribution.DistributionDao;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderDetailLogDTO;
import org.example.jhta_2402_2_final.model.dto.distribution.LogisticsWareHouseDto;
//import org.example.jhta_2402_2_final.util.SmsUtil;
import org.example.jhta_2402_2_final.model.dto.distribution.ProductOrderLogDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogisticsWareHouseService {
    private final DistributionDao distributionDao;


    public int updateKitOrderStatus(){
        return distributionDao.updateKitOrderStatus();
    }

    public List<LogisticsWareHouseDto> selectAllLogisticsWarehouse(){
        return  distributionDao.selectAllLogisticsWarehouse();
    }

    public int insertWarehouseStackForCompletedOrders(){
        return distributionDao.insertWarehouseStackForCompletedOrders();
    }
    public int updateProductOrderStatus(){
        return  distributionDao.updateProductOrderStatus();
    }

    public List<Map<String,Object>> selectRequiredStack(){
        return distributionDao.selectRequiredStack();
    }

    public int updateStack(Map<String,Object> map){
        return distributionDao.updateStack(map);
    }


    public int updateStackFirstRecord(Map<String, Object> params) {
        return distributionDao.updateStackFirstRecord(params);
    }

    public int deleteZeroQuantityRecords(){
        return distributionDao.deleteZeroQuantityRecords();
    }

    public List<KitOrderDetailLogDTO> selectKitOrderLogDetailsBySourceId(String sourceId){
        return distributionDao.selectKitOrderLogDetailsBySourceId(sourceId);
    }

    public List<ProductOrderLogDTO> selectProductOrderLogDetailsBySourceId(String sourceId) {
        return distributionDao.selectProductOrderLogDetailsBySourceId(sourceId);
    }

    public List<LogisticsWareHouseDto> selectBySourceNameLogisticsWarehouse(String keyword) {
    return distributionDao.selectBySourceNameLogisticsWarehouse(keyword);
    }
}

