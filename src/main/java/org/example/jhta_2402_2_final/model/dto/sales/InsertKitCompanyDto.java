package org.example.jhta_2402_2_final.model.dto.sales;

import lombok.Data;

import java.util.UUID;

@Data
public class InsertKitCompanyDto {
    // 폼으로 넘겨받는 값
    private String userId;
    private String password;
    private String passwordConfirm;
    private String companyName;
    private String userName;
    private String email;
    private String tel;
    private String address;
    private String role;


    //폼으로 못받는 값
    private UUID userPk;
    private UUID kitCompanyId;

}
