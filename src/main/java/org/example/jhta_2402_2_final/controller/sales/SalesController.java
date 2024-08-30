package org.example.jhta_2402_2_final.controller.sales;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.Employee;
import org.example.jhta_2402_2_final.model.dto.sales.KitOrderDetailDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitOrderDto;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/sales")
@Controller
public class SalesController {

    private final SalesService salesService;

    @GetMapping
    public String salesMain(Model model) {

        //디비 테이블 그대로 가져오기
        List<KitOrderDto> kitOrders = salesService.getAllKitOrder();
        model.addAttribute("kitOrders", kitOrders);

        //상세정보 조인해서 가져오기
        List<KitOrderDetailDto> kitOrderDetails = salesService.getAllKitOrderDetail();
        model.addAttribute("kitOrderDetails", kitOrderDetails);

        //업체명과 pk값 가져오기
        List<Map<String, String>> companyList = salesService.getKitCompanyIdAndNames();
        model.addAttribute("companyList",companyList);

        //밀키트명이랑 pk가져오기
        List<Map<String, Object>> mealkitList = salesService.getMealkitIdAndNames();
        model.addAttribute("mealkitList", mealkitList);

        return "sales/admin";
    }

    @PostMapping("/insert")
    public String insert(@ModelAttribute KitOrderDto kitOrderDto) {
        salesService.createKitOrder(kitOrderDto);
        return "redirect:/sales";
    }

    @PostMapping("/update-status")
    public String changeKitOrderStatus(
            @RequestParam("kitOrderId") String kitOrderId,
            @RequestParam("statusId") int statusId) {

        // 서비스 호출하여 상태 업데이트
        salesService.updateKitOrderStatus(kitOrderId, statusId);

        return "redirect:/sales";
    }
}
