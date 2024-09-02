package org.example.jhta_2402_2_final.dao.product;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.common.SourceDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper
public interface ProductCompanyDao {
    // 생산업체에 등록된 생산품 리스트 가져옴
    List<Map<String, Object>> getSourcesByCompanyName(String productCompanyName);
    // 모든 재료 리스트
    List<SourceDto> getAllSources();

    // 생산업체 생산품 Create, Update, Delete
    void addSourceToCompany(Map<String, Object> paramData);
    void deleteSourceFromCompany(String companySourceId);

    // Source 중복체크겸 id 가져오기 (return: null 이면 중복 없으므로 재료 테이블에 추가 -> addSource)
    String getSourceIdByName(String sourceName);
    void addSource(String sourceId, String sourceName);

    String getCompanyIdByName(String companyName);
}
