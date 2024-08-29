package org.example.jhta_2402_2_final.dao.product;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProductCompanyDao {
    List<Map<String, Object>> getSourcesByCompanyName(String productCompanyName);
}
