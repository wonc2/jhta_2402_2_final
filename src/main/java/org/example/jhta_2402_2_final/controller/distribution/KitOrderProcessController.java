package org.example.jhta_2402_2_final.controller.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.distribution.RequestOrderDto;
import org.example.jhta_2402_2_final.service.distribution.KitOrderProcessService;
import org.example.jhta_2402_2_final.service.sales.SalesService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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

    @MessageMapping("/kitOrderProcess/update")
    @SendTo("/topic/warehouse/update")
    public String sendUpdate() {
        return "connected";
    }

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

    /*
    * [code-review] requestBody의 경우 Map이 아닌 class로 만들어서 null 경우 에러를 출력하게 만들 수 있습니다.
    * */
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

        /*
         * [code-review] 아래와 같이 특정 함수를 실행하고 컨트롤러에서 실패여부를 판단하는 패턴이 보이는데
         * 해당 방법보다는 해당 메서드에서 런타임에러를 발생시키고 그 런타임 에러를 전역처리기 에서 처리하는 방법을 추천드립니다.
         * https://velog.io/@u-nij/Spring-%EC%A0%84%EC%97%AD-%EC%98%88%EC%99%B8-%EC%B2%98%EB%A6%AC-RestControllerAdivce-%EC%A0%81%EC%9A%A9
         * */
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







