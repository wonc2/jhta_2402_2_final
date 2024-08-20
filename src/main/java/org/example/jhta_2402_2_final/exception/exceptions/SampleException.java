package org.example.jhta_2402_2_final.exception.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.enums.ErrorCode;

@Getter
@RequiredArgsConstructor
public class SampleException extends RuntimeException {
    private ErrorCode errorCode;
    private String detailMessage;
}
