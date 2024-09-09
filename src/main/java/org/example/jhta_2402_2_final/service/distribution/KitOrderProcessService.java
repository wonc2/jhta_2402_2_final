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

    // 아래부터 3개는 창고 조회를 위해 사용되는 부분

    public Integer findOrderQuantityByKitOrderId(String kitOrderId) {
        // 전체 UUID와 매칭하기 위해 앞 몇 글자를 LIKE 조건으로 전달
        return kitOrderProcessDao.findOrderQuantityByKitOrderId(kitOrderId);
    }

    public String findMealKitByKitOrderId(String kitOrderId) {
        // 전체 UUID와 매칭하기 위해 앞 몇 글자를 LIKE 조건으로 전달
        return kitOrderProcessDao.findMealKitByKitOrderId(kitOrderId);
    }

    // 원래 쓰던 수량별로 밀키트에 필요한 재료 나타내는 서비스 메소드
    /*public List<Map<String, Object>> findKitRecipeWithStock(String kitOrderId) {
        String mealkitId = findMealKitByKitOrderId(kitOrderId);
        Integer orderQuantity = findOrderQuantityByKitOrderId(kitOrderId);


        if (orderQuantity == null) {
            orderQuantity = 0;
        }

        return kitOrderProcessDao.findKitRecipeWithStock(mealkitId, orderQuantity);
    }*/

    public List<Map<String, Object>> findKitRecipeWithStockAndSupplier(String mealKitId, int orderQuantity) {

        return kitOrderProcessDao.findKitRecipeWithStockAndSupplier(mealKitId, orderQuantity);
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

        // 5. 주문 상태를 '처리완료'로 업데이트 => 3에서 6으로 수정
        kitOrderProcessDao.updateKitOrderStatus(kitOrderId, 6);

        // 6. 주문 로그 테이블 (KIT_ORDER_LOG) 에 상태가 3인 것을 추가
        kitOrderProcessDao.insertKitOrderLog(kitOrderId, 6);


//// 밑에서부턴 밀키트 창고 재고 업데이트 부분
//
//        KitOrderDto kitOrder = salesDao.selectKitOrderById(kitOrderId);
//
//        String kitCompanyId = kitOrder.getKitCompanyId();
//        String mealkitId = kitOrder.getMealkitId();
//        int quantity = kitOrder.getQuantity();
//
////밀키트 아이디와 판매업체 아이디로 창고 재고를 확인
//        KitStorageDto kitStorageDto = salesDao.selectKitStorageById(kitCompanyId,mealkitId);
//        UUID kiStorageId = UUID.randomUUID();
//
////해당 재고가 없으면 새로 추가
//        if (kitStorageDto == null) {
//            salesDao.insertKitStorage(kiStorageId, kitCompanyId, mealkitId, quantity);
//        }
//
////재고가 있으면 업데이트
//        else {
//            salesDao.updateKitStorage(kitStorageDto.getKitStorageId(), quantity);
//        }


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


    // 데이터 저장을 위한 메서드
    /*public void saveOrder(KitOrderProcessDto kitOrderProcessDto) {
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
    }*/

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

    /*// 모든 주문을 조회하는 메서드
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
    }*/

    // 밀키트 회사에서 필요한 재료와 수량 주문 넣는 서비스 매소드
    /*@Transactional
    public void requestProductOrders(String kitOrderId, List<Map<String, Object>> orderDetails) {
        boolean allOrdersProcessed = true; // 전체 주문이 성공적으로 처리되었는지 여부

        for (Map<String, Object> orderDetail : orderDetails) {
            // 재료 이름, 공급업체 이름, 최소 가격을 기반으로 sourceId, sourcePriceId 가져오기
            String sourceName = (String) orderDetail.get("sourceName");
            String supplierName = (String) orderDetail.get("supplierName");
            int minPrice = (int) orderDetail.get("minPrice");
            int insufficientQuantity = (int) orderDetail.get("insufficientQuantity"); // 부족한 수량

            Map<String, Object> productInfo  = kitOrderProcessDao.findSourceAndPriceInfo(sourceName, supplierName, minPrice);
            String sourcePriceId = (String) productInfo.get("sourcePriceId");
            log.info("sourcePriceId ===== ", sourcePriceId);

            // 부족한 수량이 0보다 큰지 확인
            if (insufficientQuantity > 0) {
                try {
                    // 새로운 발주 생성
                    kitOrderProcessDao.insertProductOrder(sourcePriceId, insufficientQuantity);

                    // 주문 로그 기록
                    // kitOrderProcessDao.insertProductOrderLog(productOrderId, 1); // 상태 1 = 발주 요청
                } catch (Exception e) {
                    log.error("Error processing product order for sourceName: {}, supplierName: {}", sourceName, supplierName, e);
                    throw new RuntimeException("Failed to process product order", e);
                }
            } else {
                log.warn("Insufficient quantity is not valid for sourceName: {}, supplierName: {}", sourceName, supplierName);
            }
        }

        // KIT_ORDER 테이블의 상태를 처리중(STATUS_ID = 2)로 업데이트
        kitOrderProcessDao.updateKitOrderStatus(kitOrderId, 2);

        // KIT_ORDER_LOG 테이블에 로그 기록 추가 (상태가 2)
        kitOrderProcessDao.insertKitOrderLog(kitOrderId, 2);  // 상태 2 = 처리중

        // 6. PRODUCT_ORDER_LOG 테이블에 로그 기록 추가 (상태가 1)
        // kitOrderProcessDao.insertProductOrderLog(productOrderId, 1);
    }*/
    /*@Transactional
    public boolean requestProductOrders(String kitOrderId, List<String> sourceNames, List<String> supplierNames, List<Integer> minPrices, List<Integer> insufficientQuantities) {
        for (int i = 0; i < sourceNames.size(); i++) {
            String sourceName = sourceNames.get(i);
            String supplierName = supplierNames.get(i);
            int minPrice = minPrices.get(i);
            int insufficientQuantity = insufficientQuantities.get(i);

            // 비즈니스 로직 수행 (재고 부족 처리 및 발주 생성)
            Map<String, Object> productInfo = kitOrderProcessDao.findSourceAndPriceInfo(sourceName, supplierName, minPrice);
            String sourcePriceId = (String) productInfo.get("sourcePriceId");

            // 부족한 수량이 있을 때 발주 생성 (PRODUCT_ORDER 에 INSERT)
            if (insufficientQuantity > 0) {
                kitOrderProcessDao.insertProductOrder(sourcePriceId, insufficientQuantity);
            }
        }

        // KIT_ORDER 테이블의 상태를 처리중(STATUS_ID = 2)로 업데이트
        kitOrderProcessDao.updateKitOrderStatus(kitOrderId, 2);

        // KIT_ORDER_LOG 테이블에 로그 기록 추가 (상태가 2)
        kitOrderProcessDao.insertKitOrderLog(kitOrderId, 2);  // 상태 2 = 처리중

        return true;
    }*/

    @Transactional
    public boolean requestProductOrders(RequestOrderDto requestOrderDto) {
        String kitOrderId = requestOrderDto.getKitOrderId();
        List<OrderDetail> orderDetails = requestOrderDto.getOrderDetailList();

        // 재료별로 발주 생성 로직 수행
        for (OrderDetail detail : orderDetails) {
            String sourceName = detail.getSourceName();
            String supplierName = detail.getSupplierName();
            int minPrice = detail.getMinPrice();
            int insufficientQuantity = detail.getInsufficientQuantity();

            // 비즈니스 로직 수행 (재고 부족 처리 및 발주 생성)
            // 재료회사ID, 재료ID, 재료가격 찾아와야함
            Map<String, Object> productInfo = kitOrderProcessDao.findProductCompanyIdAndSourceId(sourceName, supplierName, minPrice);
            // String sourcePriceId = (String) productInfo.get("sourcePriceId");

            String supplierId = (String) productInfo.get("productCompanyId");
            String sourceId = (String) productInfo.get("sourceId");

            // 부족한 수량이 있을 때 발주 생성 (PRODUCT_ORDER 테이블에 INSERT)
            // 재료주문아이디(UUID), 재료공급업체명, 재료의 ID, 부족한 수량, 재료의 가격, 현재시각, 상태 넣어야 함
            if (insufficientQuantity > 0) {
                // 재료발주요청 테이블에 넣기(PRODUCT_ORDER)
                kitOrderProcessDao.insertProductOrder(supplierId, sourceId, minPrice, insufficientQuantity, minPrice, kitOrderId);

                // 재료발주요청 로그에 값 넣기 위한 PRODUCT_ORDER SELECT(PRODUCT_ORDER_LOG 테이블)
                List<String> productOrderIds = kitOrderProcessDao.findProductOrderIds(kitOrderId);

                // 재료발주요청 로그 테이블에 값 INSERT(PRODUCT_ORDER_LOG)
                // 각 productOrderId에 대해 로그 생성
                for (String productOrderId : productOrderIds) {
                    kitOrderProcessDao.insertProductOrderLog(productOrderId);
                }


                // KIT_ORDER 테이블의 상태를 처리중(STATUS_ID = 2)로 업데이트
                kitOrderProcessDao.updateKitOrderStatus(kitOrderId, 2);

                // KIT_ORDER_LOG 테이블에 로그 기록 추가 (상태가 2)
                kitOrderProcessDao.insertKitOrderLog(kitOrderId, 2);  // 상태 2 = 처리중
            }
            // UUID, PRODUCT_COMPANY_ID, SOURCE_ID, QUANTITY, PRICE, NOW(), 1
        }

        return true;
    }




    /*public boolean processOrderRequest(List<Map<String, Object>> orderDetails) {
        try {
            for (Map<String, Object> orderDetail : orderDetails) {
                String sourceName = (String) orderDetail.get("sourceName"); // 원자재의 이름(감자, 김치, 쌀 등등)
                int insufficientQuantity = (int) orderDetail.get("insufficientQuantity");

                // 공급업체 정보와 해당 재료의 최소가 가져오기
                Map<String, Object> supplyInfo = kitOrderProcessDao.findSupplierInfoWithSourceName(sourceName);

                if (supplyInfo == null) {
                    // 공급업체 정보가 없는 경우 처리 실패
                    return false;
                }

                String productCompanyId = (String) supplyInfo.get("productCompanyId");
                int price = (int) supplyInfo.get("price");

                // PRODUCT_ORDER 테이블에 주문 INSERT
                kitOrderProcessDao.insertProductOrder(productCompanyId, price, insufficientQuantity);

            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }*/


}



