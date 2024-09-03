package org.example.jhta_2402_2_final.controller.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderProcessDto;
import org.example.jhta_2402_2_final.service.distribution.KitOrderProcessService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/distribution")
//@RequestMapping("/logistics")
public class KitOrderProcessController<Order> {

    private final KitOrderProcessService kitOrderProcessService;

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
        return kitOrderProcessService.findKitRecipe(kitOrderId);
    }











    @PostMapping("/kitSourceCheckAndOrder")
    public String processOrder(@RequestParam("modalKitOrderID") String kitOrderId,
                               @RequestParam("modalKitCompanyName") String companyName,
                               @RequestParam("modalKitName") String kitName,
                               @RequestParam("modalQuantity") String quantity,
                               @RequestParam("modalDate") String orderDate,
                               @RequestParam("kitIngredients") List<String> ingredients, // 재료 목록
                               @RequestParam("kitIngredientQuantities") List<String> ingredientQuantities, // 재료 수량 목록
                               Model model) {
        // 콘솔에 로그를 찍거나, 데이터 처리만 수행
        log.info("Order ID: {}", kitOrderId);
        log.info("Company Name: {}", companyName);
        log.info("Kit Name: {}", kitName);
        log.info("Quantity: {}", quantity);
        log.info("Order Date: {}", orderDate);

        // 재료 및 재료 수량 로그
        for (int i = 0; i < ingredients.size(); i++) {
            log.info("Ingredient: {} - Quantity: {}", ingredients.get(i), ingredientQuantities.get(i));
        }

        // 모델에 추가할 데이터
        model.addAttribute("orderId", kitOrderId);
        model.addAttribute("companyName", companyName);
        model.addAttribute("kitName", kitName);
        model.addAttribute("quantity", quantity);
        model.addAttribute("orderDate", orderDate);
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("ingredientQuantities", ingredientQuantities);
        model.addAttribute("message", "Order processed successfully!");

        // 결과를 표시할 뷰로 리다이렉트
        return "redirect:/distribution/main"; // 주문 내역 페이지로 리다이렉트
    }

    @GetMapping("/order-details")
    @ResponseBody
    public ResponseEntity<List<KitOrderProcessDto>> getOrderDetails(@RequestParam("orderKeyword") String orderKeyword) {
        List<KitOrderProcessDto> orders = kitOrderProcessService.findOrdersByKeyword(orderKeyword);
        return ResponseEntity.ok(orders);
    }

}







