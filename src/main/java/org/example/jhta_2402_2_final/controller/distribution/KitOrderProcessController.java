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


        //return kitOrderProcessService.findKitRecipeWithStock(kitOrderId);

        // return kitOrderProcessService.findKitRecipe(kitOrderId); // 이 리턴 부분은 바뀔수도 있을 것 같음
    }

    @PostMapping("/kitOrderRelease")
    public String completeKitOrderRelease(@RequestParam("kitOrderId") String kitOrderId) {
        // 나중에 재고관련된 동시성 처리도 구현해야 함
        boolean success = kitOrderProcessService.processKitOrder(kitOrderId);

        if (success) {
            return "redirect:/distribution/main?success=true";
        } else {
            return "redirect:/distribution/main?error=insufficientStock";
        }
    }

    /*@PostMapping("/requestSourceOrder")
    @ResponseBody
    public ResponseEntity<String> requestProductOrder(@RequestBody Map<String, Object> orderData) {
        String kitOrderId = (String) orderData.get("kitOrderId");
        List<Map<String, Object>> orderDetails = (List<Map<String, Object>>) orderData.get("orderDetails");

        // 서비스 호출
        kitOrderProcessService.requestProductOrders(kitOrderId, orderDetails);

        // 요청이 성공했음을 알리는 메시지 반환
        return ResponseEntity.ok("재료 발주 요청이 완료되었습니다.");
    }*/

    /*@PostMapping("/requestSourceOrder")
    public String requestProductOrder(@RequestParam Map<String, Object> orderData, Model model) {
        String kitOrderId = (String) orderData.get("kitOrderId");
        List<Map<String, Object>> orderDetails = (List<Map<String, Object>>) orderData.get("orderDetails");

        // 서비스 호출
        kitOrderProcessService.requestProductOrders(kitOrderId, orderDetails);

        // 처리 후 리다이렉트 (성공 페이지나 메인 페이지로)
        return "redirect:/distribution/main";
    }*/

    // 구버전 requestProductOrder 매소드
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



    // form.submit()받는 컨트롤러 메소드
    /*@PostMapping("/requestSourceOrder")
    public String requestSourceOrder(
            @RequestParam("kitOrderId") String kitOrderId,
            @RequestParam("sourceName[]") List<String> sourceNames,
            @RequestParam("supplierName[]") List<String> supplierNames,
            @RequestParam("minPrice[]") List<Integer> minPrices,
            @RequestParam("insufficientQuantity[]") List<Integer> insufficientQuantities) {

        // 디버깅 출력
        boolean success = kitOrderProcessService.requestProductOrders(kitOrderId, sourceNames, supplierNames, minPrices, insufficientQuantities);

        // 요청에 대한 비즈니스 로직 처리

        if (success) {
            return "redirect:/distribution/main?success=true";
        } else {
            return "redirect:/distribution/main?error=insufficientStock";
        }
    }*/











}







