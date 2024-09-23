package org.example.jhta_2402_2_final.controller;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.sales.KitOrderDetailDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitOrderLogDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitSourceDetailDto;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final SalesService salesService;
    @GetMapping("/")
    public String home(Model model){

        // 현재 날짜를 가져옴
        LocalDate currentDate = LocalDate.now();

        // 년도와 달을 구함
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        model.addAttribute("currentYear", currentYear);
        model.addAttribute("currentMonth", currentMonth);


        //월 매출액
        int totalMonthSale = salesService.getTotalMonthSale(currentYear, currentMonth);

        // 숫자를 3자리마다 콤마로 구분하는 포맷 설정
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        String formattedMonthSale = numberFormat.format(totalMonthSale);

        model.addAttribute("totalMonthSale", formattedMonthSale);

        //연매출액
        int totalYearSale = salesService.getTotalYearSale(currentYear);
        String formattedYearSale = numberFormat.format(totalYearSale);
        model.addAttribute("totalYearSale", formattedYearSale);

        //처리중인 주문 개수
        int processingCount = salesService.getProcessCount();
        model.addAttribute("processingCount",processingCount);

        //처리완료된 주문 개수
        int completeCount = salesService.getCompleteCount();
        model.addAttribute("completeCount",completeCount);

        //업체별 월별 판매량
        List<Map<String, Object>> monthlySales = salesService.getMonthlySales();
        model.addAttribute("monthlySales",monthlySales);

        //업체별 누적 판매량, 판매금액
        List<Map<String, String>> totalQuantityByCompanyNameList = salesService.selectTotalQuantityByCompanyName();
        model.addAttribute("totalQuantityByCompanyNameList", totalQuantityByCompanyNameList);

        //밀키트와 해당 밀키트의 재료 가져오기
        List<KitSourceDetailDto> kitSourceDetails = salesService.getAllKitSourceDetail();
        model.addAttribute("kitSourceDetails", kitSourceDetails);

        //상세정보 조인해서 가져오기
        List<KitOrderDetailDto> kitOrderDetails = salesService.getAllKitOrderDetail();
        model.addAttribute("kitOrderDetails", kitOrderDetails);

        //업체명과 pk값 가져오기
        List<Map<String, String>> companyList = salesService.getKitCompanyIdAndNames();
        model.addAttribute("companyList",companyList);

        //밀키트명이랑 pk가져오기
        List<Map<String, Object>> mealkitList = salesService.getMealkitIdAndNames();
        model.addAttribute("mealkitList", mealkitList);

        //재료명이랑 pk가져오기
        List<Map<String,String>> sourceList = salesService.getSourceIdAndNames();
        model.addAttribute("sourceList", sourceList);

        //로그
        List<KitOrderLogDto> kitOrderLogs = salesService.getKitOrderLogs();
        model.addAttribute("kitOrderLogs", kitOrderLogs);

        return "index/index";
    }

    @GetMapping("/test")
    public String test(){
        return "test";
    }


}
