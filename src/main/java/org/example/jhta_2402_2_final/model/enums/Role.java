package org.example.jhta_2402_2_final.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_KIT_MANAGER("ROLE_KIT_MANAGER"),
    ROLE_PRODUCT_MANAGER("ROLE_PRODUCT_MANAGER");

    private final String role;
}