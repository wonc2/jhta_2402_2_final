package org.example.jhta_2402_2_final.exception.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.exception.types.SampleException;
import org.example.jhta_2402_2_final.exception.types.TestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
//@RequiredArgsConstructor
public class GlobalExceptionHandler {

//    private final ExceptionUtil util;

    /* 예외 Json 으로 던질시 사용하면 됩니다. 테스트용 샘플입니다 ~ */
    @ExceptionHandler(TestException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> testException(TestException e) {

        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("errorCode", e.getErrorCode());    //  에러 코드
        errorMap.put("errorMessage", e.getErrorCode().getMessage()); // Enum 에서 정의한 에러 메시지
        errorMap.put("errorStatus", e.getErrorCode().getStatus()); // Enum 에서 정의한 HTTP 상태 코드

        return new ResponseEntity<>(errorMap, e.getErrorCode().getStatus());
    }

    /* 예외 View 로 던질시 @ResponseBody 빼고, view 리턴해서 사용하면 됩니다. */
    @ExceptionHandler(SampleException.class)
    public String sampleException(SampleException e, Model model) {

        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("errorCode", e.getErrorCode());    //  에러 코드
        errorMap.put("errorMessage", e.getErrorCode().getMessage()); // Enum 에서 정의한 에러 메시지
        errorMap.put("errorStatus", e.getErrorCode().getStatus()); // Enum 에서 정의한 HTTP 상태 코드
        model.addAttribute("error", errorMap);

        return "error.html";
    }
}
