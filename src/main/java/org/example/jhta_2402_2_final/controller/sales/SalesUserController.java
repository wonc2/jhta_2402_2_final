package org.example.jhta_2402_2_final.controller.sales;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.CustomUserDetails;
import org.example.jhta_2402_2_final.model.dto.salesUser.UserKitOrderDto;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.example.jhta_2402_2_final.service.sales.SalesUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/sales/user")
public class SalesUserController {

    final private SalesUserService salesUserService;
    final private SalesService salesService;

    @GetMapping
    public String salesUser(@AuthenticationPrincipal CustomUserDetails userDetails,
                            Model model) {
        String userId = userDetails.getMemberDto().getUserId();
        model.addAttribute("userId",userId);

        //로그인한 userId를 통해 kitCompanyId 가져오기
        UserKitOrderDto info = salesUserService.selectKitCompanyIdByUserId(userId);
        String kitCompanyId = info.getKitCompanyId();
        model.addAttribute("info",info);

        //해당 업체 주문 정보 가져오기
        List<UserKitOrderDto> list = salesUserService.selectKitOrderByKitCompanyId(kitCompanyId);
        model.addAttribute("list",list);

        //밀키트명이랑 pk가져오기
        List<Map<String, Object>> mealkitList = salesService.getMealkitIdAndNames();
        model.addAttribute("mealkitList", mealkitList);

        //밀키트 재고 가져오기
        List<Map<String, Object>> kitStorage = salesUserService.selectKitStorage(kitCompanyId);
        model.addAttribute("kitStorage", kitStorage);

        List<Integer> monthlyList = salesUserService.selectMonthly(kitCompanyId);
        System.out.println("monthlyList = >>>>>>>" + monthlyList);
        model.addAttribute("monthlyList",monthlyList);
        return "sales/user";
    }

    @PostMapping("/insert")
    public String insert(@ModelAttribute UserKitOrderDto dto) {
        salesUserService.insertKitOrder(dto);
        return "redirect:/sales/user";
    }

    @PostMapping("/cancel")
    public String cancel (@RequestParam UUID kitOrderId) {
        System.out.println("kitOrderId>>>>>>"+kitOrderId);
        salesService.updateKitOrderCancel(kitOrderId);
        return "redirect:/sales/user";
    }
}
