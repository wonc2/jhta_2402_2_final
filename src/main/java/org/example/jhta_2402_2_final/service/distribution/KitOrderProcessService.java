package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Store;
import org.example.jhta_2402_2_final.dao.distribution.KitOrderProcessDao;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderProcessDto;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KitOrderProcessService {

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
        return kitOrderProcessDao.findKitRecipe(kitOrderId);
    }



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


    // 데이터 저장을 위한 메서드
    public void saveOrder(KitOrderProcessDto kitOrderProcessDto) {
        try {
            int result = kitOrderProcessDao.requestKitSourceOrder(kitOrderProcessDto);
            if (result > 0) {
                log.info("Order saved successfully: {}", kitOrderProcessDto);
            } else {
                log.warn("Order not saved: {}", kitOrderProcessDto);
            }
        } catch (Exception e) {
            log.error("Error saving order: {}", kitOrderProcessDto, e);
        }
    }

    // 주문 확인하기
    public List<KitOrderProcessDto> findOrdersByKeyword(String orderKeyword) {
        List<Map<String, Object>> results = kitOrderProcessDao.findOrdersByKeyword(orderKeyword);
        return results.stream()
                .map(result -> KitOrderProcessDto.builder()
                        .kitOrderUid(UUID.fromString((String) result.get("kitOrderID")))
                        .kitCompanyName((String) result.get("kitCompanyName"))
                        .kitName((String) result.get("kitName"))
                        .quantity((Integer) result.get("quantity"))
                        .kitOrderDate((String) result.get("kitOrderDate"))
                        .status((String) result.get("status"))
                        .build())
                .collect(Collectors.toList());
    }

    // 모든 주문을 조회하는 메서드
    public List<KitOrderProcessDto> findAllOrders() {
        List<Map<String, Object>> results = kitOrderProcessDao.findAllOrders();
        return results.stream()
                .map(result -> KitOrderProcessDto.builder()
                        .kitOrderUid(UUID.fromString((String) result.get("kitOrderID")))
                        .kitCompanyName((String) result.get("kitCompanyName"))
                        .kitName((String) result.get("kitName"))
                        .quantity((Integer) result.get("quantity"))
                        .kitOrderDate((String) result.get("kitOrderDate"))
                        .status((String) result.get("status"))
                        .build())
                .collect(Collectors.toList());
    }
}



