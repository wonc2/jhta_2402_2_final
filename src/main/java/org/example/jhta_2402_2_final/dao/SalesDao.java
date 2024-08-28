package org.example.jhta_2402_2_final.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.Employee;
import org.example.jhta_2402_2_final.model.dto.KitOrderDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface SalesDao {

    List<KitOrderDto> findAll();
    Optional<KitOrderDto> findById(UUID id);
    int insert(KitOrderDto kitOrderDto);
    int update(KitOrderDto kitOrderDto);
    int delete(UUID id);

    List<KitOrderDto> search(String category, String keyword);
}