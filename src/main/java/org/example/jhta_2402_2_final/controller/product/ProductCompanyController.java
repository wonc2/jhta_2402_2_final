package org.example.jhta_2402_2_final.controller.product;

import org.example.jhta_2402_2_final.model.dto.common.ModalStatus;
import org.example.jhta_2402_2_final.model.dto.productCompany.ProductCompanyUpdateMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product/company")
public class ProductCompanyController {
    @GetMapping()
    public String productMainPage() {return "product/productCompanyMainPage";}

    @MessageMapping("/update")
    @SendTo("/topic/product/company")
    public ProductCompanyUpdateMessage sendUpdate() {
        return new ProductCompanyUpdateMessage("update");  // WebSocket 메시지 처리
    }

    @MessageMapping("/produceModal/open")
    @SendTo("/topic/produceModal")
    public ModalStatus handleModalOpen() {
        return new ModalStatus("open");
    }

    @MessageMapping("/produceModal/close")
    @SendTo("/topic/produceModal")
    public ModalStatus handleModalClose() {
        return new ModalStatus("closed");
    }
}
