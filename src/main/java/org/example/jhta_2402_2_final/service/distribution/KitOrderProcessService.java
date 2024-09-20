package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.example.jhta_2402_2_final.dao.distribution.KitOrderProcessDao;
import org.example.jhta_2402_2_final.dao.sales.SalesDao;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderProcessDto;
import org.example.jhta_2402_2_final.model.dto.distribution.OrderDetail;
import org.example.jhta_2402_2_final.model.dto.distribution.RequestOrderDto;
import org.example.jhta_2402_2_final.model.dto.distribution.WareHouseStockChartDto;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KitOrderProcessService {

    private final SqlSession sqlSession;
    private final KitOrderProcessDao kitOrderProcessDao;
    private final SalesService salesService;
    private final SalesDao salesDao;
    private final SimpMessagingTemplate messagingTemplate;

    public List<Map<String, Object>> findNewOrders() {
        return kitOrderProcessDao.findNewOrders();
    }

    public List<Map<String, Object>> findProcessedOrders() {
        return kitOrderProcessDao.findProcessedOrders();
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

    public Integer findOrderQuantityByKitOrderId(String kitOrderId) {
        return kitOrderProcessDao.findOrderQuantityByKitOrderId(kitOrderId);
    }

    public String findMealKitByKitOrderId(String kitOrderId) {
        return kitOrderProcessDao.findMealKitByKitOrderId(kitOrderId);
    }

    public List<Map<String, Object>> findKitRecipeWithStockAndSupplier(String mealKitId, int orderQuantity) {
        return kitOrderProcessDao.findKitRecipeWithStockAndSupplier(mealKitId, orderQuantity);
    }

    @Transactional
    public boolean processKitOrder(String kitOrderId) {

        // 처리 전 상태(1) 이 아니면 로직 안 수행되게 false 로 리턴
        int currentStatus = kitOrderProcessDao.findKitOrderStatus(kitOrderId);
        if (currentStatus != 1) {
            return false;
        }

        // 1. 주문에 해당하는 재료 목록과 필요 수량 가져옴
        Integer orderQuantity = findOrderQuantityByKitOrderId(kitOrderId);

        // 2. 밀키트 주문에 해당하는 밀키트 ID 가져오기
        String mealKitId = findMealKitByKitOrderId(kitOrderId);

        // 3. 주문에 해당하는 재료 목록과 필요 수량을 가져옴 (각 재료별)
        List<Map<String, Object>> ingredients = kitOrderProcessDao.findKitRecipeWithStock(mealKitId, orderQuantity);

        // 4. 모든 재료의 재고가 충분한지 먼저 확인 (검증 단계)
        for (Map<String, Object> ingredient : ingredients) {
            String sourceId = (String) ingredient.get("재료번호");
            int required = ((Number) ingredient.get("필요수량")).intValue(); // Number로 받고 int로 변환
            int totalStockOfSameSource = ((Number) ingredient.get("창고재고수량")).intValue(); // 동일하게 변환

            // 하나라도 재고가 부족하면 바로 실패 처리 (출고 로직 처음부터 실행하지 않음)
            if (totalStockOfSameSource < required) {
                return false; // 재고 부족으로 출고 실패
            }
        }

        // 5. 모든 재료의 재고가 충분한 경우에만 출고 로직을 실행
        for (Map<String, Object> ingredient : ingredients) {
            String sourceId = (String) ingredient.get("재료번호");
            int required = ((Number) ingredient.get("필요수량")).intValue();

            // 재고 리스트를 오래된 순서로 가져옴
            List<Map<String, Object>> warehouseStacks = kitOrderProcessDao.findWarehouseStacks(sourceId);

            // FIFO 방식으로 재고 차감
            for (Map<String, Object> stack : warehouseStacks) {
                if (required <= 0) {
                    break; // 필요 수량을 모두 처리했으면 중단
                }

                String stackId = (String) stack.get("유통창고ID");
                int stock = (int) stack.get("재고수량");

                if (stock > 0) {
                    if (stock >= required) {
                        kitOrderProcessDao.updateWarehouseStockWithStackId(stackId, required); // 재고 차감
                        required = 0; // 필요 수량 충족
                        break; // 현재 재료는 처리 완료
                    } else {
                        kitOrderProcessDao.updateWarehouseStockWithStackId(stackId, stock);
                        required -= stock; // 남은 필요 수량 계산
                    }
                }
            }
        }

        // 6. 모든 재료가 성공적으로 출고되었으면 주문 상태를 '처리 완료'로 업데이트
        kitOrderProcessDao.updateKitOrderStatus(kitOrderId, 6); // 상태 6으로 변경
        kitOrderProcessDao.insertKitOrderLog(kitOrderId, 6); // 로그 기록 추가

        messagingTemplate.convertAndSend("/topic/warehouse/update", "update");

        return true; // 성공적으로 처리 완료
    }


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

    @Transactional
    public boolean requestProductOrders(RequestOrderDto requestOrderDto) {
        String kitOrderId = requestOrderDto.getKitOrderId();
        List<OrderDetail> orderDetails = requestOrderDto.getOrderDetailList();

        for (OrderDetail detail : orderDetails) {
            String sourceName = detail.getSourceName();
            String supplierName = detail.getSupplierName();
            int minPrice = detail.getMinPrice();
            int insufficientQuantity = detail.getInsufficientQuantity();

            Map<String, Object> productInfo = kitOrderProcessDao.findProductCompanyIdAndSourceId(sourceName, supplierName, minPrice);

            String supplierId = (String) productInfo.get("productCompanyId");
            String sourceId = (String) productInfo.get("sourceId");

            if (insufficientQuantity > 0) {
                kitOrderProcessDao.insertProductOrder(supplierId, sourceId, minPrice, insufficientQuantity, minPrice, kitOrderId);
                List<String> productOrderIds = kitOrderProcessDao.findProductOrderIds(kitOrderId);

                for (String productOrderId : productOrderIds) {
                    kitOrderProcessDao.insertProductOrderLog(productOrderId);
                }

                kitOrderProcessDao.updateKitOrderStatus(kitOrderId, 2);
                kitOrderProcessDao.insertKitOrderLog(kitOrderId, 2);
            }
        }

        return true;
    }

    public List<WareHouseStockChartDto> findLogisticsWarehouseStock() {

        return kitOrderProcessDao.findAllWarehouseStocks();
    }


}
