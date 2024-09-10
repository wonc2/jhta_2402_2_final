package org.example.jhta_2402_2_final.dao.sales;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.jhta_2402_2_final.model.dto.sales.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface SalesDao {

    List<KitOrderDto> findAll();
    Optional<KitOrderDto> findById(UUID id);
    int insert(KitOrderDto kitOrderDto);
    int update(KitOrderDto kitOrderDto);
    int delete(UUID id);

    List<KitOrderDto> search(String category, String keyword);

    // 조인 셀렉 가져오기
    List<KitOrderDetailDto> findAllDetail();

    //업체명이랑 pk가져오기
    List<Map<String, String>> getKitCompanyIdAndNames();

    //밀키트명이랑 pk가져오기
    List<Map<String, Object>> getMealkitIdAndNames();

    // 상태 변경하기
    int updateKitOrderStatus(UUID kitOrderId, int statusId);

    //밀키트별 재료 가져오기
    List<KitSourceDetailDto> findAllKitSourceDetail();

    //재료명이랑 pk가져오기
    List<Map<String, String>> getSourceIdAndNames();

    void insertMealkit(@Param("mealkitName") String mealkitName);

    String getMealkitIdByName(@Param("name") String name);

    void insertKitSources(@Param("mealkitId") String mealkitId,
                          @Param("sourceId") String sourceId,
                          @Param("quantity") Integer quantity);

    //로그 셀렉
    List<KitOrderLogDto> selectKitOrderLogs();

    int insertKitOrderLog(UUID KitOrderId);

    //창고
    List<KitCompletedDto> selectKitStorage();

    //처리 완료된 애들만
    List<KitOrderLogDto> findAllCompleted();

    // 주문 id값으로 주문 정보를 가져옴
    KitOrderDto selectKitOrderById(UUID kitOrderId);

    //밀키트 아이디와 업체 아이디로 창고 정보를 가져옴
    KitStorageDto selectKitStorageById(String kitCompanyId, String mealkitId);

    //창고를 업데이트함
    void updateKitStorage(UUID kitStorageId, int quantity);

    //재고가 없으면 인서트함
    void insertKitStorage(UUID kitStorageId, String kitCompanyId, String mealkitId, int quantity);

    // 생산업체별 재료가격 셀렉
    List<SourcePriceDto> selectAllSourcePrice();

    //최소 재료 값 셀렉
    List<SourcePriceDto> selectMinSourcePrice();

    //밀키트 별 재료별 최소값, 수량 이거로 밀키트 가격 설정 할거
    List<KitPriceDto> selectMinKitPrice();


    //밀키트 가격 수정
    void updateKitPrice(@Param("mealkitId") String mealkitId, @Param("minPrice") int minPrice);

    // 밀키트 주문 재료별 최소값 포함한 상세정보
    List<OrderDetailDto> selectOrderDetail(UUID kitOrderId, int quantity);

    //최소값을 파는 업체명과 재료명으로 sourcePriceId를 찾아옴
    UUID findSourcePriceId(String sourceName, String companyName);

    //product_order 인서트
    int insertProductOrder(UUID productOrderId, String companyName, String sourceName, int itemQuantity, int minPrice, UUID kitOrderId);

    //product_order 테이블 조회
    List<ProductOrderDetailDto> selectProductOrder();

    //product_order_log 인서트
    int insertProductOrderLog(UUID productOrderId);

    //product_order_log 셀렉
    List<ProductOrderLogDetailDto> selectProductOrderLog();


    void updateKitOrderCancel(UUID kitOrderId);

    void updateProductOrderCancel(UUID kitOrderId);

    List<UUID> selectProductOrderIdByKitOrderId(UUID kitOrderId);

    //창고에서 밀키트 별 재고 확인
    List<Map<String, String>> selectKitStorageTotalQuantity();

    //처리완료된(판매된) 밀키트 수량 확인
    List<Map<String, String>> selectKitTotalQuantity();

    //업체별 누적 판매량과 금액
    List<Map<String, String>> selectTotalQuantityByCompanyName();

    //새로운 업체 등록
    void insertKitCompany(String companyName, String companyAddress);

    //월별 업체별
    List<MonthlySalesDto> getMonthlySales();
}