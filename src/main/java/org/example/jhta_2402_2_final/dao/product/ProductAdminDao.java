package org.example.jhta_2402_2_final.dao.product;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.common.StatusDto;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductAdminDao {
    // 리스트 가져옴
    List<Map<String, Object>> findAll();
    List<Map<String, Object>> getProductListByParams(Map<String, Object> params);
    List<ProductCompanyDto> getAllCompanies();

    List<StatusDto> getAllStatus();
}
