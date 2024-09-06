package org.example.jhta_2402_2_final.exception.types.productCompany;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ProduceSourceException extends RuntimeException {
    private final String errorMsg;
    private final HttpStatus httpStatus;
}
