package org.example.jhta_2402_2_final.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.product.ProductDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductDao {
    List<Map<String, Object>> findAll();
    int insertProduct(ProductDto productDto);
}
