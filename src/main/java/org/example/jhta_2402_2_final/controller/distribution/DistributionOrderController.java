package org.example.jhta_2402_2_final.controller.distribution;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.distribution.*;
import org.example.jhta_2402_2_final.service.distribution.DistributionOrderService;
import org.example.jhta_2402_2_final.model.dto.distribution.MinPriceOrderDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

//@RequiredArgsConstructor
//@Controller
//public class DistributionOrderController {

//    private final DistributionOrderService distributionOrderService;

//    @GetMapping("/distribution/order")
//    public String order(Model model) {}
//        List<KitOrderDistDto> kitOrderDtos = distributionOrderService.getAllKitOrderDto();
//        model.addAttribute("kitOrderDtos", kitOrderDtos);
//
//        List<KitOrderNameDto> kitOrderNameDtos = distributionOrderService.kitOrderNameDto();
//        model.addAttribute("kitOrderNameDtos", kitOrderNameDtos);
//
//        List<KitOrderSourceNameDto> kitOrderSourceNameDtos = distributionOrderService.kitOrderSourceNameDto();
//        model.addAttribute("kitOrderSourceNameDtos", kitOrderSourceNameDtos);
//
//        List<MinPriceSourceDto> minPriceSourceDtos = distributionOrderService.minPriceSourceDto();
//        model.addAttribute("minPriceSourceDtos", minPriceSourceDtos);
//
//        List<MinPriceOrderDto> minPriceOrderDtos = distributionOrderService.minPriceOrderDto();
//        model.addAttribute("minPriceOrderDtos", minPriceOrderDtos);
//
//        return "distribution/order";
   // }

    /*@PostMapping("/distribution/sourceOrder")
    public String sourceOrder(@RequestParam("kitOrderId") UUID kitOrderId,
                              @RequestParam("sourceId") UUID sourceId,
                              @RequestParam("totalQuantity") int totalQuantity,
                              @RequestParam("sourcePrice") int sourcePrice,
                              @RequestParam("productCompanyId") UUID productCompanyId) {

        System.out.println("kitOrderId =>>>>>>>> " + kitOrderId);
        System.out.println("sourceId =>>>>>>>> " + sourceId);
        System.out.println("totalQuantity =>>>>>>>> " + totalQuantity);
        System.out.println("sourcePrice =>>>>>>>> " + sourcePrice);
        System.out.println("productCompanyId =>>>>>>>> " + productCompanyId);

        UUID productOrderId = UUID.randomUUID();

        distributionOrderService.processOrder(productOrderId, productCompanyId, sourceId, totalQuantity, sourcePrice, kitOrderId);

        return "redirect:/distribution/order";
    }*/

    /*@PostMapping("/distribution/productToOrder")
    public String productToOrder(@RequestParam("kitOrderId") UUID kitOrderId,
                                 @RequestParam("sourceId") UUID sourceId,
                                 @RequestParam("totalQuantity") int totalQuantity,
                                 @RequestParam("sourcePrice") int sourcePrice,
                                 @RequestParam("productCompanyId") UUID productCompanyId) {

        System.out.println("kitOrderId =>>>>>>>> " + kitOrderId);
        System.out.println("sourceId =>>>>>>>> " + sourceId);
        System.out.println("totalQuantity =>>>>>>>> " + totalQuantity);
        System.out.println("sourcePrice =>>>>>>>> " + sourcePrice);
        System.out.println("productCompanyId =>>>>>>>> " + productCompanyId);

        UUID productOrderId = UUID.randomUUID();

        distributionOrderService.processOrder(productOrderId, productCompanyId, sourceId, totalQuantity, sourcePrice, kitOrderId);

        return "redirect:/distribution/order";
    };*/
//    @GetMapping("/distribution/getIngredients")
//    public ResponseEntity<String> getIngredients(@RequestParam("kitOrderId") UUID kitOrderId) {
//        List<IngredientDto> ingredients = distributionOrderService.getIngredientsByKitOrderId(kitOrderId);
//        System.out.println(ingredients);
//        // Convert the list of ingredients to a plain text format
//        StringBuilder result = new StringBuilder();
//        for (IngredientDto ingredient : ingredients) {
//            result.append(String.format("%s, %d,  %d, %s\n",
//                    ingredient.getName(), ingredient.getQuantity(),
//                    ingredient.getPrice(), ingredient.getSupplier()));
//        }
//        System.out.println("result=="+result);
//        return ResponseEntity.ok(result.toString());
//    }


//    @GetMapping("/distribution/getIngredients2")
//    @ResponseBody
//    public int getIngredients2(@RequestParam("kitOrderId") UUID kitOrderId) {
//        List<IngredientDto> ingredients = distributionOrderService.getIngredientsByKitOrderId(kitOrderId);
//        int result =0;
//        for(int i=0; i<ingredients.size(); i++){
//            ingredients.get(i).setKitOrderId(kitOrderId);
//            result = distributionOrderService.insertProductOrder(ingredients.get(i));
//            String productOrderId = distributionOrderService.getProductOrderId(ingredients.get(i));
//            distributionOrderService.insertProductOrderLog(productOrderId);
//            System.out.println(result);
//            result=0;
//        }
//
//
//        return 0;
//    }
    /*@PostMapping("/distribution/insertIngredients")
    public ResponseEntity<String> insertIngredients(@RequestBody String requestBody) {
        try {
            System.out.println("requestBody==="+requestBody);
            // Parse the requestBody from text format
            String[] params = requestBody.split("&");
            String ingredientsString = params[0].split("=")[1];
            String kitOrderIdString = params[1].split("=")[1];

            // Decode and parse the JSON string
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> ingredientList = objectMapper.readValue(URLDecoder.decode(ingredientsString, "UTF-8"), new TypeReference<List<Map<String, Object>>>() {});
            UUID kitOrderId = UUID.fromString(kitOrderIdString);

            // Convert to DTOs
            List<IngredientDto> ingredients = ingredientList.stream()
                    .map(item -> {
                        IngredientDto dto = new IngredientDto();
                        dto.setName((String) item.get("name"));
                        dto.setQuantity((Integer) item.get("quantity"));
                        dto.setPrice((Integer) item.get("price"));
                        dto.setSupplier((String) item.get("supplier"));
                        return dto;
                    })
                    .collect(Collectors.toList());

            distributionOrderService.processIngredients(ingredients, kitOrderId);
            return ResponseEntity.ok("Ingredients successfully inserted and processed.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inserting ingredients: " + e.getMessage());
        }
    }*/






//}
