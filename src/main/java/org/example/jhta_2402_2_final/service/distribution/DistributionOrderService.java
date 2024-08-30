package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.dao.distribution.DistributionSourcePriceDao;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionBestSupplierDto;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionOrderDetailDto;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionSourcePriceDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DistributionOrderService {

    private final DistributionSourcePriceDao distributionSourcePriceDao;

    public List<DistributionSourcePriceDto> getSourcePricesForKit(String kitId) {
        return distributionSourcePriceDao.getSourcePricesForKit(kitId);
    }

    public List<DistributionBestSupplierDto> getBestSuppliers() {
        return distributionSourcePriceDao.getBestSuppliers();
    }

    public List<DistributionOrderDetailDto> getOrderDetails() {
        return distributionSourcePriceDao.getOrderDetails();
    }

    public List<DistributionSourcePriceDto> getAllSourcePrices() {
        return distributionSourcePriceDao.getAllSourcePrices();
    }
}
