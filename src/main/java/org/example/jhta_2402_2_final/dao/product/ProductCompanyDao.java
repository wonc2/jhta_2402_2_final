package org.example.jhta_2402_2_final.dao.product;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.common.SourceDto;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyChartDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductCompanyDao {
    // 생산업체에 등록된 생산품 리스트 가져옴
    List<Map<String, Object>> getSourcesByCompanyName(String companyName);
    // 모든 재료 리스트
    List<SourceDto> getAllSources(String companyName);

    // 생산업체 생산품 Create, Update, Delete
    int addSourceToCompany(Map<String, Object> paramData);
    int deleteSourceFromCompany(String companySourceId);

    // Source 중복체크겸 id 가져오기 (return: null 이면 중복 없으므로 재료 테이블에 추가 -> addSource)
    String getSourceIdByName(String sourceName);
    String getCompanyIdByName(String companyName);
    int addSource(String sourceId, String sourceName);

    // 생산 창고 리스트
    List<Map<String, Object>> getWarehouseSources(String companyName);
    // 제품 생산
    int produceSource(Map<String, Object> paramData);

    int sourcePriceUpdate(Map<String, Object> paramData);

    boolean checkDuplicateCompanySource(Map<String, Object> paramData);

    List<Map<String, Object>> getProductOrderList(Map<String, Object> paramData);

    int orderProcess(Map<String, Object> paramData);

    int getSourceQuantityFromWarehouse(String sourcePriceId);

    int outboundSource(Map<String, Object> paramData);

    List<ProductCompanyChartDto> getChart(String companyName);
}
