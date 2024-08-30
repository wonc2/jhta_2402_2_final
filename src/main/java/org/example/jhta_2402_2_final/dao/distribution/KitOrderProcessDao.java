package org.example.jhta_2402_2_final.dao.distribution;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface KitOrderProcessDao {

    List<Map<String, Object>> findAllOrder();
}
