package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.example.jhta_2402_2_final.dao.distribution.KitOrderProcessDao;
import org.example.jhta_2402_2_final.dao.sales.SalesDao;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderProcessDto;
import org.example.jhta_2402_2_final.model.dto.distribution.OrderDetail;
import org.example.jhta_2402_2_final.model.dto.distribution.RequestOrderDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitOrderDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitStorageDto;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.springframework.security.core.parameters.P;
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

    public Integer findOrderQuantityByKitOrderId(String kitOrderId) {
        return kitOrderProcessDao.findOrderQuantityByKitOrderId(kitOrderId);
    }

    public String findMealKitByKitOrderId(String kitOrderId) {
        return kitOrderProcessDao.findMealKitByKitOrderId(kitOrderId);
    }

    public List<Map<String, Object>> findKitRecipeWithStockAndSupplier(String mealKitId, int orderQuantity) {
        return kitOrderProcessDao.findKitRecipeWithStockAndSupplier(mealKitId, orderQuantity);
    }

    /*@Transactional
    public boolean processKitOrder(String kitOrderId) {
        // 1. 주문에 해당하는 재료 목록과 필요 수량 가져옴
        Integer orderQuantity = findOrderQuantityByKitOrderId(kitOrderId);

        // 2. 밀키트 주문에 해당하는 밀키트 ID 가져오기
        String mealKitId = findMealKitByKitOrderId(kitOrderId);

        // 3. 주문에 해당하는 재료 목록과 필요 수량을 가져옴(각 재료별)
        List<Map<String, Object>> ingredients = kitOrderProcessDao.findKitRecipeWithStock(mealKitId, orderQuantity);

        // 4. 각 재료의 재고가 충분한지 확인 및 재고 차감
        for (Map<String, Object> ingredient : ingredients) { // 각각의 재료별로 처리할것
            String sourceId = (String) ingredient.get("재료번호");
            int required = (int) ingredient.get("필요수량"); // 넘겨줘야 할 김치의 수량
            int totalStockOfSameSource = (int) ingredient.get("창고재고수량"); // 창고에 있는 모든 김치의 수량
            // totalStockOfSameSource = IFNULL(SUM(LWS.QUANTITY), 0) AS 창고재고수량

            if (totalStockOfSameSource >= required) { // 각 재료별로 창고 재고의 합이 요구하는 것 보다 크거나 같아면 로직 진행

                // 재고 리스트를 오래된 순서로 가져옴
                List<Map<String, Object>> warehouseStacks = kitOrderProcessDao.findWarehouseStacks(sourceId);

                // FIFO 방식으로 재고 차감
                for (Map<String, Object> stack : warehouseStacks) {

                    int stackId = (int) stack.get("유통창고ID");
                    int stock = (int) stack.get("재고수량"); // 현재 stackId 에 해당하는 재료의 재고

                    if (required <= 0) {
                        kitOrderProcessDao.updateKitOrderStatus(kitOrderId, 6);
                        kitOrderProcessDao.insertKitOrderLog(kitOrderId, 6);
                        return true; // 필요수량 만큼 전부 꺼내 갔으면 로그 업데이트 및 생성, 그리고 true 리턴
                    }

                    if (stock > 0) {
                        if (stock >= required) {
                            kitOrderProcessDao.updateWarehouseStockWithStackId(stackId, required);
                            required = 0;
                            kitOrderProcessDao.updateKitOrderStatus(kitOrderId, 6);
                            kitOrderProcessDao.insertKitOrderLog(kitOrderId, 6);
                            return true;
                        } else {
                            kitOrderProcessDao.updateWarehouseStockWithStackId(stackId, stock);
                            required -= stock;
                        }
                    }
                }

            } else {
                return false;
            }

            *//*if (required > 0) {
                return false;
            }*//*
        }

        return false;
    }*/

    @Transactional
    public boolean processKitOrder(String kitOrderId) {
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
}
