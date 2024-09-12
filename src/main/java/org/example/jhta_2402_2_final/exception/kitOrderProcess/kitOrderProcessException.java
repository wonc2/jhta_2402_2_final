package org.example.jhta_2402_2_final.exception.kitOrderProcess;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// kitOrderProcess 에 관한 Exception
@Getter
@RequiredArgsConstructor
public class kitOrderProcessException extends RuntimeException {
    private final String errorMsg;
    private final HttpStatus httpStatus;

}
