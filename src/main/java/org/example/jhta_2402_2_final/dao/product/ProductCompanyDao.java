package org.example.jhta_2402_2_final.dao.product;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.common.SourceDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper
public interface ProductCompanyDao {
    List<Map<String, Object>> getSourcesByCompanyName(String productCompanyName);
    List<SourceDto> getAllSources();

    void insertCompanySource(Map<String, Object> params);
}
