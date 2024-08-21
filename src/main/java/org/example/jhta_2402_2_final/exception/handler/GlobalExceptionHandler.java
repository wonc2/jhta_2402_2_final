package org.example.jhta_2402_2_final.exception.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.exception.ExceptionUtil;
import org.example.jhta_2402_2_final.exception.types.TestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    /* 예외 Json 으로 던질시 사용하면 됩니다. 테스트용 샘플입니다 ~ */
    @ExceptionHandler(TestException.class)
    @ResponseBody
    public ResponseEntity<Map<String, Object>> testExceptionJson(TestException e) {
        // errorMap 가져오기
        Map<String, Object> errorMap = util.getErrorMap(e);
//  errorMap : {                                    -- 이렇게 가져옵니다.
//                "errorMessage": "에러메시지 ",
//                "errorCode": "Enum에 정의한 코드 ",
//                "errorStatus": "Http 상태 코드 "
//        }

        // util.getErrorMap(e) 메서드로 errorMap 가져와서 아래랑 똑같은 형식으로 리턴 하면 됩니다.
        return new ResponseEntity<>(errorMap, (HttpStatus) errorMap.get("errorStatus"));
    }

    /* 예외 View 로 던질시 @ResponseBody 빼고, view 리턴해서 사용하면 됩니다. */
//    @ExceptionHandler(TestException.class)
//    public String testExceptionView (TestException e, Model model){
//        // 커스텀 예외 View 로 내리기
//
//        Map<String, Object> errorMap = new HashMap<>();
//        errorMap.put("errorCode", e.getErrorCode());
//        model.addAttribute("error", errorMap);
//
//        return "error.html";
//    }
}
