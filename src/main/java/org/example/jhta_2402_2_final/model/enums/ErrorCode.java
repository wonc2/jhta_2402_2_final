package org.example.jhta_2402_2_final.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    // 샘플용.. 어떻게 만드는지 모름
    NOT_FOUND("해당하는 아이디없음"),
    INVALID_REQUEST("잘못된 요청"),
    INTERNAL_SERVER_ERROR("알 수 없는 오류"),
    BAD_REQUEST("잘못된 요청");

    private final String message;
}
