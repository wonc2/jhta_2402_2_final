package org.example.jhta_2402_2_final.controller.distribution;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.distribution.LogisticsWareHouseDto;
import org.example.jhta_2402_2_final.service.distribution.LogisticsWareHouseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/wareHouse")
public class WareHouseController {

    private final LogisticsWareHouseService logisticsWareHouseService;


    @GetMapping("/selectAll")
    public String selectAll(Model model) {

        // 프러덕트 오더 테이블에서 스테이터스가 3인 경우 창고에 적재하고 3 -> 5로 바꿈
        int result = logisticsWareHouseService.insertWarehouseStackForCompletedOrders();
        if (result > 0) {
            logisticsWareHouseService.updateProductOrderStatus();
        }

        // 밀키트 오더 테이블에서 스테이터스가 3인 경우 창고에서 차감하고 3->6으로 바꿈
       /* List<Map<String, Object>> requiredStackList = logisticsWareHouseService.selectRequiredStack();
        if (!requiredStackList.isEmpty()) {
            for (Map<String, Object> list : requiredStackList) {
                String sourceFk = (String) list.get("sourceFk");
                BigDecimal totalQuantity = (BigDecimal) list.get("totalQuantity");
                int quantity = totalQuantity.intValue();
                System.out.println("ddddddddddddddd>>>>>>>>>>" + sourceFk);
                System.out.println("ddddddddddddddddd >>>>>>" + quantity);
                Map<String, Object> params = new HashMap<>();
                params.put("sourceFk", sourceFk);
                params.put("quantity", quantity);

                logisticsWareHouseService.updateStackFirstRecord(params);
            }
            //logisticsWareHouseService.updateKitOrderStatus();
        }*/


        logisticsWareHouseService.deleteZeroQuantityRecords();

        List<LogisticsWareHouseDto> list = logisticsWareHouseService.selectAllLogisticsWarehouse();

        model.addAttribute("warehouseList", list);

        return "distribution/wareHouseList";
    }

    @GetMapping("/selectLog")
    @ResponseBody
    public List<Map<String, Object>> getWarehouseLog(@RequestParam String sourceUUID) {
        // sourceUUID를 이용해 데이터베이스에서 관련 데이터를 조회합니다.
        // 예시로 List<Map<String, Object>> 타입의 데이터를 반환한다고 가정합니다.
        return logisticsWareHouseService.selectKitOrderLogDetailsBySourceId(sourceUUID);
    }


}
