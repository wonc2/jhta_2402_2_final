package org.example.jhta_2402_2_final.controller.distribution;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionBestSupplierDto;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionOrderDetailDto;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionSourcePriceDto;
import org.example.jhta_2402_2_final.service.distribution.DistributionOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/distribution")
public class DistributionOrderController {

    private final DistributionOrderService distributionOrderService;

    @GetMapping("/kit-source-prices")
    public String getSourcePricesForKit(@RequestParam("kitId") String kitId, Model model) {
        List<DistributionSourcePriceDto> sourcePrices = distributionOrderService.getSourcePricesForKit(kitId);
        model.addAttribute("sourcePrices", sourcePrices);
        return "kitSourcePrices"; // 반환할 뷰의 이름
    }

    @GetMapping("/best-suppliers")
    public String getBestSuppliers(Model model) {
        List<DistributionBestSupplierDto> bestSuppliers = distributionOrderService.getBestSuppliers();
        model.addAttribute("bestSuppliers", bestSuppliers);
        return "bestSuppliers"; // 반환할 뷰의 이름
    }

    @GetMapping("/details")
    public List<DistributionOrderDetailDto> getOrderDetails() {
        return distributionOrderService.getOrderDetails();
    }
}
