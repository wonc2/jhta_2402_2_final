package org.example.jhta_2402_2_final.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.product.ProductDto;

import java.util.List;

@Mapper
public interface SampleProductDao {
    List<ProductDto> findAll();
    int insertNewProduct(ProductDto productDto);
}
