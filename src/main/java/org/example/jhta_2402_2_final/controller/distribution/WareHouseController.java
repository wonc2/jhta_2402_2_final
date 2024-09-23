package org.example.jhta_2402_2_final.controller.distribution;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.distribution.CombineLogDto;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderDetailLogDto;
import org.example.jhta_2402_2_final.model.dto.distribution.LogisticsWareHouseDto;
import org.example.jhta_2402_2_final.model.dto.distribution.ProductOrderLogDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitOrderDetailDto;
import org.example.jhta_2402_2_final.model.dto.sales.SourcePriceDto;
import org.example.jhta_2402_2_final.model.enums.Status;
import org.example.jhta_2402_2_final.service.distribution.LogisticsWareHouseService;
//import org.example.jhta_2402_2_final.util.SmsUtil;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/wareHouse")
public class WareHouseController {

    private final LogisticsWareHouseService logisticsWareHouseService;
    private final SalesService salesService;

    //    private final SmsUtil smsUtil;
    @GetMapping("/selectAll")
    public String selectAll(Model model) {

        //최소 가격 셀렉
        List<SourcePriceDto> minSourcePrice = salesService.getMinSourcePrice();
        model.addAttribute("minSourcePrice", minSourcePrice);

        //상세정보 조인해서 가져오기
        List<KitOrderDetailDto> kitOrderDetails = salesService.getAllKitOrderDetail();
        model.addAttribute("kitOrderDetails", kitOrderDetails);

        logisticsWareHouseService.insertWarehouseStackForCompletedOrders();

        logisticsWareHouseService.deleteZeroQuantityRecords();

        List<LogisticsWareHouseDto> list = logisticsWareHouseService.selectAllLogisticsWarehouse();

        model.addAttribute("warehouseList", list);

        return "distribution/wareHouseList";
    }


    @PostMapping("/sales")
    public String sales(@RequestParam("kitOrderIdForSale") String kitOrderId,
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

        // 창고에서 차감 (FIFO 방식 적용)
        logisticsWareHouseService.updateStackBySourceName(combinedList);

        // KitOrder의 Status 수정
        salesService.updateKitOrderStatus(UUID.fromString(kitOrderId), Status.SHIPPED.getStatusId());

        // KitOrderLog 기입
        salesService.insertKitOrderLog(UUID.fromString(kitOrderId));

        Map<String,Object> map = new HashMap<>();
        return "redirect:/wareHouse/selectAll";
    }


    @GetMapping("/selectBySourceName")
    public String selectBySourceName(@RequestParam String keyword, Model model) {


        List<LogisticsWareHouseDto> list = logisticsWareHouseService.selectBySourceNameLogisticsWarehouse(keyword);

        model.addAttribute("warehouseList", list);
        return "distribution/wareHouseList";
    }


    @GetMapping("/selectLog")
    @ResponseBody
    public List<CombineLogDto> getWarehouseLog(@RequestParam String sourceId) {

        List<KitOrderDetailLogDto> kitOrderLogs = logisticsWareHouseService.selectKitOrderLogDetailsBySourceId(sourceId);
        List<ProductOrderLogDto> productOrderLogs = logisticsWareHouseService.selectProductOrderLogDetailsBySourceId(sourceId);


        List<CombineLogDto> combineLogs = new ArrayList<>();
        for (KitOrderDetailLogDto kitLog : kitOrderLogs) {
            combineLogs.add(new CombineLogDto(
                    kitLog.getKitOrderId(),
                    kitLog.getCompanyName(),
                    kitLog.getSourceName(),
                    kitLog.getSourceId(),
                    kitLog.getQuantity(),
                    kitLog.getOrderDate(),
                    kitLog.getStatus()
            ));
        }
        for (ProductOrderLogDto productLog : productOrderLogs) {
            combineLogs.add(new CombineLogDto(
                    productLog.getProductOrderId(),
                    productLog.getCompanyName(),
                    productLog.getSourceName(),
                    productLog.getSourceId(),
                    productLog.getQuantity(),
                    productLog.getOrderDate(),
                    productLog.getStatus()
            ));
        }

        Collections.sort(combineLogs, (o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()));

//        Collections.sort(combineLogs, new Comparator<CombineLogDto>() {
//            @Override
//            public int compare(CombineLogDto o1, CombineLogDto o2) {
//                return o2.getOrderDate().compareTo(o1.getOrderDate());
//            }
//        });

        return combineLogs;

    }


    @PostMapping("/singleOrder")
    public String singleOrder(@RequestParam("companyName") String companyName,
                              @RequestParam("sourceName") String sourceName,
                              @RequestParam("price") String price,
                              @RequestParam("quantity") int quantity) {

        Map<String, Object> map = new HashMap<>();
        map.put("companyName", companyName);
        map.put("sourceName", sourceName);
        map.put("price", price);
        map.put("quantity", quantity);

        logisticsWareHouseService.insertProductOrder(map);

        return "redirect:/wareHouse/selectAll";

    }





//    @GetMapping("/sendMSG")
//    @ResponseBody
//    public void sendMSG() {
//        smsUtil.sendOne();
//    }


}
