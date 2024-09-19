package org.example.jhta_2402_2_final.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.exception.types.productCompany.CompanySourceException;
import org.example.jhta_2402_2_final.exception.types.productCompany.ProduceSourceException;
import org.example.jhta_2402_2_final.exception.types.productCompany.ProductCompanyAccessException;
import org.example.jhta_2402_2_final.exception.types.productCompany.ProductCompanyOrderProcessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /* Product Company */
    @ExceptionHandler(ProductCompanyAccessException.class)
    public ResponseEntity<Map<String, String>> handleProductCompanyAccessException(ProductCompanyAccessException e) {
        return buildErrorResponse(e.getErrorMsg(), e.getHttpStatus());
    }
    @ExceptionHandler(CompanySourceException.class)
    public ResponseEntity<Map<String, String>> addCompanySourceException(CompanySourceException e) {
        return buildErrorResponse(e.getErrorMsg(), e.getHttpStatus());
    }
    @ExceptionHandler(ProduceSourceException.class)
    public ResponseEntity<Map<String, String>> produceSourceException(ProduceSourceException e){
        return buildErrorResponse(e.getErrorMsg(), e.getHttpStatus());
    }
    @ExceptionHandler(ProductCompanyOrderProcessException.class)
    public ResponseEntity<Map<String, String>> productCompanyOrderProcessException(ProductCompanyOrderProcessException e){
        return buildErrorResponse(e.getErrorMsg(), e.getHttpStatus());
    }
    /* Product Company 끝 */

    // errorMessage, HttpStatus 리턴 메서드
    private ResponseEntity<Map<String, String>> buildErrorResponse(String errorMessage, HttpStatus status) {
        log.warn("{} : {} : {}", errorMessage, status.value(), status.getReasonPhrase());
        Map<String, String> responseError = Map.of("message", errorMessage);
        return ResponseEntity.status(status).body(responseError);
    }
}