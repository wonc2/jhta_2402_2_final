package org.example.jhta_2402_2_final.controller.sales;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.CustomUserDetails;
import org.example.jhta_2402_2_final.model.dto.sales.KitPriceDto;
import org.example.jhta_2402_2_final.model.dto.salesUser.UserKitOrderDto;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.example.jhta_2402_2_final.service.sales.SalesUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static org.example.jhta_2402_2_final.controller.sales.SalesAdminController.alter;

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
        model.addAttribute("userId", userId);

        // 로그인한 userId를 통해 kitCompanyId 가져오기
        UserKitOrderDto info = salesUserService.selectKitCompanyIdByUserId(userId);
        String kitCompanyId = info.getKitCompanyId();
        model.addAttribute("info", info);

        // 현재 날짜를 가져옴
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        model.addAttribute("currentYear", currentYear);
        model.addAttribute("currentMonth", currentMonth);

        // 월 매출액
        model.addAttribute("totalMonthSale", formatCurrency(salesUserService.getTotalMonthSale(currentYear, currentMonth, kitCompanyId)));

        // 연매출액
        model.addAttribute("totalYearSale", formatCurrency(salesUserService.getTotalYearSale(currentYear, kitCompanyId)));

        // 처리중인 주문 개수
        model.addAttribute("processingCount", salesUserService.getProcessCount(kitCompanyId));

        // 처리완료된 주문 개수
        model.addAttribute("completeCount", salesUserService.getCompleteCount(kitCompanyId));

        // 해당 업체 주문 정보 가져오기
        List<UserKitOrderDto> list = salesUserService.selectKitOrderByKitCompanyId(kitCompanyId);
        model.addAttribute("list", list);

        // 밀키트명이랑 pk 가져오기
        List<Map<String, Object>> mealkitList = salesService.getMealkitIdAndNames();
        model.addAttribute("mealkitList", mealkitList);

        // 밀키트 재고 가져오기
        List<Map<String, Object>> kitStorage = salesUserService.selectKitStorage(kitCompanyId);
        model.addAttribute("kitStorage", kitStorage);

        // 월별 매출 데이터 가져오기
        List<Integer> monthlyList = salesUserService.selectMonthly(kitCompanyId);
        model.addAttribute("monthlyList", monthlyList);

        return "sales/user";
    }

    private String formatCurrency(int amount) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        return numberFormat.format(amount);
    }

    @PostMapping("/insert")
    public String insert(@ModelAttribute UserKitOrderDto userKitOrderDto,
                         RedirectAttributes redirectAttributes) {
        String mealkitId = userKitOrderDto.getMealkitId();
        KitPriceDto dto = salesService.getCurrentPriceAndMinPrice(mealkitId);

        // 현재 가격과 최소 가격 비교
        if (dto.getCurrentMealkitPrice() < dto.getMinMealkitPrice()) {
            alter(redirectAttributes, "밀키트 가격이 변동되었습니다. 해당 밀키트는 현재 주문이 불가합니다.");
            return "redirect:/sales/user";
        }

        salesUserService.insertKitOrder(userKitOrderDto);
        alter(redirectAttributes, "새로운 주문이 추가되었습니다.");
        return "redirect:/sales/user";
    }

    @PostMapping("/cancel")
    public String cancel(@RequestParam UUID kitOrderId,
                         RedirectAttributes redirectAttributes) {
        salesService.updateKitOrderCancel(kitOrderId);
        alter(redirectAttributes, "주문이 취소되었습니다.");
        return "redirect:/sales/user";
    }
}
