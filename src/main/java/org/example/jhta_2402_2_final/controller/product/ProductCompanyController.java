package org.example.jhta_2402_2_final.controller.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.CustomUserDetails;
import org.example.jhta_2402_2_final.service.CustomUserDetailsService;
import org.example.jhta_2402_2_final.service.product.ProductCompanyService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product/company")
public class ProductCompanyController {
    private final ProductCompanyService productCompanyService;

    @GetMapping()
    public String productMainPage(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        String companyName = productCompanyService.getCompanyNameByUserId(userDetails.getMemberDto().getUserId());
        model.addAttribute("companyName", companyName);
        return "product/productCompanyMainPage";
    }

    @MessageMapping("/update")
    @SendTo("/topic/product/company")
    public String sendUpdate() {return "connected";}
}
