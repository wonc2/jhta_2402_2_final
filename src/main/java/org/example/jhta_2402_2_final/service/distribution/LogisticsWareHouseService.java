package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.distribution.DistributionDao;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderDetailLogDto;
import org.example.jhta_2402_2_final.model.dto.distribution.LogisticsWareHouseDto;
//import org.example.jhta_2402_2_final.util.SmsUtil;

import org.example.jhta_2402_2_final.model.dto.distribution.ProductOrderLogDto;
import org.example.jhta_2402_2_final.model.dto.distribution.wareHouseChartDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogisticsWareHouseService {
    private final DistributionDao distributionDao;


    public int updateKitOrderStatus() {
        return distributionDao.updateKitOrderStatus();
    }

    public List<LogisticsWareHouseDto> selectAllLogisticsWarehouse() {
        return distributionDao.selectAllLogisticsWarehouse();
    }

    public int insertWarehouseStackForCompletedOrders() {
        return distributionDao.insertWarehouseStackForCompletedOrders();
    }

    public int updateProductOrderStatus() {
        return distributionDao.updateProductOrderStatus();
    }

    public List<Map<String, Object>> selectRequiredStack() {
        return distributionDao.selectRequiredStack();
    }

    public int updateStack(Map<String, Object> map) {
        return distributionDao.updateStack(map);
    }


    public int updateStackFirstRecord(Map<String, Object> params) {
        return distributionDao.updateStackFirstRecord(params);
    }

    public int deleteZeroQuantityRecords() {
        return distributionDao.deleteZeroQuantityRecords();
    }

    public List<KitOrderDetailLogDto> selectKitOrderLogDetailsBySourceId(String sourceId) {
        return distributionDao.selectKitOrderLogDetailsBySourceId(sourceId);
    }

    public List<ProductOrderLogDto> selectProductOrderLogDetailsBySourceId(String sourceId) {
        return distributionDao.selectProductOrderLogDetailsBySourceId(sourceId);
    }

    public List<LogisticsWareHouseDto> selectBySourceNameLogisticsWarehouse(String keyword) {
        return distributionDao.selectBySourceNameLogisticsWarehouse(keyword);
    }

    public List<String> selectProductOrderIdByStatus(int i) {
        return distributionDao.selectProductOrderIdByStatus(i);
    }

    public int insertProductOrderLog(List<String> productOrderIdList) {
        return distributionDao.insertProductOrderLog(productOrderIdList);
    }

    public List<String> selectKitOrderIdByStatus(int i) {
        return distributionDao.selectKitOrderIdByStatus(i);
    }

    public int insertKitOrderLog(List<String> kitOrderIdList) {
        return distributionDao.insertKitOrderLog(kitOrderIdList);
    }

    public void updateStackBySourceName(List<Map<String, Object>> combinedList) {
        for (Map<String, Object> item : combinedList) {
            String sourceName = (String) item.get("sourceName");
            int quantityToDeduct = (int) item.get("quantity");


            deductQuantityFIFO(sourceName, quantityToDeduct);
        }
    }

    private void deductQuantityFIFO(String sourceName, int quantityToDeduct) {
        int remainingQuantity = quantityToDeduct;

        List<LogisticsWareHouseDto> logisticsWareHouseDtos = distributionDao.selectStacksBySourceFIFO(sourceName);
        for (LogisticsWareHouseDto stack : logisticsWareHouseDtos) {
            if (remainingQuantity <= 0) {
                break;
            }

            int availableQuantity = stack.getQuantity();
            int deductedQuantity = Math.min(availableQuantity, remainingQuantity);

            Map<String, Object> params = new HashMap<>();
            params.put("stackId", stack.getLogisticsWareHouseUUID());
            params.put("deduction", deductedQuantity);
            distributionDao.updateStackQuantityFIFO(params);

            remainingQuantity -= deductedQuantity;
        }
        if (remainingQuantity > 0) {
            throw new RuntimeException("양이 충분하지 않음 소스 네임 >>>: " + sourceName);
        }


    }

    public void insertProductOrder(Map<String, Object> map) {
        distributionDao.insertProductOrder(map);
    }


    public List<LogisticsWareHouseDto> selectStacksBySourceFIFO(String sourceName) {
        return distributionDao.selectStacksBySourceFIFO(sourceName);
    }

    public void updateStackQuantityFIFO(Map<String, Object> params) {
        distributionDao.updateStackQuantityFIFO(params);
    }

    public List<Map<String, Object>> getSource() {
        return distributionDao.getSource();
    }

    public List<Map<String, Object>> getCompany() {
    return distributionDao.getCompanyName();
    }

    public List<wareHouseChartDto> getChartData(wareHouseChartDto dto) {
    return distributionDao.getChartData(dto);
    }
}

