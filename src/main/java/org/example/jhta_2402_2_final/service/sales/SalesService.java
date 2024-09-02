package org.example.jhta_2402_2_final.service.sales;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.sales.SalesDao;
import org.example.jhta_2402_2_final.model.dto.sales.KitOrderDetailDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitOrderDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitSourceDetailDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesDao salesDao;

    public List<KitOrderDto> getAllKitOrder() {
        return salesDao.findAll();
    }

    public KitOrderDto getKitOrderById(UUID id) {
        return salesDao.findById(id).get();
    }

    public int createKitOrder(KitOrderDto kitOrderDto) {
        return salesDao.insert(kitOrderDto);
    }

    public int updateKitOrder(KitOrderDto kitOrderDto) {
        return salesDao.update(kitOrderDto);
    }

    public int deleteKitOrder(UUID id) {
        return salesDao.delete(id);
    }

    public List<KitOrderDto> searchKitOrder(String category, String keyword) {return  salesDao.search(category, keyword);}


    public List<KitOrderDetailDto> getAllKitOrderDetail() {

        return salesDao.findAllDetail();
    }

    //업체명이랑 pk가져오기
    public List<Map<String, String>> getKitCompanyIdAndNames() {

        return salesDao.getKitCompanyIdAndNames();
    }

    //밀키트 명이랑 pk가져오기
    public List<Map<String, Object>> getMealkitIdAndNames() {

        return salesDao.getMealkitIdAndNames();
    }

    // 상태 변경하기
    public int updateKitOrderStatus(String kitOrderId, int statusId) {
        return salesDao.updateKitOrderStatus(kitOrderId, statusId);
    }

    //밀키트랑 재료 가져오기
    public List<KitSourceDetailDto> getAllKitSourceDetail() {
        return salesDao.findAllKitSourceDetail();
    }

    public List<Map<String, String>> getSourceIdAndNames(){
        return salesDao.getSourceIdAndNames();
    }

    //새로운 밀키트 등록, 그에맞는 재료도 같이 등록
    @Transactional
    public void insertMealkit(String mealkitName, List<String> sourceIds, List<Integer> quantities) {
        // 1. 밀키트를 삽입
        salesDao.insertMealkit(mealkitName);

        // 2. 삽입된 밀키트의 ID 조회
        String mealkitId = salesDao.getMealkitIdByName(mealkitName);

        int sourceIdIndex = 0;
        // 3. 밀키트와 선택된 재료를 매핑, 수량이 0 이상인 것만
        for (int i = 0; i < quantities.size(); i++) {
            if (quantities.get(i) != null) {
                salesDao.insertKitSources(mealkitId, sourceIds.get(sourceIdIndex), quantities.get(i));
                sourceIdIndex++;
            }
        }
    }
}