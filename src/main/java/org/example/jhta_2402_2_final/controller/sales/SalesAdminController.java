package org.example.jhta_2402_2_final.controller.sales;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.sales.*;
import org.example.jhta_2402_2_final.service.distribution.LogisticsWareHouseService;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/sales/admin")
@Controller
@Slf4j
public class SalesAdminController {

    private final SalesService salesService;
    private final LogisticsWareHouseService logisticsWareHouseService;

    public static void alter(RedirectAttributes redirectAttributes, String content) {
        redirectAttributes.addFlashAttribute("message", content);
    }

    @GetMapping
    public String salesMain(Model model) {

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

        return "sales/admin";
    }

    @PostMapping("/insert")
    public String insert(@ModelAttribute KitOrderDto kitOrderDto,
                         RedirectAttributes redirectAttributes) {
        String mealkitId = kitOrderDto.getMealkitId();

        KitPriceDto dto = salesService.getCurrentPriceAndMinPrice(mealkitId);

        int currentPrice = dto.getCurrentMealkitPrice();
        int minPrice = dto.getMinMealkitPrice();

        //현재 가격이 최소가격보다 낮을경우 판매 불가능 하도록
        if (currentPrice < minPrice) {
            alter(redirectAttributes, "밀키트 가격이 변동되었습니다. 해당 밀키트는 현재 주문이 불가합니다.");
            return "redirect:/sales/admin";
        }


        int isSuccess = salesService.createKitOrder(kitOrderDto);

        UUID kitOrderId = kitOrderDto.getKitOrderId();
        if (isSuccess > 0) {
            salesService.insertKitOrderLog(kitOrderId);
            alter(redirectAttributes, "밀키트 주문이 추가되었습니다.");
        } else {
            alter(redirectAttributes,"밀키트 주문에 실패하였습니다.");
        }

        return "redirect:/sales/admin";
    }

    // 상태 변경
    @PostMapping("/update-status")
    public String changeKitOrderStatus(
            @RequestParam("kitOrderId") UUID kitOrderId,
            @RequestParam("statusId") int statusId,
            RedirectAttributes redirectAttributes) {

        // 현재 상태 확인
        String currentStatus = salesService.getKitOrderStatus(kitOrderId);

        // 출고상태가 아닌경우 처리완료 못하도록
        if (!"출고".equals(currentStatus)) {
            alter(redirectAttributes, "출고 상태인 경우에만 처리 완료가 가능합니다. 주문 상태를 다시 확인하세요.");
            return "redirect:/sales/admin";
        }


        salesService.updateKitOrderStatus(kitOrderId, statusId);
        salesService.updateKitStorage(kitOrderId);

        alter(redirectAttributes,"밀키트 주문이 처리완료 되었습니다.");

        return "redirect:/sales/admin/storage";
    }

    @PostMapping("/insert-mealkit")
    public String insertMealkit(
            @RequestParam("mealkitName") String mealkitName,
            @RequestParam("sourceIds") List<String> sourceIds,
            @RequestParam(value = "quantities", required = false) List<Integer> quantities,
            RedirectAttributes redirectAttributes) {

        // 서비스에서 밀키트 추가 로직 호출
        salesService.insertMealkit(mealkitName, sourceIds, quantities);
        alter(redirectAttributes,"새로운 밀키트가 등록되었습니다.");

        return "redirect:/sales/admin";  // 완료 후 목록 페이지로 리다이렉트
    }

    //주문별 최소 재료값 포함한 상세정보
    @GetMapping("/order/details")
    @ResponseBody
    public List<OrderDetailDto> getOrderDetails(@RequestParam("kitOrderId") UUID kitOrderId,
                                                @RequestParam("quantity") int quantity) {

        return salesService.getOrderDetails(kitOrderId, quantity);
    }


    @Transactional
    @PostMapping("/order/create")
    public String submitOrder(
            @RequestParam("kitOrderId") UUID kitOrderId,
            @RequestParam("mealkitName") String mealkitName,
            @RequestParam("quantity") int quantity,
            @RequestParam("sourceNames") String sourceNamesJson,
            @RequestParam("itemQuantities") String itemQuantitiesJson,
            @RequestParam("stackQuantities") String stackQuantitiesJson,
            @RequestParam("minPrices") String minPricesJson,
            @RequestParam("companyNames") String companyNamesJson,
            RedirectAttributes redirectAttributes) throws JsonProcessingException {

        // JSON 문자열을 배열로 변환
        ObjectMapper mapper = new ObjectMapper();

        String[] sourceNames = mapper.readValue(sourceNamesJson, String[].class);
        int[] itemQuantities = mapper.readValue(itemQuantitiesJson, int[].class);
        int[] stackQuantities = mapper.readValue(stackQuantitiesJson, int[].class);
        int[] minPrices = mapper.readValue(minPricesJson, int[].class);
        String[] companyNames = mapper.readValue(companyNamesJson, String[].class);

        salesService.processOrder(sourceNames, companyNames, itemQuantities, stackQuantities, minPrices, kitOrderId);
        salesService.updateKitOrderStatus(kitOrderId, 2);

        alter(redirectAttributes, "발주 요청 되었습니다.");

        return "redirect:/wareHouse/selectAll";
    }

    @PostMapping("/shinhyeok")
    public String shinhyeok(@RequestParam("kitOrderIdForSale") UUID kitOrderId,
                            @RequestParam("sourceNamesForSale") String sourceNamesJson,
                            @RequestParam("itemQuantitiesForSale") String itemQuantitiesJson,
                            RedirectAttributes redirectAttributes) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        // JSON 문자열을 List로 변환
        List<String> sourceNames = objectMapper.readValue(sourceNamesJson, new TypeReference<List<String>>() {});
        List<Integer> itemQuantities = objectMapper.readValue(itemQuantitiesJson, new TypeReference<List<Integer>>() {});

        // 검증: 두 리스트의 크기가 동일한지 확인
        if (sourceNames.size() != itemQuantities.size()) {
            throw new IllegalArgumentException("sourceNames와 itemQuantities의 크기가 다릅니다.");
        }

        // List<Map<String, Object>>로 변환
        List<Map<String, Object>> combinedList = new ArrayList<>();
        for (int i = 0; i < sourceNames.size(); i++) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("sourceName", sourceNames.get(i));
            itemMap.put("quantity", itemQuantities.get(i));
            combinedList.add(itemMap);
        }

        // 데이터 저장을 위한 Map 생성
        Map<String, Object> map = new HashMap<>();
        map.put("kitOrderId", kitOrderId);
        map.put("combinedList", combinedList);

        // 창고에서 차감
        logisticsWareHouseService.updateStackBySourceName(combinedList);

        // KitOrder의 Status 수정
        salesService.updateKitOrderStatus(kitOrderId, 8);

        // KitOrderLog 기입
        salesService.insertKitOrderLog(kitOrderId);

        alter(redirectAttributes, "출고되었습니다.");

        return "redirect:/sales/admin";
    }

    @PostMapping("/cancel")
    public String cancel (@RequestParam UUID kitOrderId,
                          RedirectAttributes redirectAttributes) {
        // 현재 상태 확인
        String currentStatus = salesService.getKitOrderStatus(kitOrderId);

        if ("처리완료".equals(currentStatus) || "취소".equals(currentStatus)) {
            alter(redirectAttributes, "이미 취소되었거나 처리완료된 주문입니다.");
            return "redirect:/sales/admin";
        }

        salesService.updateKitOrderCancel(kitOrderId);
        alter(redirectAttributes, "주문이 취소되었습니다.");
        return "redirect:/sales/admin";
    }

    @PostMapping("/insert/company")
    public String insertCompany(@ModelAttribute InsertKitCompanyDto insertKitCompanyDto,
                                RedirectAttributes redirectAttributes) {

        salesService.insertKitCompany(insertKitCompanyDto);
        alter(redirectAttributes, "새로운 업체가 등록되었습니다.");
        return "redirect:/sales/admin";
    }

    @ResponseBody
    @GetMapping("/checkUserId")
    public String checkUserId(@RequestParam String userId) {
        boolean exists = salesService.checkUserIdExists(userId);  // 중복 여부 확인 로직
        return exists ? "duplicate" : "available";
    }

    @ResponseBody
    @GetMapping("/checkCompanyName")
    public String checkCompanyName(@RequestParam String companyName) {
        System.out.println("companyName =>>>>>>> " + companyName);
        boolean exists = salesService.checkCompanyNameExists(companyName);  // 중복 여부 확인 로직
        return exists ? "duplicate" : "available";
    }

    @ResponseBody
    @GetMapping("/checkEmail")
    public String checkEmail(@RequestParam String email) {
        boolean exists = salesService.checkEmailExists(email);  // 이메일 중복 여부 확인 로직
        return exists ? "duplicate" : "available";
    }

    @ResponseBody
    @GetMapping("/checkTel")
    public String checkTel(@RequestParam String tel) {
        boolean exists = salesService.checkTelExists(tel);
        return exists ? "duplicate" : "available";
    }

    @ResponseBody
    @GetMapping("/checkAddress")
    public String checkAddress(@RequestParam String address) {
        boolean exists = salesService.checkAddressExists(address);
        return exists ? "duplicate" : "available";
    }


}
