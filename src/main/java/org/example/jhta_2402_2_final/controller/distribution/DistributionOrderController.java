package org.example.jhta_2402_2_final.controller.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionMaterialDto;
import org.example.jhta_2402_2_final.service.distribution.DistributionOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/distributionOrder")
public class DistributionOrderController {

    private final DistributionOrderService distributionOrderService;

    @GetMapping
    public String getDefaultPage(Model model) {
        log.info("Received request to get default page");
        List<DistributionMaterialDto> sourcePricesList = distributionOrderService.getAllSourcePrices();
        model.addAttribute("sourcePricesList", sourcePricesList);
        return "distribution"; // 기본 페이지에 대한 뷰 이름
    }

    @GetMapping("source-prices")
    public ResponseEntity<List<DistributionMaterialDto>> getSourcePrices(
            @RequestParam("category") String category,
            @RequestParam("keyword") String keyword) {
        log.info("Received request to get source prices by category: {} and keyword: {}", category, keyword);
        List<DistributionMaterialDto> sourcePricesList = distributionOrderService.getFilteredSourcePrices(category, keyword);
        return ResponseEntity.ok(sourcePricesList); // JSON 형식으로 응답
    }

    @GetMapping("best-suppliers")
    public ResponseEntity<List<DistributionMaterialDto>> getBestSuppliers() {
        log.info("Received request to get best suppliers");
        List<DistributionMaterialDto> bestSuppliers = distributionOrderService.getBestSuppliers();
        return ResponseEntity.ok(bestSuppliers); // JSON 형식으로 응답
    }
}
