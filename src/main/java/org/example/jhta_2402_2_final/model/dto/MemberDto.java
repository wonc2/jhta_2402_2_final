package org.example.jhta_2402_2_final.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MemberDto {
    private UUID primaryKey;
    private String userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userTel;
    private String role;



    @Builder
    public MemberDto(String userId, String userName, String userPassword, String userEmail, String role, String userTel) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userTel = userTel;
        this.role = role;
    }
}