package org.example.jhta_2402_2_final.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.Sample;

import java.util.List;
import java.util.Optional;

@Mapper
public interface SampleDao {
    List<Sample> findAll();
    Optional<Sample> findById(Long id);
    int insert(Sample sample);
    int update(Sample sample);
    int delete(Long id);
}