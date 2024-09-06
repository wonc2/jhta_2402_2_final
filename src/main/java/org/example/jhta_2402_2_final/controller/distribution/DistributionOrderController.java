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
    public String placeOrder(@RequestParam("kitOrderId") String kitOrderId,
                             @RequestParam("sourceId") UUID sourceId,
                             @RequestParam("totalQuantity") int totalQuantity,
                             @RequestParam("sourcePrice") int sourcePrice,
                             @RequestParam("productCompanyId") UUID productCompanyId
                             ) {

        UUID sourcePriceId = distributionOrderService.selectSourcePriceId(sourceId, productCompanyId);


        int result = distributionOrderService.insertProductOrder(sourcePriceId, totalQuantity);

        if (result > 0) System.out.println("인서트 성공");
        else System.out.println("인서트 실패 ");

        return "redirect:/distribution/order";
    }






}
