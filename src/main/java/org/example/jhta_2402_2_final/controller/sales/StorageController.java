package org.example.jhta_2402_2_final.controller.sales;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.sales.KitOrderDetailDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitStorageDto;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sales/storage")
public class StorageController {
    private final SalesService salesService;

    @GetMapping
    public String storage(Model model) {
        //창고
        List<KitStorageDto> storage = salesService.getKitStorages();
        model.addAttribute("storage", storage);

        //처리 완료된 애들만 가져오기
        List<KitOrderDetailDto> allCompleted = salesService.getAllCompleted();
        model.addAttribute("allCompleted",allCompleted);

        return "sales/storage";
    }
}
