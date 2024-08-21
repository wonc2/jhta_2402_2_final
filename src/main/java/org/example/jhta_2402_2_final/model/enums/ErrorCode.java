package org.example.jhta_2402_2_final.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND("404 - 페이지를 찾을 수 없음", HttpStatus.NOT_FOUND),
    SERVER_ERROR("500 - 알 수 없는 에러", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("400 - 잘못된 요청", HttpStatus.BAD_REQUEST);
    // 추가적인 오류 코드...

    private final String message;
    private final HttpStatus status;
}
