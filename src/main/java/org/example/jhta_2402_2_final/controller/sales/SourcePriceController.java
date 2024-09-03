package org.example.jhta_2402_2_final.controller.sales;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.sales.SourcePriceDto;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/sales/source-price")
@Controller
@Slf4j
public class SourcePriceController {

    private final SalesService salesService;

    @GetMapping
    public String sourcePrice(Model model){

        //업체별 재료 가격 셀렉
        List<SourcePriceDto> sourcePriceDtos = salesService.getAllSourcePrice();
        model.addAttribute("sourcePrice",sourcePriceDtos);

        //최소 가격 셀렉
        List<SourcePriceDto> minSourcePrice = salesService.getMinSourcePrice();
        model.addAttribute("minSourcePrice",minSourcePrice);
        return "sales/source";
    }
}
