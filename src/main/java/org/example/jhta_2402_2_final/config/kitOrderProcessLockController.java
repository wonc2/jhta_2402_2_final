/*package org.example.jhta_2402_2_final.config;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class kitOrderProcessLockController {
    @MessageMapping("/lock")
    @SendTo("/topic/lockStatus")
    public LockStatusMessage lock(String kitOrderId) {
        return new LockStatusMessage(kitOrderId, true); // true 는 잠금 의미
    }

    // 잠금 해제 요청을 처리할 수 있는 엔드포인트
    @MessageMapping("/unlock")
    @SendTo("/topic/lockStatus")
    public LockStatusMessage unlock(String orderId) {
        // 잠금 해제 메시지 생성
        return new LockStatusMessage(kitOrderId, false); // false는 잠금 해제를 의미
    }


}*/
