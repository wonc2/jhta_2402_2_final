package org.example.jhta_2402_2_final.controller.sales;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.sales.KitPriceDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitSourceDetailDto;
import org.example.jhta_2402_2_final.model.dto.sales.SourcePriceDto;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/sales/source-price")
@Controller
@Slf4j
public class SourcePriceController {

    private final SalesService salesService;

    @GetMapping
    public String sourcePrice(Model model){

        //밀키트와 해당 밀키트의 재료 가져오기
        List<KitSourceDetailDto> kitSourceDetails = salesService.getAllKitSourceDetail();
        model.addAttribute("kitSourceDetails", kitSourceDetails);

        //업체별 재료 가격 셀렉
        List<SourcePriceDto> sourcePriceDtos = salesService.getAllSourcePrice();
        model.addAttribute("sourcePrice",sourcePriceDtos);

        //최소 가격 셀렉
        List<SourcePriceDto> minSourcePrice = salesService.getMinSourcePrice();
        model.addAttribute("minSourcePrice",minSourcePrice);

        //밀키트 별 최소 가격
        List<KitPriceDto> minKitPrice = salesService.getMinKitPrice();
        model.addAttribute("minKitPrice",minKitPrice);

        return "sales/source";
    }

    @PostMapping("/updateKitPrice")
    public String updateKitPrice(@RequestParam("mealkitId") String mealkitId,
                                 @RequestParam("minPrice") int minPrice){

        salesService.updateKitPrice(mealkitId, minPrice);
        return "redirect:/sales/source-price";
    }
}
