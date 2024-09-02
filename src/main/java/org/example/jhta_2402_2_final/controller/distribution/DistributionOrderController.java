package org.example.jhta_2402_2_final.controller.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionMaterialDto;
import org.example.jhta_2402_2_final.service.distribution.DistributionOrderService;
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
        // 가격 테이블을 조회합니다.
        List<DistributionMaterialDto> sourcePricesList = distributionOrderService.getAllSourcePrices();
        model.addAttribute("sourcePricesList", sourcePricesList);
        return "distribution"; // 기본 페이지에 대한 뷰 이름
    }

    @GetMapping("kit-source-prices")
    public String getSourcePricesForKit(@RequestParam("kitId") String kitId, Model model) {
        log.info("Received request to get source prices for kitId: {}", kitId);
        List<DistributionMaterialDto> sourcePricesList = distributionOrderService.getSourcePricesForKit(kitId);
        model.addAttribute("sourcePricesList", sourcePricesList);
        return "distribution"; // 결과를 보여줄 뷰 이름
    }

    @GetMapping("best-suppliers")
    public String getBestSuppliers(Model model) {
        log.info("Received request to get best suppliers");
        List<DistributionMaterialDto> bestSuppliers = distributionOrderService.getBestSuppliers();
        model.addAttribute("bestSuppliers", bestSuppliers);
        return "distribution";
    }

    @GetMapping("details")
    public String getOrderDetails(Model model) {
        log.info("Received request to get order details");
        List<DistributionMaterialDto> orderDetails = distributionOrderService.getOrderDetails();
        model.addAttribute("orderDetails", orderDetails);
        return "distribution";
    }

    @GetMapping("source-prices")
    public String getSourcePrices(@RequestParam("category") String category, @RequestParam("keyword") String keyword, Model model) {
        log.info("Received request to get source prices by category: {} and keyword: {}", category, keyword);
        List<DistributionMaterialDto> sourcePricesList = distributionOrderService.getFilteredSourcePrices(category, keyword);
        model.addAttribute("sourcePricesList", sourcePricesList);
        return "distribution"; // 뷰 이름
    }
}
