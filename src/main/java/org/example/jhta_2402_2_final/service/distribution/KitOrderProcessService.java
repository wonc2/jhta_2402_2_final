package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.example.jhta_2402_2_final.dao.distribution.KitOrderProcessDao;
import org.example.jhta_2402_2_final.dao.sales.SalesDao;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderProcessDto;



import org.example.jhta_2402_2_final.model.dto.sales.KitOrderDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitStorageDto;

import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class KitOrderProcessService {

    private final SqlSession sqlSession;
    private final KitOrderProcessDao kitOrderProcessDao;
    private final SalesService salesService;
    private final SalesDao salesDao;

    /*
    * [code-review] 다양한 곳에서도 Map 형태로 객체간 소통하는 것을 많이 보았습니다.
    * Map 자료구조는 매우 휼룽한 자료 구조입니다. 하지만 Java 라는 언어가 어떤것을 지향하는지 생각해봐야 합니다.
    * Map 으로 보내면 지금은 매우 편합니다. 어떤 변화에 있어서도 유연하게 대처할 수 있으니까요.
    * 하지만 개발 이란 것은 사실 개발이 10% 이고 유지보수가 90% 전 생각합니다. 당장 아래에 있는 requestKitSourceMap
    * 하여도 저는 해당 Map 어떤 필드가 더 있을지 알수가 없고 의도 자체도 파악이 힘듭니다.
    * requestKitSourceMap 받을 시 형변환 이라는 추가적인 리소스 까지 발생합니다.
    * Java는 객체지향 언어 입니다. 이 의미를 한번 더 새겨보시길 바랍니다.
    * */
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

        // 6. 주문 로그 테이블 (KIT_ORDER_LOG) 에 상태가 3인 것을 추가
        kitOrderProcessDao.insertOrderLogStatus(kitOrderId, 3);


// 밑에서부턴 밀키트 창고 재고 업데이트 부분

        KitOrderDto kitOrder = salesDao.selectKitOrderById(kitOrderId);

        String kitCompanyId = kitOrder.getKitCompanyId();
        String mealkitId = kitOrder.getMealkitId();
        int quantity = kitOrder.getQuantity();

//밀키트 아이디와 판매업체 아이디로 창고 재고를 확인
        KitStorageDto kitStorageDto = salesDao.selectKitStorageById(kitCompanyId,mealkitId);
        UUID kiStorageId = UUID.randomUUID();

//해당 재고가 없으면 새로 추가
        if (kitStorageDto == null) {
            salesDao.insertKitStorage(kiStorageId, kitCompanyId, mealkitId, quantity);
        }

//재고가 있으면 업데이트
        else {
            salesDao.updateKitStorage(kitStorageDto.getKitStorageId(), quantity);
        }


        return true;

    }
}