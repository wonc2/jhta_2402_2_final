package org.example.jhta_2402_2_final.controller.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionMaterialDto;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderProcessDto;
import org.example.jhta_2402_2_final.service.distribution.DistributionOrderService;
import org.example.jhta_2402_2_final.service.distribution.KitOrderProcessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/distributionOrder")
public class DistributionOrderController {

    private final DistributionOrderService distributionOrderService;
    private final KitOrderProcessService kitOrderProcessService;

    @GetMapping
    public String getDefaultPage(Model model) {
        log.info("Received request to get default page");
        List<DistributionMaterialDto> sourcePricesList = distributionOrderService.getAllSourcePrices();
        model.addAttribute("sourcePricesList", sourcePricesList);
        return "distribution"; // 기본 페이지에 대한 뷰 이름
    }

    @GetMapping("source-prices")
    public ResponseEntity<List<DistributionMaterialDto>> getSourcePrices(
            @RequestParam("category") String category,
            @RequestParam("keyword") String keyword) {
        log.info("Received request to get source prices by category: {} and keyword: {}", category, keyword);
        List<DistributionMaterialDto> sourcePricesList = distributionOrderService.getFilteredSourcePrices(category, keyword);
        return ResponseEntity.ok(sourcePricesList); // JSON 형식으로 응답
    }

    @GetMapping("best-suppliers")
    public ResponseEntity<List<DistributionMaterialDto>> getBestSuppliers() {
        log.info("Received request to get best suppliers");
        List<DistributionMaterialDto> bestSuppliers = distributionOrderService.getBestSuppliers();
        return ResponseEntity.ok(bestSuppliers); // JSON 형식으로 응답
    }

    @PostMapping("/send-order-summary")
    public ResponseEntity<String> sendOrderSummary() {
        try {
            distributionOrderService.sendOrderSummary();
            return ResponseEntity.ok("주문 내역서가 성공적으로 전송되었습니다.");
        } catch (Exception e) {
            log.error("Failed to send order summary", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("주문 내역서를 전송하는 데 실패했습니다.");
        }
    }

    @PostMapping("/kitSourceCheckAndOrder")
    public ResponseEntity<String> kitSourceCheckAndOrder(
            @RequestParam("modalKitOrderID") String kitOrderID,
            @RequestParam("modalKitCompanyName") String kitCompanyName,
            @RequestParam("modalKitName") String kitName,
            @RequestParam("modalQuantity") int quantity,
            @RequestParam("modalDate") String kitOrderDate) {
        // 로그 추가
        log.info("Received kitOrderID: {}", kitOrderID);


        // Validate UUID format
        UUID kitOrderUid;
        try {
            kitOrderUid = UUID.fromString(kitOrderID);
        } catch (IllegalArgumentException e) {
            log.error("Invalid UUID format: {}", kitOrderID);
            return ResponseEntity.badRequest().body("Invalid UUID format.");
        }

        // Create DTO using validated data
        KitOrderProcessDto kitOrderProcessDto = KitOrderProcessDto.builder()
                .kitOrderUid(kitOrderUid)
                .kitCompanyName(kitCompanyName)
                .kitName(kitName)
                .quantity(quantity)
                .kitOrderDate(kitOrderDate)
                .status("Requested") // Example status
                .build();

        // Call service method to handle the order
        try {
            kitOrderProcessService.processKitSourceOrder(kitOrderProcessDto);
            return ResponseEntity.ok("Order placed successfully.");
        } catch (Exception e) {
            log.error("Error processing kit source order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to process the order.");
        }
    }


    @PostMapping("/processOrder")
    public String processOrder(@RequestParam("modalKitOrderID") String kitOrderId,
                               @RequestParam("modalKitCompanyName") String companyName,
                               @RequestParam("modalKitName") String kitName,
                               @RequestParam("modalQuantity") String quantity,
                               @RequestParam("modalDate") String orderDate,
                               @RequestParam("kitIngredients") List<String> ingredients,
                               @RequestParam("kitIngredientQuantities") List<String> ingredientQuantities,
                               Model model) {
        // 폼 데이터 로그 출력
        log.info("Order ID: {}", kitOrderId);
        log.info("Company Name: {}", companyName);
        log.info("Kit Name: {}", kitName);
        log.info("Quantity: {}", quantity);
        log.info("Order Date: {}", orderDate);

        // 재료 및 재료 수량 로그 출력
        for (int i = 0; i < ingredients.size(); i++) {
            log.info("Ingredient: {} - Quantity: {}", ingredients.get(i), ingredientQuantities.get(i));
        }

        // 모델에 추가할 데이터 설정
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

    @PostMapping("/kitOrderRelease")
    public String completeKitOrderRelease(@RequestParam("kitOrderId") String kitOrderId) {
        // 재고 관련 동시성 처리 로직 추가 필요
        boolean success = kitOrderProcessService.processKitOrder(kitOrderId);

        // 성공 시 성공 메시지와 함께 메인 페이지로 리다이렉트
        if (success) {
            return "redirect:/distribution/main?success=true";
        } else {
            // 실패 시 재고 부족 메시지와 함께 메인 페이지로 리다이렉트
            return "redirect:/distribution/main?error=insufficientStock";
        }
    }

    @GetMapping("/order-details")
    @ResponseBody
    public ResponseEntity<List<KitOrderProcessDto>> getOrderDetails(@RequestParam("orderKeyword") String orderKeyword) {
        List<KitOrderProcessDto> orders = kitOrderProcessService.findOrdersByKeyword(orderKeyword);
        return ResponseEntity.ok(orders);
    }
}
