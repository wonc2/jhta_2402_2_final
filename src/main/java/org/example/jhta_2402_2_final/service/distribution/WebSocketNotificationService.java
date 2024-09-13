package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderDistDto;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    // WebSocket을 통한 새로운 주문 알림 전송
    // 새로운 주문이 있을 때 WebSocket으로 알림 전송
    public void sendNewOrderNotification(KitOrderDistDto order) {
        messagingTemplate.convertAndSend("/topic/new-order", order);
    }
}

