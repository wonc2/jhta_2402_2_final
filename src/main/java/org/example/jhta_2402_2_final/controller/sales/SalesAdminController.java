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
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();

        model.addAttribute("currentYear", currentYear);
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("totalMonthSale", formatNumber(salesService.getTotalMonthSale(currentYear, currentMonth)));
        model.addAttribute("totalYearSale", formatNumber(salesService.getTotalYearSale(currentYear)));
        model.addAttribute("processingCount", salesService.getProcessCount());
        model.addAttribute("completeCount", salesService.getCompleteCount());
        model.addAttribute("monthlySales", salesService.getMonthlySales());
        model.addAttribute("totalQuantityByCompanyNameList", salesService.selectTotalQuantityByCompanyName());
        model.addAttribute("kitSourceDetails", salesService.getAllKitSourceDetail());
        model.addAttribute("kitOrderDetails", salesService.getAllKitOrderDetail());
        model.addAttribute("companyList", salesService.getKitCompanyIdAndNames());
        model.addAttribute("mealkitList", salesService.getMealkitIdAndNames());
        model.addAttribute("sourceList", salesService.getSourceIdAndNames());
        model.addAttribute("kitOrderLogs", salesService.getKitOrderLogs());

        return "sales/admin";
    }

    private String formatNumber(int number) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.KOREA);
        return numberFormat.format(number);
    }

    @PostMapping("/insert")
    public String insert(@ModelAttribute KitOrderDto kitOrderDto, RedirectAttributes redirectAttributes) {
        String mealkitId = kitOrderDto.getMealkitId();
        KitPriceDto dto = salesService.getCurrentPriceAndMinPrice(mealkitId);

        if (!isPriceValid(dto)) {
            alter(redirectAttributes, "밀키트 가격이 변동되었습니다. 해당 밀키트는 현재 주문이 불가합니다.");
            return "redirect:/sales/admin";
        }

        handleKitOrderInsertion(kitOrderDto, redirectAttributes);
        return "redirect:/sales/admin";
    }

    private boolean isPriceValid(KitPriceDto dto) {
        return dto.getCurrentMealkitPrice() >= dto.getMinMealkitPrice();
    }

    private void handleKitOrderInsertion(KitOrderDto kitOrderDto, RedirectAttributes redirectAttributes) {
        int isSuccess = salesService.createKitOrder(kitOrderDto);
        UUID kitOrderId = kitOrderDto.getKitOrderId();

        if (isSuccess > 0) {
            salesService.insertKitOrderLog(kitOrderId);
            alter(redirectAttributes, "밀키트 주문이 추가되었습니다.");
        } else {
            alter(redirectAttributes, "밀키트 주문에 실패하였습니다.");
        }
    }

    @PostMapping("/update-status")
    public String changeKitOrderStatus(@RequestParam("kitOrderId") UUID kitOrderId,
                                       @RequestParam("statusId") int statusId,
                                       RedirectAttributes redirectAttributes) {
        if (!isOrderStatusValid(kitOrderId, redirectAttributes)) {
            return "redirect:/sales/admin";
        }

        salesService.updateKitOrderStatus(kitOrderId, statusId);
        salesService.updateKitStorage(kitOrderId);
        alter(redirectAttributes, "밀키트 주문이 처리완료 되었습니다.");
        return "redirect:/sales/admin/storage";
    }

    private boolean isOrderStatusValid(UUID kitOrderId, RedirectAttributes redirectAttributes) {
        String currentStatus = salesService.getKitOrderStatus(kitOrderId);
        if (!"출고".equals(currentStatus)) {
            alter(redirectAttributes, "출고 상태인 경우에만 처리 완료가 가능합니다. 주문 상태를 다시 확인하세요.");
            return false;
        }
        return true;
    }

    @PostMapping("/insert-mealkit")
    public String insertMealkit(@RequestParam("mealkitName") String mealkitName,
                                @RequestParam("sourceIds") List<String> sourceIds,
                                @RequestParam(value = "quantities", required = false) List<Integer> quantities,
                                RedirectAttributes redirectAttributes) {
        salesService.insertMealkit(mealkitName, sourceIds, quantities);
        alter(redirectAttributes, "새로운 밀키트가 등록되었습니다.");
        return "redirect:/sales/admin";
    }

    @GetMapping("/order/details")
    @ResponseBody
    public List<OrderDetailDto> getOrderDetails(@RequestParam("kitOrderId") UUID kitOrderId,
                                                @RequestParam("quantity") int quantity) {
        return salesService.getOrderDetails(kitOrderId, quantity);
    }

    @Transactional
    @PostMapping("/order/create")
    public String submitOrder(@RequestParam("kitOrderId") UUID kitOrderId,
                              @RequestParam("mealkitName") String mealkitName,
                              @RequestParam("quantity") int quantity,
                              @RequestParam("sourceNames") String sourceNamesJson,
                              @RequestParam("itemQuantities") String itemQuantitiesJson,
                              @RequestParam("stackQuantities") String stackQuantitiesJson,
                              @RequestParam("minPrices") String minPricesJson,
                              @RequestParam("companyNames") String companyNamesJson,
                              RedirectAttributes redirectAttributes) throws JsonProcessingException {

        processOrderData(sourceNamesJson, itemQuantitiesJson, stackQuantitiesJson, minPricesJson, companyNamesJson, kitOrderId);
        salesService.updateKitOrderStatus(kitOrderId, 2);
        alter(redirectAttributes, "발주 요청 되었습니다.");
        return "redirect:/sales/product/order";
    }

    private void processOrderData(String sourceNamesJson, String itemQuantitiesJson, String stackQuantitiesJson,
                                  String minPricesJson, String companyNamesJson, UUID kitOrderId) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String[] sourceNames = mapper.readValue(sourceNamesJson, String[].class);
        int[] itemQuantities = mapper.readValue(itemQuantitiesJson, int[].class);
        int[] stackQuantities = mapper.readValue(stackQuantitiesJson, int[].class);
        int[] minPrices = mapper.readValue(minPricesJson, int[].class);
        String[] companyNames = mapper.readValue(companyNamesJson, String[].class);

        salesService.processOrder(sourceNames, companyNames, itemQuantities, stackQuantities, minPrices, kitOrderId);
    }

    @PostMapping("/shinhyeok")
    public String shinhyeok(@RequestParam("kitOrderIdForSale") UUID kitOrderId,
                            @RequestParam("sourceNamesForSale") String sourceNamesJson,
                            @RequestParam("itemQuantitiesForSale") String itemQuantitiesJson,
                            RedirectAttributes redirectAttributes) throws IOException {

        List<String> sourceNames = parseJsonToList(sourceNamesJson, new TypeReference<List<String>>() {});
        List<Integer> itemQuantities = parseJsonToList(itemQuantitiesJson, new TypeReference<List<Integer>>() {});

        validateListsSize(sourceNames, itemQuantities);

        List<Map<String, Object>> combinedList = createCombinedList(sourceNames, itemQuantities);
        logisticsWareHouseService.updateStackBySourceName(combinedList);
        salesService.updateKitOrderStatus(kitOrderId, 8);
        salesService.insertKitOrderLog(kitOrderId);
        alter(redirectAttributes, "출고되었습니다.");

        return "redirect:/sales/admin";
    }

    private <T> List<T> parseJsonToList(String json, TypeReference<List<T>> typeReference) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, typeReference);
    }

    private void validateListsSize(List<?> sourceNames, List<?> itemQuantities) {
        if (sourceNames.size() != itemQuantities.size()) {
            throw new IllegalArgumentException("sourceNames와 itemQuantities의 크기가 다릅니다.");
        }
    }

    private List<Map<String, Object>> createCombinedList(List<String> sourceNames, List<Integer> itemQuantities) {
        List<Map<String, Object>> combinedList = new ArrayList<>();
        for (int i = 0; i < sourceNames.size(); i++) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("sourceName", sourceNames.get(i));
            itemMap.put("quantity", itemQuantities.get(i));
            combinedList.add(itemMap);
        }
        return combinedList;
    }

    @PostMapping("/cancel")
    public String cancel(@RequestParam UUID kitOrderId, RedirectAttributes redirectAttributes) {
        if (isOrderCancellable(kitOrderId, redirectAttributes)) {
            salesService.updateKitOrderCancel(kitOrderId);
            alter(redirectAttributes, "주문이 취소되었습니다.");
        }
        return "redirect:/sales/admin";
    }

    private boolean isOrderCancellable(UUID kitOrderId, RedirectAttributes redirectAttributes) {
        String currentStatus = salesService.getKitOrderStatus(kitOrderId);
        if ("처리완료".equals(currentStatus) || "취소".equals(currentStatus)) {
            alter(redirectAttributes, "이미 취소되었거나 처리완료된 주문입니다.");
            return false;
        }
        return true;
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
        return checkAvailability(() -> salesService.checkUserIdExists(userId));
    }

    @ResponseBody
    @GetMapping("/checkCompanyName")
    public String checkCompanyName(@RequestParam String companyName) {
        return checkAvailability(() -> salesService.checkCompanyNameExists(companyName));
    }

    @ResponseBody
    @GetMapping("/checkEmail")
    public String checkEmail(@RequestParam String email) {
        return checkAvailability(() -> salesService.checkEmailExists(email));
    }

    @ResponseBody
    @GetMapping("/checkTel")
    public String checkTel(@RequestParam String tel) {
        return checkAvailability(() -> salesService.checkTelExists(tel));
    }

    @ResponseBody
    @GetMapping("/checkAddress")
    public String checkAddress(@RequestParam String address) {
        return checkAvailability(() -> salesService.checkAddressExists(address));
    }

    private String checkAvailability(CheckExistsFunction checkExistsFunction) {
        boolean exists = checkExistsFunction.exists();
        return exists ? "duplicate" : "available";
    }

    @FunctionalInterface
    private interface CheckExistsFunction {
        boolean exists();
    }
}
