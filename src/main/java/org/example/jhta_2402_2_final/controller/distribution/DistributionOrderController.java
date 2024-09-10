package org.example.jhta_2402_2_final.controller.distribution;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.distribution.*;
import org.example.jhta_2402_2_final.service.distribution.DistributionOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
public class DistributionOrderController {

    private final DistributionOrderService distributionOrderService;

    @GetMapping("/distribution/order")
    public String order(Model model){
        List<KitOrderDistDto> kitOrderDtos = distributionOrderService.getAllKitOrderDto();
        model.addAttribute("kitOrderDtos",kitOrderDtos);

        List<KitOrderNameDto> kitOrderNameDtos = distributionOrderService.kitOrderNameDto();
        model.addAttribute("kitOrderNameDtos", kitOrderNameDtos);

        List<KitOrderSourceNameDto> kitOrderSourceNameDtos = distributionOrderService.kitOrderSourceNameDto();
        model.addAttribute("kitOrderSourceNameDtos",kitOrderSourceNameDtos);

        List<MinPriceSourceDto> minPriceSourceDtos = distributionOrderService.minPriceSourceDto();
        model.addAttribute("minPriceSourceDtos", minPriceSourceDtos);

        return "distribution/order";
    }

    @PostMapping("/distribution/sourceOrder")
    public String sourceOrder(@RequestParam("kitOrderId") UUID kitOrderId,
                              @RequestParam("sourceId") UUID sourceId,
                              @RequestParam("totalQuantity") int totalQuantity,
                              @RequestParam("sourcePrice") int sourcePrice,
                              @RequestParam("productCompanyId") UUID productCompanyId) {

        System.out.println("kitOrderId =>>>>>>>> " + kitOrderId);
        System.out.println("sourceId =>>>>>>>> " + sourceId);
        System.out.println("totalQuantity =>>>>>>>> " + totalQuantity);
        System.out.println("sourcePrice =>>>>>>>> " + sourcePrice);
        System.out.println("productCompanyId =>>>>>>>> " + productCompanyId);

        UUID productOrderId = UUID.randomUUID();

        distributionOrderService.processOrder(productOrderId, productCompanyId, sourceId, totalQuantity, sourcePrice, kitOrderId);

        return "redirect:/distribution/order";
    }







}
