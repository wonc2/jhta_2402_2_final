package org.example.jhta_2402_2_final.controller.product;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product/company")
public class ProductCompanyController {
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping()
    public String productMainPage() {return "product/productCompanyMainPage";}

    @MessageMapping("/update")
    @SendTo("/topic/product/company")
    public String sendUpdate() {return "connected";}
}
