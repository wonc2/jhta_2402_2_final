package org.example.jhta_2402_2_final.controller.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.service.distribution.KitOrderProcessService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/distribution")
//@RequestMapping("/logistics")
public class KitOrderProcessController {

    private final KitOrderProcessService kitOrderProcessService;

    @GetMapping("/main")
    public String kitOrderProcessMainPage(Model model) {
        List<Map<String, Object>> kitOrderList = kitOrderProcessService.findAllOrder();
        model.addAttribute("kitOrderList", kitOrderList);

        return "distribution/KitOrderProcessMainPage";
    }



}
