package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.example.jhta_2402_2_final.dao.distribution.KitOrderProcessDao;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderProcessDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KitOrderProcessService {

    private final SqlSession sqlSession;
    private final KitOrderProcessDao kitOrderProcessDao;

    public List<Map<String, Object>> findAllOrder() {
        return kitOrderProcessDao.findAllOrder();
    }

    public void requestKitSourceOrder(Map<String, Object> requestKitSourceMap) {
        KitOrderProcessDto kitOrderProcessDto = KitOrderProcessDto.builder()
                .kitOrderUid(UUID.fromString((String) requestKitSourceMap.get("kitOrderID")))
                .kitCompanyName((String) requestKitSourceMap.get("kitCompanyName"))
                .kitName((String) requestKitSourceMap.get("kitName"))
                .quantity((Integer) requestKitSourceMap.get("quantity"))
                .kitOrderDate((String) requestKitSourceMap.get("kitOrderDate"))
                .status((String) requestKitSourceMap.get("status"))
                .build();

        kitOrderProcessDao.requestKitSourceOrder(kitOrderProcessDto);

    }

    public List<Map<String, Object>> findKitRecipe(String kitOrderId) {
        return kitOrderProcessDao.findKitRecipe(kitOrderId); // 미사용
    }

    // 아래부터 3개는 창고 조회를 위해 사용되는 부분

    public Integer findOrderQuantityByKitOrderId(String kitOrderId) {
        // 전체 UUID와 매칭하기 위해 앞 몇 글자를 LIKE 조건으로 전달
        return kitOrderProcessDao.findOrderQuantityByKitOrderId(kitOrderId);
    }

    public String findMealKitByKitOrderId(String kitOrderId) {
        // 전체 UUID와 매칭하기 위해 앞 몇 글자를 LIKE 조건으로 전달
        return kitOrderProcessDao.findMealKitByKitOrderId(kitOrderId);
    }

    public List<Map<String, Object>> findKitRecipeWithStock(String kitOrderId) {
        String mealkitId = findMealKitByKitOrderId(kitOrderId);
        Integer orderQuantity = findOrderQuantityByKitOrderId(kitOrderId);

        if (orderQuantity == null) {
            orderQuantity = 0;
        }

        return kitOrderProcessDao.findKitRecipeWithStock(mealkitId, orderQuantity);
    }

    @Transactional
    public boolean processKitOrder(String kitOrderId) {
        // 1. 주문에 해당하는 재료 목록과 필요 수량 가져옴
        Integer orderQuantity = findOrderQuantityByKitOrderId(kitOrderId);

        // 2. 주문에 해당하는 재료 목록과 필요 수량을 가져옴
        List<Map<String, Object>> ingredients = kitOrderProcessDao.findKitRecipeWithStock(kitOrderId, orderQuantity);

        // 3. 각 재료의 재고가 충분한지 확인
        for (Map<String, Object> ingredient : ingredients) {
            int required = (int) ingredient.get("필요수량");
            int stock = (int) ingredient.get("창고재고수량");

            if (stock < required) {
                return false; // 재고가 부족하면 처리 실패
            }
        }

        // 4. 재고 차감 및 주문 상태 업데이트
        for (Map<String, Object> ingredient : ingredients) {
            String sourceId = (String) ingredient.get("sourceId");
            int required = (int) ingredient.get("필요수량");

            // 창고 재고 차감
            kitOrderProcessDao.updateWarehouseStock(sourceId, -required);
        }

        // 5. 주문 상태를 '처리완료'로 업데이트
        kitOrderProcessDao.updateOrderStatus(kitOrderId, 3);

        return true;

    }


    /*public String findMealKitByKitOrderId(String kitOrderId) {
    }*/



    /*public List<Map<String, Object>> findKitSource() { return kitOrderProcessDao.findKitSource();
    }*/

    /*public void requestKitSourceOrder(Map<String, Object> requestKitSourceMap) {
        String kitOrderID = (String) requestKitSourceMap.get("modalKitOrderID"); // 수정됨
        String kitCompanyName = (String) requestKitSourceMap.get("modalKitCompanyName"); // 수정됨
        String kitName = (String) requestKitSourceMap.get("modalKitName"); // 수정됨
        String quantity = (String) requestKitSourceMap.get("modalQuantity"); // 수정됨
        String date = (String) requestKitSourceMap.get("modalDate"); // 수정됨

        log.info("Kit Order ID: " + kitOrderID);
        log.info("Kit Company Name: " + kitCompanyName);
        log.info("Kit Name: " + kitName);
        log.info("Quantity: " + quantity);
        log.info("Order Date: " + date);*/
    // kitOrderProcessDao.requestKitSourceOrder(kitOrderID, kitName, quantity, date);

}



