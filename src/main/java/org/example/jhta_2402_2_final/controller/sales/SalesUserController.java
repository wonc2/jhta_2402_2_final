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
        model.addAttribute("userId",userId);


        //로그인한 userId를 통해 kitCompanyId 가져오기
        UserKitOrderDto info = salesUserService.selectKitCompanyIdByUserId(userId);
        String kitCompanyId = info.getKitCompanyId();
        model.addAttribute("info",info);

        // 현재 날짜를 가져옴
        LocalDate currentDate = LocalDate.now();

        // 년도와 달을 구함
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        model.addAttribute("currentYear", currentYear);
        model.addAttribute("currentMonth", currentMonth);


        //월 매출액
        int totalMonthSale = salesUserService.getTotalMonthSale(currentYear, currentMonth, kitCompanyId);

        // 숫자를 3자리마다 콤마로 구분하는 포맷 설정
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        String formattedMonthSale = numberFormat.format(totalMonthSale);

        model.addAttribute("totalMonthSale", formattedMonthSale);

        //연매출액
        int totalYearSale = salesUserService.getTotalYearSale(currentYear, kitCompanyId);
        String formattedYearSale = numberFormat.format(totalYearSale);
        model.addAttribute("totalYearSale", formattedYearSale);

        //처리중인 주문 개수
        int processingCount = salesUserService.getProcessCount(kitCompanyId);
        model.addAttribute("processingCount",processingCount);

        //처리완료된 주문 개수
        int completeCount = salesUserService.getCompleteCount(kitCompanyId);
        model.addAttribute("completeCount",completeCount);

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
    public String insert(@ModelAttribute UserKitOrderDto userKitOrderDto,
                         RedirectAttributes redirectAttributes) {
        System.out.println("kit order insert" +userKitOrderDto);
        String mealkitId = userKitOrderDto.getMealkitId();

        KitPriceDto dto = salesService.getCurrentPriceAndMinPrice(mealkitId);

        int currentPrice = dto.getCurrentMealkitPrice();
        int minPrice = dto.getMinMealkitPrice();

        //현재 가격이 최소가격보다 낮을경우 판매 불가능 하도록
        if (currentPrice < minPrice) {
            alter(redirectAttributes, "밀키트 가격이 변동되었습니다. 해당 밀키트는 현재 주문이 불가합니다.");
            return "redirect:/sales/user";
        }

        salesUserService.insertKitOrder(userKitOrderDto);
        alter(redirectAttributes, "새로운 주문이 추가되었습니다.");
        return "redirect:/sales/user";
    }

    @PostMapping("/cancel")
    public String cancel (@RequestParam UUID kitOrderId,
                          RedirectAttributes redirectAttributes) {
        salesService.updateKitOrderCancel(kitOrderId);
        alter(redirectAttributes,"주문이 취소되었습니다.");
        return "redirect:/sales/user";
    }
}
