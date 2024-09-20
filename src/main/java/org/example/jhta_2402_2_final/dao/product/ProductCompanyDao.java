package org.example.jhta_2402_2_final.dao.product;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.common.SourceDto;
import org.example.jhta_2402_2_final.model.dto.productCompany.ProductCompanyChartDto;
import org.example.jhta_2402_2_final.model.dto.productCompany.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mapper
public interface ProductCompanyDao {

    // 유저 인증
    Optional<String> getCompanyIdByUserId(String userId);
    Optional<String> getCompanyNameByUserId(String userId); // 한번만 씀

    // 생산업체에 등록된 생산품 리스트 가져옴
    List<CompanySourceDto> getSourcesByCompanyName(String companyId);
    // 모든 재료 리스트
    List<SourceDto> getAllSources(String companyId);

    // 생산업체 생산품 Create, Update, Delete
    int addSourceToCompany(AddSourceDto addSourceDto);
    int sourcePriceUpdate(SourcePriceUpdateDto updateDto);
    int sourcePriceHistory(SourcePriceUpdateDto updateDto);
    int deleteSourceFromCompany(String companySourceId);

    // Source 중복체크겸 id 가져오기 (return: null 이면 중복 없으므로 재료 테이블에 추가 -> addSource)
    String getSourceIdByName(String sourceName);
    int addSource(String sourceId, String sourceName);

    // 생산 창고 리스트
    List<ProductCompanyWarehouseDto> getWarehouseSources(ProductCompanySearchOptionDto paramData);
    // 제품 생산
    int produceSource(CompanySourceStackDto sourceStackDto);
    int deleteWarehouseProduceLog(String sourceWarehouseId);
    Map<String, Object> getSourcePriceIdBySourceWarehouseId(String sourceWarehouseId);

    boolean checkDuplicateCompanySource(AddSourceDto addSourceDto);

    List<ProductCompanyOrderDto> getProductOrderList(ProductCompanySearchOptionDto searchOptionDto);

    // 주문 처리
    int orderProcess(ProductCompanyOrderProcessDto orderProcessDto);
    int orderLog(ProductCompanyOrderProcessDto orderProcessDto);
    int outboundSource(ProductCompanyOrderProcessDto orderProcessDto);
    int orderStatusTempInsert(ProductCompanyOrderProcessDto orderProcessDto);
    int orderStatusTempDel(String orderId);
    int updateOrderTime(String orderId);
    boolean checkOrderLocks();
    boolean checkExpiredOrderLocks();
    int deleteExpiredOrderLocks();

    List<ProductCompanyChartDto> getChart(String companyId);
    List<ProductCompanyChartDto> orderChart(ProductCompanySearchOptionDto searchOptionDto);

    // 등록된 모든 재료 리스트 (검색용)
    List<String> selectAllCompanySource (String companyId);

    // 동시성처리
    int getOrderStatus(String orderId);
    int getSourcePriceById(String companySourceId);
    int getSourceQuantityFromWarehouse(String sourcePriceId);
}
