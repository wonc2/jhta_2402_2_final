package org.example.jhta_2402_2_final.controller.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderProcessDto;
import org.example.jhta_2402_2_final.model.dto.distribution.OrderDetail;
import org.example.jhta_2402_2_final.model.dto.distribution.RequestOrderDto;
import org.example.jhta_2402_2_final.service.distribution.KitOrderProcessService;
import org.example.jhta_2402_2_final.service.sales.SalesService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.PanelUI;
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
        List<Map<String, Object>> kitOrderList = kitOrderProcessService.findAllOrder();
        model.addAttribute("kitOrderList", kitOrderList);

        /*List<Map<String, Object>> kitSourceList = kitOrderProcessService.findKitSource();
        model.addAttribute("kitSourceList", kitSourceList);*/

        return "distribution/KitOrderProcessMainPage";
    }

    /*@GetMapping("/kitOrderRecipe")
    @ResponseBody
    public String getKitOrderRecipe(@RequestParam("kitOrderID") String kitOrderId, Model model) {
        List<Map<String, Object>> kitRecipeList = kitOrderProcessService.findKitRecipe(kitOrderId);
        model.addAttribute("kitRecipeList", kitRecipeList);

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

    // 구버전 재료판매 컨트롤러
    /*@PostMapping("/kitOrderRelease")
    public String completeKitOrderRelease(@RequestParam("kitOrderId") String kitOrderId) {
        // 나중에 재고관련된 동시성 처리도 구현해야 함
        boolean success = kitOrderProcessService.processKitOrder(kitOrderId);

        if (success) {
            return "redirect:/distribution/main?success=true";
        } else {
            return "redirect:/distribution/main?error=insufficientStock";
        }
    }*/

    // AJAX 처리한 재료판매 컨트롤러 메소드
    /*@PostMapping("/kitOrderRelease")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> completeKitOrderRelease(@RequestParam("kitOrderId") String kitOrderId) {
        boolean success = kitOrderProcessService.processKitOrder(kitOrderId);
        Map<String, Object> response = new HashMap<>();

        if (success) {
            response.put("status", "success");
            response.put("message", "재료 출고가 성공적으로 완료되었습니다.");
        } else {
            response.put("status", "error");
            response.put("message", "재고가 부족하여 출고에 실패했습니다. 재고를 다시 확인하세요.");
        }

        return ResponseEntity.ok(response); // JSON 형식으로 응답을 보냄
    }*/

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







