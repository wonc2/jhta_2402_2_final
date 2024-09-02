package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.dao.distribution.DistributionSourcePriceDao;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionMaterialDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DistributionOrderService {

    private final DistributionSourcePriceDao distributionSourcePriceDao;

    public List<DistributionMaterialDto> getSourcePricesForKit(String kitId) {
        log.info("Fetching source prices for kitId: {}", kitId);
        return distributionSourcePriceDao.getSourcePricesForKit(kitId);
    }

    public List<DistributionMaterialDto> getBestSuppliers() {
        return distributionSourcePriceDao.getBestSuppliers();
    }

    public List<DistributionMaterialDto> getOrderDetails() {
        return distributionSourcePriceDao.getOrderDetails();
    }

    public List<DistributionMaterialDto> getAllSourcePrices() {
        return distributionSourcePriceDao.getAllSourcePrices();
    }

    public List<DistributionMaterialDto> getFilteredSourcePrices(String category, String keyword) {
        return distributionSourcePriceDao.findSourcePricesByCategoryAndKeyword(category, keyword);
    }
}



