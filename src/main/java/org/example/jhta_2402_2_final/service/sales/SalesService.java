package org.example.jhta_2402_2_final.service.sales;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.exception.dao.sales.SalesDao;
import org.example.jhta_2402_2_final.model.dto.sales.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesDao salesDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public int createKitOrder(KitOrderDto kitOrderDto) {
        kitOrderDto.setKitOrderId(UUID.randomUUID());
        kitOrderDto.setStatusId(1);
        return salesDao.insert(kitOrderDto);
    }

    public List<KitOrderDetailDto> getAllKitOrderDetail() {
        return salesDao.findAllDetail();
    }

    public List<Map<String, String>> getKitCompanyIdAndNames() {
        return salesDao.getKitCompanyIdAndNames();
    }

    public List<Map<String, Object>> getMealkitIdAndNames() {
        return salesDao.getMealkitIdAndNames();
    }

    public int updateKitOrderStatus(UUID kitOrderId, int statusId) {
        int result = salesDao.updateKitOrderStatus(kitOrderId, statusId);
        salesDao.insertKitOrderLog(kitOrderId);
        return (result > 0) ? 1 : 0;
    }

    public List<KitSourceDetailDto> getAllKitSourceDetail() {
        return salesDao.findAllKitSourceDetail();
    }

    public List<Map<String, String>> getSourceIdAndNames() {
        return salesDao.getSourceIdAndNames();
    }

    @Transactional
    public void insertMealkit(String mealkitName, List<String> sourceIds, List<Integer> quantities) {
        salesDao.insertMealkit(mealkitName);
        String mealkitId = salesDao.getMealkitIdByName(mealkitName);
        insertKitSources(mealkitId, sourceIds, quantities);
    }

    private void insertKitSources(String mealkitId, List<String> sourceIds, List<Integer> quantities) {

        int sourceIndex = 0;

        for (int i = 0; i < quantities.size(); i++) {
            if (quantities.get(i) != null) {
                salesDao.insertKitSources(mealkitId, sourceIds.get(sourceIndex), quantities.get(i));
                sourceIndex++;
            }
        }
    }

    public List<KitOrderLogDto> getKitOrderLogs() {
        return salesDao.selectKitOrderLogs();
    }

    public int insertKitOrderLog(UUID kitOrderId) {
        return salesDao.insertKitOrderLog(kitOrderId);
    }

    public List<KitCompletedDto> getKitStorages() {
        return salesDao.selectKitStorage();
    }

    public List<KitOrderLogDto> getAllCompleted() {
        return salesDao.findAllCompleted();
    }

    public void updateKitStorage(UUID kitOrderId) {
        KitOrderDto kitOrder = salesDao.selectKitOrderById(kitOrderId);
        String kitCompanyId = kitOrder.getKitCompanyId();
        String mealkitId = kitOrder.getMealkitId();
        int quantity = kitOrder.getQuantity();

        KitStorageDto kitStorageDto = salesDao.selectKitStorageById(kitCompanyId, mealkitId);
        if (kitStorageDto == null) {
            salesDao.insertKitStorage(UUID.randomUUID(), kitCompanyId, mealkitId, quantity);
        } else {
            salesDao.updateKitStorage(kitStorageDto.getKitStorageId(), quantity);
        }
    }

    public List<SourcePriceDto> getAllSourcePrice() {
        return salesDao.selectAllSourcePrice();
    }

    public List<SourcePriceDto> getMinSourcePrice() {
        return salesDao.selectMinSourcePrice();
    }

    public List<KitPriceDto> getMinKitPrice() {
        return salesDao.selectMinKitPrice();
    }

    public void updateKitPrice(String mealkitId, int minPrice) {
        salesDao.updateKitPrice(mealkitId, minPrice);
    }

    public List<OrderDetailDto> getOrderDetails(UUID kitOrderId, int quantity) {
        return salesDao.selectOrderDetail(kitOrderId, quantity);
    }

    public List<ProductOrderDetailDto> selectProductOrder() {
        return salesDao.selectProductOrder();
    }

    public void processOrder(String[] sourceNames, String[] companyNames, int[] itemQuantities, int[] stackQuantities, int[] minPrices, UUID kitOrderId) {
        for (int i = 0; i < sourceNames.length; i++) {
            if (itemQuantities[i] > stackQuantities[i]) {
                insertProductOrder(companyNames[i], sourceNames[i], itemQuantities[i] - stackQuantities[i], minPrices[i], kitOrderId);
            }
        }
    }

    public void insertProductOrder(String companyName, String sourceName, int itemQuantity, int minPrice, UUID kitOrderId) {
        UUID productOrderId = UUID.randomUUID();
        salesDao.insertProductOrder(productOrderId, companyName, sourceName, itemQuantity, minPrice, kitOrderId);
        salesDao.insertProductOrderLog(productOrderId);
    }

    public List<ProductOrderLogDetailDto> selectProductOrderLog() {
        return salesDao.selectProductOrderLog();
    }

    public void updateKitOrderCancel(UUID kitOrderId) {
        salesDao.updateKitOrderCancel(kitOrderId);
        salesDao.updateProductOrderCancel(kitOrderId);
        salesDao.insertKitOrderLog(kitOrderId);

        List<UUID> productOrderIds = salesDao.selectProductOrderIdByKitOrderId(kitOrderId);
        for (UUID id : productOrderIds) {
            salesDao.insertProductOrderLog(id);
        }
    }

    public List<Map<String, String>> selectKitStorageTotalQuantity() {
        return salesDao.selectKitStorageTotalQuantity();
    }

    public List<Map<String, String>> selectKitTotalQuantity() {
        return salesDao.selectKitTotalQuantity();
    }

    public List<Map<String, String>> selectTotalQuantityByCompanyName() {
        return salesDao.selectTotalQuantityByCompanyName();
    }

    public List<Map<String, Object>> getMonthlySales() {
        List<MonthlySalesDto> dtos = salesDao.getMonthlySales();
        return aggregateMonthlySales(dtos);
    }

    private List<Map<String, Object>> aggregateMonthlySales(List<MonthlySalesDto> dtos) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, int[]> companyMap = new LinkedHashMap<>();
        int[] arr = new int[12];
        String prevCompany = null;

        int index = 0;
        for (MonthlySalesDto dto : dtos) {
            String key = dto.getCompanyName();
            if (prevCompany != null && !prevCompany.equals(key)) {
                companyMap.put(prevCompany, arr.clone());
                arr = new int[12];
                index = 0;
            }

            arr[index] = dto.getTotalSales();
            index++;
            prevCompany = key;
        }

        if (prevCompany != null) {
            companyMap.put(prevCompany, arr.clone());
        }

        for (Map.Entry<String, int[]> entry : companyMap.entrySet()) {
            Map<String, Object> resultMap = new LinkedHashMap<>();
            resultMap.put("companyName", entry.getKey());
            resultMap.put("monthlySales", entry.getValue());
            resultList.add(resultMap);
        }

        return resultList;
    }

    @Transactional
    public void insertKitCompany(InsertKitCompanyDto insertKitCompanyDto) {
        UUID kitCompanyId = UUID.randomUUID();
        insertKitCompanyDto.setKitCompanyId(kitCompanyId);
        insertKitCompanyDto.setUserPk(UUID.randomUUID());
        insertKitCompanyDto.setPassword(bCryptPasswordEncoder.encode(insertKitCompanyDto.getPassword()));

        salesDao.insertKitCompany(insertKitCompanyDto);
        salesDao.insertUser(insertKitCompanyDto);
        salesDao.insertKitCompanyMember(insertKitCompanyDto);

    }


    public String getKitOrderStatus(UUID kitOrderId) {
        return salesDao.getKitOrderStatus(kitOrderId);
    }

    public KitPriceDto getCurrentPriceAndMinPrice(String mealkitId) {
        return salesDao.getCurrentPriceAndMinPrice(mealkitId);
    }

    public boolean checkUserIdExists(String userId) {
        return salesDao.checkUserIdExists(userId);
    }

    public boolean checkCompanyNameExists(String companyName) {
        return salesDao.checkCompanyNameExists(companyName);
    }

    public boolean checkEmailExists(String email) {
        return salesDao.checkEmailExists(email);
    }

    public boolean checkTelExists(String tel) {
        return salesDao.checkTelExists(tel);
    }

    public boolean checkAddressExists(String address) {
        return salesDao.checkAddressExists(address);
    }

    public int getTotalMonthSale(int currentYear, int currentMonth) {
        return salesDao.getTotalMonthSale(currentYear, currentMonth);
    }

    public int getTotalYearSale(int currentYear) {
        return salesDao.getTotalYearSale(currentYear);
    }

    public int getProcessCount() {
        return salesDao.getProcessingCount();
    }

    public int getCompleteCount() {
        return salesDao.getCompleteCount();
    }
}
