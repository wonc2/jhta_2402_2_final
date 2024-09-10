package org.example.jhta_2402_2_final.controller.sales;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.sales.KitCompletedDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitOrderDetailDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitOrderLogDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitStorageDto;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sales/storage")
public class StorageController {
    private final SalesService salesService;

    @GetMapping
    public String storage(Model model) {

        //밀키트별 누적 판매량
        List<Map<String, String>> salesKit = salesService.selectKitTotalQuantity();
        model.addAttribute("salesKit", salesKit);

        //밀키트별 창고 재고
        List<Map<String, String>> kitTotalQuantity = salesService.selectKitStorageTotalQuantity();
        model.addAttribute("kitTotalQuantity", kitTotalQuantity);

        //밀키트명이랑 pk가져오기
        List<Map<String, Object>> mealkitList = salesService.getMealkitIdAndNames();
        model.addAttribute("mealkitList", mealkitList);

        //창고
        List<KitCompletedDto> storage = salesService.getKitStorages();
        model.addAttribute("storage", storage);

        //처리 완료된 애들만 가져오기
        List<KitOrderLogDto> allCompleted = salesService.getAllCompleted();
        model.addAttribute("allCompleted",allCompleted);

        return "sales/storage";
    }
}
