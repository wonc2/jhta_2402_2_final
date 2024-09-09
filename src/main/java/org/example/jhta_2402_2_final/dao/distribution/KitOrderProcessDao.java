package org.example.jhta_2402_2_final.dao.distribution;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderProcessDto;
import java.util.List;
import java.util.Map;

@Mapper
public interface KitOrderProcessDao {

    List<Map<String, Object>> findAllOrder();

    void requestKitSourceOrder(KitOrderProcessDto kitOrderProcessDto);

    // 주문번호(id)를 파라미터로 해서 마이바티스 매핑 후 레시피 조회
    List<Map<String, Object>> findKitRecipe(@Param("kitOrderId") String kitOrderId);

    // 주문 내역을 조회하는 쿼리 (키워드 기반)
    List<Map<String, Object>> findOrdersByKeyword(@Param("orderKeyword") String orderKeyword);


    // MealKit ID와 주문수량으로 재료 및 창고 재고 정보 조회
    List<Map<String, Object>> findKitRecipeWithStock(@Param("mealkitId") String mealkitId, @Param("orderQuantity") int orderQuantity);

    // 주문번호(id)로 밀키트 ID 조회
    String findMealKitByKitOrderId(@Param("kitOrderId") String kitOrderId);

    // 주문번호(id)로 주문수량 조회
    int findOrderQuantityByKitOrderId(@Param("kitOrderId") String kitOrderId);

    // 창고 수량 차감
    void updateWarehouseStock(@Param("sourceId") String sourceId, @Param("quantityChange") int quantityChange);


    // KIT_ORDER_LOG 테이블에 추가
    void insertKitOrderLog(@Param("kitOrderId") String kitOrderId, @Param("status") int status);

    List<Map<String, Object>> findKitRecipeWithStockAndSupplier(@Param("mealkitId") String mealkitId, @Param("orderQuantity") int orderQuantity);


    //Map<String, Object> findSupplierInfoWithSourceName(@Param("sourceName") String sourceName);

    // 재료 이름을 이용해 해당 재료의 공급업체 정보 및 최소가를 가져오는 메소드
    // Map<String, Object> findSourceAndPriceIdByNameAndSupplier(@Param("sourceName") String sourceName, @Param("supplierName") String supplierName, @Param("minPrice") int minPrice);


    //void insertProductOrder(UUID productOrderId, String sourcePriceId, int insufficientQuantity);

    // SOURCE_PRICE_ID와 관련된 정보 조회
    Map<String, Object> findProductCompanyIdAndSourceId(@Param("sourceName") String sourceName,
                                                        @Param("supplierName") String supplierName,
                                                        @Param("minPrice") int minPrice);

    // PRODUCT_ORDER 테이블에 데이터 추가
    void insertProductOrder(@Param("supplierId") String supplierId,
                            String sourceId, @Param("quantity") int quantity, int insufficientQuantity, int minPrice, String kitOrderId);


    // KIT_ORDER_LOG 테이블에 새로운 로그 추가하는 메소드
    //void insertKitOrderLog(@Param("logId") UUID logId, @Param("kitOrderId") String kitOrderId, @Param("statusId") int statusId);

    // void updateKitOrderStatus(String kitOrderId, int i);
    // 밀키트 주문 상태 변경(KIT_ORDER 테이블)
    void updateKitOrderStatus(@Param("kitOrderId") String kitOrderId, @Param("status") int status);

    //
    List<String> findProductOrderIds(@Param("kitOrderId") String kitOrderId);

    void insertProductOrderLog(@Param("productOrderId") String productOrderId);
}
