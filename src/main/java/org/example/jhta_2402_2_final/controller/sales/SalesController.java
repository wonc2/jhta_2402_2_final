package org.example.jhta_2402_2_final.controller.sales;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.Employee;
import org.example.jhta_2402_2_final.model.dto.sales.*;
import org.example.jhta_2402_2_final.service.distribution.LogisticsWareHouseService;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@RequestMapping("/sales")
@Controller
@Slf4j
public class SalesController {

    private final SalesService salesService;
    private final LogisticsWareHouseService logisticsWareHouseService;

    @GetMapping
    public String salesMain(Model model) {

        //업체별 월별 판매량
        List<Map<String, Object>> monthlySales = salesService.getMonthlySales();
        model.addAttribute("monthlySales",monthlySales);


        //업체별 누적 판매량, 판매금액
        List<Map<String, String>> totalQuantityByCompanyNameList = salesService.selectTotalQuantityByCompanyName();
        model.addAttribute("totalQuantityByCompanyNameList", totalQuantityByCompanyNameList);

        //밀키트와 해당 밀키트의 재료 가져오기
        List<KitSourceDetailDto> kitSourceDetails = salesService.getAllKitSourceDetail();
        model.addAttribute("kitSourceDetails", kitSourceDetails);

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

        //재료명이랑 pk가져오기
        List<Map<String,String>> sourceList = salesService.getSourceIdAndNames();
        model.addAttribute("sourceList", sourceList);

        //로그
        List<KitOrderLogDto> kitOrderLogs = salesService.getKitOrderLogs();
        model.addAttribute("kitOrderLogs", kitOrderLogs);

        return "sales/admin";
    }

    @PostMapping("/insert")
    public String insert(@ModelAttribute KitOrderDto kitOrderDto) {
        salesService.createKitOrder(kitOrderDto);

        UUID kitOrderId = kitOrderDto.getKitOrderId();
        salesService.insertKitOrderLog(kitOrderId);
        return "redirect:/sales";
    }

    // 상태 변경
    @PostMapping("/update-status")
    public String changeKitOrderStatus(
            @RequestParam("kitOrderId") UUID kitOrderId,
            @RequestParam("statusId") int statusId) {

        salesService.updateKitOrderStatus(kitOrderId, statusId);

        //만약 상태 아이디가 3인 경우에 재고 테이블에 해당 밀키트 추가
        if (statusId == 3) {
            salesService.updateKitStorage(kitOrderId);
            return "redirect:/sales/storage";
        }

        return "redirect:/sales";
    }

    @PostMapping("/insert-mealkit")
    public String insertMealkit(
            @RequestParam("mealkitName") String mealkitName,
            @RequestParam("sourceIds") List<String> sourceIds,
            @RequestParam(value = "quantities", required = false) List<Integer> quantities) {

        // 서비스에서 밀키트 추가 로직 호출
        salesService.insertMealkit(mealkitName, sourceIds, quantities);

        return "redirect:/sales";  // 완료 후 목록 페이지로 리다이렉트
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
            @RequestParam("companyNames") String companyNamesJson) throws JsonProcessingException {

        // JSON 문자열을 배열로 변환
        ObjectMapper mapper = new ObjectMapper();

        String[] sourceNames = mapper.readValue(sourceNamesJson, String[].class);
        int[] itemQuantities = mapper.readValue(itemQuantitiesJson, int[].class);
        int[] stackQuantities = mapper.readValue(stackQuantitiesJson, int[].class);
        int[] minPrices = mapper.readValue(minPricesJson, int[].class);
        String[] companyNames = mapper.readValue(companyNamesJson, String[].class);

        salesService.processOrder(sourceNames, companyNames, itemQuantities, stackQuantities, minPrices, kitOrderId);
        salesService.updateKitOrderStatus(kitOrderId, 2);

        return "redirect:/sales/product/order";
    }

    @PostMapping("/shinhyeok")
    public String shinhyeok(@RequestParam("kitOrderIdForSale") UUID kitOrderId,
                            @RequestParam("sourceNamesForSale") String sourceNamesJson,
                            @RequestParam("itemQuantitiesForSale") String itemQuantitiesJson) throws IOException {

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

        return "redirect:/sales";
    }

    @PostMapping("/cancel")
    public String cancel (@RequestParam UUID kitOrderId) {
        salesService.updateKitOrderCancel(kitOrderId);
        return "redirect:/sales";
    }

    @PostMapping("/insert/company")
    public String insertCompany(@RequestParam String companyName,
                                @RequestParam String companyAddress) {
        salesService.insertKitCompany(companyName, companyAddress);
        return "redirect:/sales";
    }



}
