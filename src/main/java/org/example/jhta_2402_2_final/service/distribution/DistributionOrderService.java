package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.dao.distribution.DistributionSourcePriceDao;

import org.example.jhta_2402_2_final.model.dto.distribution.DistributionBestSupplierDto;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionOrderDetailDto;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionSourcePriceDto;
import org.springframework.stereotype.Service;

import java.util.List;

//생산업체에게 발주하기

@Service
@Slf4j
@RequiredArgsConstructor
public class DistributionOrderService {

    private final DistributionSourcePriceDao distributionSourcePriceDao;

    public List<DistributionSourcePriceDto> getSourcePricesForKit(String kitId) {
        log.info("Fetching source prices for kit ID: {}", kitId);
        return distributionSourcePriceDao.getSourcePricesForKit(kitId);
    }

    public List<DistributionBestSupplierDto> getBestSuppliers() {
        log.info("Fetching the best suppliers");
        return distributionSourcePriceDao.getBestSuppliers();
    }

    public List<DistributionOrderDetailDto> getOrderDetails() {
        return distributionSourcePriceDao.getOrderDetails();
    }
}
