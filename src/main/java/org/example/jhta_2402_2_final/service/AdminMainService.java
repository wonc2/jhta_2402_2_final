package org.example.jhta_2402_2_final.service;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.AdminMainDao;
import org.example.jhta_2402_2_final.model.dto.Employee;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminMainService {

    private final AdminMainDao adminMainDao;

    public List<Employee> getAllEmp() {
        return adminMainDao.findAll();
    }

    public Employee getEmpById(Long id) {
        return adminMainDao.findById(id).get();
    }

    public int createEmp(Employee employee) {
        return adminMainDao.insert(employee);
    }

    public int updateEmp(Employee employee) {
        return adminMainDao.update(employee);
    }

    public int deleteEmp(Long id) {
        return adminMainDao.delete(id);
    }

    public List<Employee> searchEmp(String category, String keyword) {return  adminMainDao.search(category, keyword);}
}