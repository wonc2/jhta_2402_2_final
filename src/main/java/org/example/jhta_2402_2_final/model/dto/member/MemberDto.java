package org.example.jhta_2402_2_final.model.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@Setter
@ToString
public class MemberDto {
    private UUID primaryKey;
    private String userId;
    private String userName;
    private String userPassword;
    private String userEmail;
    private String userTel;
    private String roleId;



    @Builder
    public MemberDto(String userId, String userName, String userPassword, String userEmail, String roleId, String userTel) {
        this.userId = userId;
        this.userName = userName;
        this.userPassword = userPassword;
        this.userEmail = userEmail;
        this.userTel = userTel;
        this.roleId = roleId;
    }
}