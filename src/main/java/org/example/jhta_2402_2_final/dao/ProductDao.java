package org.example.jhta_2402_2_final.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyDto;
import org.example.jhta_2402_2_final.model.dto.product.ProductDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductDao {
    // 리스트 가져옴
    List<Map<String, Object>> findAll();
    List<Map<String, Object>> getProductListByParams(Map<String, Object> params);
    List<ProductCompanyDto> getAllCompanies();
    List<Map<String, Object>> getAllStatus();
    int insertProduct(ProductDto productDto);
}
