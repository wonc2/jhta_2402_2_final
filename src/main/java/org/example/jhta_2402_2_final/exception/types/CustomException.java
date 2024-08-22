package org.example.jhta_2402_2_final.exception.types;

import org.example.jhta_2402_2_final.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

public interface CustomException {
    ErrorCode getErrorCode();
    String getDetailMessage();
    HttpStatus getStatus();
}
