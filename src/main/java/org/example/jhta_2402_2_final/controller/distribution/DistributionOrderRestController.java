package org.example.jhta_2402_2_final.controller.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionMaterialDto;
import org.example.jhta_2402_2_final.service.distribution.DistributionOrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/distributionOrder")
public class DistributionOrderRestController {

    private final DistributionOrderService distributionOrderService;

    @GetMapping("/findSourcePricesByCategoryAndKeyword")
    public List<DistributionMaterialDto> findSourcePricesByCategoryAndKeyword(
            @RequestParam("category") String category,
            @RequestParam("keyword") String keyword) {
        log.info("Fetching source prices by category: {} and keyword: {}", category, keyword);
        return distributionOrderService.getFilteredSourcePrices(category, keyword);
    }

    @GetMapping("/getBestSuppliers")
    public List<DistributionMaterialDto> getBestSuppliers() {
        log.info("Fetching best suppliers");
        return distributionOrderService.getBestSuppliers();
    }
}
