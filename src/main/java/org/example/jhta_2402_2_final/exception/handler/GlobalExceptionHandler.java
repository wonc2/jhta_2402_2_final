package org.example.jhta_2402_2_final.exception.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.exception.ExceptionUtil;
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
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ExceptionUtil util;

    /* 예외 Json 으로 던질시 ResponseEntity 리턴해서 사용하면 됩니다. 테스트용 샘플입니다 ~ */
    @ExceptionHandler(TestException.class)
    public ResponseEntity<Map<String, Object>> testException(TestException e) {

        // exception 패키지 - ExceptionUtil 에 getErrorMap(Exception e) 로 errorMap 가져옴
        // errorMap: {
        //           "errorMessage": "에러메시지",
        //           "errorCode": "enum 에러 코드",
        //           "errorStatus": "http 상태 코드"
        //          }
        Map<String, Object> errorMap = util.getErrorMap(e);
        return new ResponseEntity<>(errorMap, (HttpStatus) errorMap.get("errorStatus"));
    }

    /* 예외 View 로 던질시 view 리턴해서 사용하면 됩니다. 테스트용 샘플입니다 ~ */
    @ExceptionHandler(SampleException.class)
    public String sampleException(SampleException e, Model model) {
        Map<String, Object> errorMap = util.getErrorMap(e);
        model.addAttribute("error", errorMap);
        return "error";
    }
}