package org.example.jhta_2402_2_final.exception;

import org.example.jhta_2402_2_final.exception.types.CustomException;
import org.example.jhta_2402_2_final.exception.types.TestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class ExceptionUtil {
    /* errorMap 반환해주는 메서드입니다 */
    public Map<String, Object> getErrorMap(CustomException e) {

        String errorMessage;
        HttpStatus httpStatus;

        // throw error 에서 파람값 던졌을시 - 파람값 우선
        // throw error 에서 파람값 없을시 - Enum 에서 정의한 메시지, 상태 담음

        if (e.getDetailMessage() == null) {
            // Enum 에서 정의한 HTTP 상태 코드
            errorMessage = e.getErrorCode().getMessage();
        } else {
            // throw error (param) 값으로 던진 HTTP 상태 코드
            errorMessage = e.getDetailMessage();
        }
        if (e.getStatus() == null) {
            // Enum 에서 정의한 에러 메시지
            httpStatus = e.getErrorCode().getStatus();
        } else {
            // throw error (param) 값으로 던진 에러 메시지
            httpStatus = e.getStatus();
        }

        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("errorCode", e.getErrorCode());
        errorMap.put("errorMessage", errorMessage);
        errorMap.put("errorStatus", httpStatus);
        return errorMap;
    }
}
