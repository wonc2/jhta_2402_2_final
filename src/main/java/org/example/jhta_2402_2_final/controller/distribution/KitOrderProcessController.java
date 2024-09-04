package org.example.jhta_2402_2_final.controller.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderProcessDto;
import org.example.jhta_2402_2_final.service.distribution.KitOrderProcessService;
import org.example.jhta_2402_2_final.service.sales.SalesService;

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

        return kitOrderProcessService.findKitRecipeWithStock(kitOrderId);

        // return kitOrderProcessService.findKitRecipe(kitOrderId); // 이 리턴 부분은 바뀔수도 있을 것 같음
    }



}







