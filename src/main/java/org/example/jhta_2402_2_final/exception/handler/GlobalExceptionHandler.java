package org.example.jhta_2402_2_final.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.exception.types.productCompany.AddCompanySourceException;
import org.example.jhta_2402_2_final.exception.types.productCompany.ProduceSourceException;
import org.example.jhta_2402_2_final.exception.types.productCompany.ProductCompanyOrderProcessException;
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
    /* Product Company */
    @ExceptionHandler(AddCompanySourceException.class)
    public ResponseEntity<Map<String, Object>> addCompanySourceException(AddCompanySourceException e) {
        Map<String, Object> responseError = new HashMap<>();
        responseError.put("message", e.getErrorMsg());
        responseError.put("httpStatus", e.getHttpStatus().value());
        return ResponseEntity.status(e.getHttpStatus()).body(responseError);
    }
    @ExceptionHandler(ProduceSourceException.class)
    public ResponseEntity<Map<String, Object>> produceSourceException(ProduceSourceException e){
        Map<String, Object> responseError = new HashMap<>();
        responseError.put("message", e.getErrorMsg());
        responseError.put("httpStatus", e.getHttpStatus().value());
        return ResponseEntity.status(e.getHttpStatus()).body(responseError);
    }
    @ExceptionHandler(ProductCompanyOrderProcessException.class)
    public ResponseEntity<Map<String, Object>> productCompanyOrderProcessException(ProductCompanyOrderProcessException e){
        Map<String, Object> responseError = new HashMap<>();
        responseError.put("message", e.getErrorMsg());
        responseError.put("httpStatus", e.getHttpStatus().value());
        return ResponseEntity.status(e.getHttpStatus()).body(responseError);
    }
    /* Product Company 끝 */
}