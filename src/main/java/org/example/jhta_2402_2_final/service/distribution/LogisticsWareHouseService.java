package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.ProductDao;
import org.example.jhta_2402_2_final.dao.distribution.DistributionDao;
import org.example.jhta_2402_2_final.model.dto.distribution.LogisticsWareHouseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogisticsWareHouseService {
    private final DistributionDao distributionDao;

    public List<LogisticsWareHouseDto> selectAllLogisticsWarehouse(){
      return  distributionDao.selectAllLogisticsWarehouse();
    }

}

