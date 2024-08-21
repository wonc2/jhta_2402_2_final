package org.example.jhta_2402_2_final.exception.types;

import lombok.Getter;
import org.example.jhta_2402_2_final.model.enums.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public class TestException extends RuntimeException {
    private ErrorCode errorCode;
    private String detailMessage;
    private HttpStatus status;

    public TestException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public TestException(ErrorCode errorCode, String detailMessage) {
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
    }

    public TestException(ErrorCode errorCode, String detailMessage, HttpStatus status) {
        this.errorCode = errorCode;
        this.detailMessage = detailMessage;
        this.status = status;
    }
}