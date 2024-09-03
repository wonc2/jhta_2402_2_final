package org.example.jhta_2402_2_final.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.exception.types.DuplicateCompanySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateCompanySource.class)
    public ResponseEntity<Map<String, Object>> handleDuplicateCompanySource(DuplicateCompanySource ex) {
        log.error(ex.toString());
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("message", ex.getMessage());
        errorMap.put("httpStatus", ex.getHttpStatus());
        return ResponseEntity.status(ex.getHttpStatus()).body(errorMap);
    }
}