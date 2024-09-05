//package org.example.jhta_2402_2_final.util;
//
//import jakarta.annotation.PostConstruct;
//import net.nurigo.sdk.NurigoApp;
//import net.nurigo.sdk.message.model.Message;
//import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
//import net.nurigo.sdk.message.response.SingleMessageSentResponse;
//import net.nurigo.sdk.message.service.DefaultMessageService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SmsUtil {
//
//    @Value("${coolsms.api.key}")
//    private String apiKey;
//    @Value("${coolsms.api.secret}")
//    private String apiSecretKey;
//
//    private DefaultMessageService messageService;
//
//    @PostConstruct
//    private void init() {
//        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecretKey, "https://api.coolsms.co.kr");
//    }
//
//    // 단일 메시지 발송 예제
////    public SingleMessageSentResponse sendOne(String to, String verificationCode) {
//    public SingleMessageSentResponse sendOne() {
//        Message message = new Message();
//        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
//        message.setFrom("01050692632");
//        message.setTo("01029974205");
//        message.setText("박신혁이 테스트로 보냅니다.");
//
//        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
//        return response;
//    }
//
//}