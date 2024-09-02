package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.dao.distribution.KitOrderProcessDao;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderProcessDto;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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









}



