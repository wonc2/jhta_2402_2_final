package org.example.jhta_2402_2_final.controller.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionBestSupplierDto;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionOrderDetailDto;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionSourcePriceDto;
import org.example.jhta_2402_2_final.service.distribution.DistributionOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/distribution")
public class DistributionOrderController {

    private final DistributionOrderService distributionOrderService;

    @ResponseBody
    @GetMapping("kit-source-prices")
    public String getSourcePricesForKit(@RequestParam("kitId") String kitId, Model model) {
        List<DistributionSourcePriceDto> sourcePricesList = distributionOrderService.getSourcePricesForKit(kitId);
        model.addAttribute("sourcePricesList", sourcePricesList);
        return "index.html"; // 결과를 보여줄 뷰 이름
    }

    @GetMapping("best-suppliers")
    public String getBestSuppliers(Model model) {
        List<DistributionBestSupplierDto> bestSuppliers = distributionOrderService.getBestSuppliers();
        model.addAttribute("bestSuppliers", bestSuppliers);
        return "index.html";
    }

    @GetMapping("details")
    public List<DistributionOrderDetailDto> getOrderDetails() {
        return distributionOrderService.getOrderDetails();
    }

    @GetMapping("all-source-prices")
    public String getAllSourcePrices(Model model) {
        List<DistributionSourcePriceDto> allSourcePrices = distributionOrderService.getAllSourcePrices();
        model.addAttribute("allSourcePrices", allSourcePrices);
        return "index.html"; // 결과를 보여줄 뷰 이름
    }
}
