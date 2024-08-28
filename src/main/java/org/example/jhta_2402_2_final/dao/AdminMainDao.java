package org.example.jhta_2402_2_final.dao;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.Employee;

import java.util.List;
import java.util.Optional;

@Mapper
public interface AdminMainDao {
    List<Employee> findAll();
    Optional<Employee> findById(Long id);
    int insert(Employee employee);
    int update(Employee employee);
    int delete(Long id);

    List<Employee> search(String category, String keyword);
}