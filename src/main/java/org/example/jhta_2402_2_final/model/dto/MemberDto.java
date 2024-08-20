package org.example.jhta_2402_2_final.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private int primaryKey;
    private String userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String role;



    @Builder
    public MemberDto(String userId, String userName, String userPassword, String userEmail, String role) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.role = role;
    }
}