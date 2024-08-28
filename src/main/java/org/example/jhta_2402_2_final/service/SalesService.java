package org.example.jhta_2402_2_final.service;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.AdminMainDao;
import org.example.jhta_2402_2_final.dao.SalesDao;
import org.example.jhta_2402_2_final.model.dto.Employee;
import org.example.jhta_2402_2_final.model.dto.KitOrderDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesDao salesDao;

    public List<KitOrderDto> getAllKitOrder() {
        return salesDao.findAll();
    }

    public KitOrderDto getKitOrderById(UUID id) {
        return salesDao.findById(id).get();
    }

    public int createKitOrder(KitOrderDto kitOrderDto) {
        return salesDao.insert(kitOrderDto);
    }

    public int updateKitOrder(KitOrderDto kitOrderDto) {
        return salesDao.update(kitOrderDto);
    }

    public int deleteKitOrder(UUID id) {
        return salesDao.delete(id);
    }

    public List<KitOrderDto> searchKitOrder(String category, String keyword) {return  salesDao.search(category, keyword);}
}