package org.example.jhta_2402_2_final.service.sales;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.sales.SalesDao;
import org.example.jhta_2402_2_final.dao.sales.SalesUserDao;
import org.example.jhta_2402_2_final.model.dto.salesUser.UserKitOrderDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SalesUserService {

    final private SalesUserDao salesUserDao;
    final private SalesDao salesDao;

    public UserKitOrderDto selectKitCompanyIdByUserId(String userId) {
        return salesUserDao.selectKitCompanyIdByUserId(userId);
    }

    public List<UserKitOrderDto> selectKitOrderByKitCompanyId(String kitCompanyId) {
        return salesUserDao.selectKitOrderByKitCompanyId(kitCompanyId);
    }

    public void insertKitOrder(UserKitOrderDto dto) {
        String kitOrderId = String.valueOf(UUID.randomUUID());
        dto.setKitOrderId(kitOrderId);
        salesUserDao.insertKitOrder(dto);
        salesDao.insertKitOrderLog(UUID.fromString(kitOrderId));
    }

    public List<Map<String, Object>> selectKitStorage(String kitCompanyId) {
        return salesUserDao.selectKitStorage(kitCompanyId);
    }

    public List<Integer> selectMonthly(String kitCompanyId) {
        return salesUserDao.selectMonthly(kitCompanyId);
    }

    public int getTotalMonthSale(int currentYear, int currentMonth, String kitCompanyId) {
        return salesUserDao.getTotalMonthSale(currentYear, currentMonth, kitCompanyId);
    }

    public int getTotalYearSale(int currentYear, String kitCompanyId) {
        return salesUserDao.getTotalYearSale(currentYear, kitCompanyId);
    }

    public int getProcessCount(String kitCompanyId) {
        return salesUserDao.getProcessingCount(kitCompanyId);
    }

    public int getCompleteCount(String kitCompanyId) {
        return salesUserDao.getCompleteCount(kitCompanyId);
    }
}
