package org.example.jhta_2402_2_final.model.dto.product;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductCompanyInsertDto {
    private String userId;
    private String userPassword;
    private String userName;
    private String productCompanyName;
    private String userEmail;
    private String userTel;
    private String productCompanyAddress;
    private String roleId;
}
