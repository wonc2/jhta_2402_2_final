package org.example.jhta_2402_2_final.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.exception.exceptions.SampleException;
import org.example.jhta_2402_2_final.model.enums.ErrorCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(SampleException.class)
    private String sampleExceptionHandler(SampleException e, ModelAndView modelAndView) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("error", e.getErrorCode());
        errorMap.put("message", e.getErrorCode().getMessage());
        return null;
    }
}
