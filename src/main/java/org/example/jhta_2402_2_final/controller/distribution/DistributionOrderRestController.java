package org.example.jhta_2402_2_final.controller.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.SimpleMessage;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderDistDto;
import org.example.jhta_2402_2_final.service.distribution.DistributionOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/distribution")
@Slf4j
public class DistributionOrderRestController {

    private final DistributionOrderService distributionOrderService;
    private final SimpMessagingTemplate messagingTemplate;

    public DistributionOrderRestController(SimpMessagingTemplate messagingTemplate,
                                           DistributionOrderService distributionOrderService){

        this.distributionOrderService = distributionOrderService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/get-new-orders")
    public ResponseEntity<List<KitOrderDistDto>> getNewOrder(){
        List<KitOrderDistDto> newOrders = distributionOrderService.getNewOrders();

        return  ResponseEntity.ok(newOrders);
    }

    @PostMapping("/confirm-order")
    public ResponseEntity<Void> confirm(@RequestParam("kitOrderId") UUID kitOrderId){
        distributionOrderService.confirmOrder(kitOrderId);
        return ResponseEntity.ok().build();
    }




}
