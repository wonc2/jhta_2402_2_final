package org.example.jhta_2402_2_final.controller.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.distribution.RequestOrderDto;
import org.example.jhta_2402_2_final.service.distribution.KitOrderProcessService;
import org.example.jhta_2402_2_final.service.sales.SalesService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/distribution")
//@RequestMapping("/logistics")
public class KitOrderProcessController {

    private final KitOrderProcessService kitOrderProcessService;
    private final SalesService salesService;

    @GetMapping("/main")
    public String kitOrderProcessMainPage(Model model) {
        List<Map<String, Object>> kitOrderList = kitOrderProcessService.findNewOrders();
        model.addAttribute("kitOrderList", kitOrderList);

        List<Map<String, Object>> kitProcessedOrderList = kitOrderProcessService.findProcessedOrders();
        model.addAttribute("kitProcessedOrderList", kitProcessedOrderList);

        /*List<Map<String, Object>> kitSourceList = kitOrderProcessService.findKitSource();
        model.addAttribute("kitSourceList", kitSourceList);*/

        int newOrderCount = kitOrderProcessService.findNewOrders().size();
        model.addAttribute("newOrderCount", newOrderCount);

        int processedOrderCount = kitOrderProcessService.findProcessedOrders().size();
        model.addAttribute("processedOrderCount", processedOrderCount);


        return "distribution/KitOrderProcessMainPage";
    }

    /*@GetMapping("/main/processed")
    public String kitOrderProcessProcessedPage(Model model) {
        List<Map<String, Object>> kitProcessedOrderList = kitOrderProcessService.findProcessedOrders();
        model.addAttribute("kitProcessedOrderList", kitProcessedOrderList);

        return "distribution/KitOrderProcessMainPage";
    }*/

    @GetMapping("/kitOrderRecipe")
    @ResponseBody
    public List<Map<String, Object>> getKitOrderRecipe(@RequestParam("kitOrderID") String kitOrderId) {

        // KitOrderId를 이용해서 밀키트 아이디를 조회?
        // String mealKitId = kitOrderProcessService.findMealKitByKitOrderId(kitOrderId);
        String mealKitId = kitOrderProcessService.findMealKitByKitOrderId(kitOrderId);
        int orderQuantity = kitOrderProcessService.findOrderQuantityByKitOrderId(kitOrderId);

        // // 3. 재료 정보, 창고 재고, 최소 가격 및 공급업체 정보 가져오기
        // 인스턴스를 사용해 메소드 호출
        return kitOrderProcessService.findKitRecipeWithStockAndSupplier(mealKitId, orderQuantity);

    }

    @PostMapping("/kitOrderRelease")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> completeKitOrderRelease(@RequestBody Map<String, String> requestBody) {
        String kitOrderId = requestBody.get("kitOrderId");
        Map<String, Object> response = new HashMap<>();

        if (kitOrderId == null) {
            response.put("status", "error");
            response.put("message", "Required parameter 'kitOrderId' is not present.");
            return ResponseEntity.badRequest().body(response);
        }

        boolean success = kitOrderProcessService.processKitOrder(kitOrderId);

        if (success) {
            response.put("status", "success");
            response.put("message", "재료 출고가 성공적으로 완료되었습니다.");
        } else {
            response.put("status", "error");
            response.put("message", "재고가 부족하여 출고에 실패했습니다. 재고를 다시 확인하세요.");
        }

        return ResponseEntity.ok(response); // JSON 형식으로 응답
    }

    // ResponseEntity 를 사용한 재료발주요청 메서드
    @PostMapping("/requestSourceOrder")
    public ResponseEntity<?> requestSourceOrder(@RequestBody RequestOrderDto requestOrderDto) {

        System.out.println("Received Request: " + requestOrderDto);
        // 서비스 로직 호출 (발주 처리)
        boolean result = kitOrderProcessService.requestProductOrders(requestOrderDto);

        if (result) {
            return ResponseEntity.ok("Order successfully processed.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the order.");
        }
    }


}







