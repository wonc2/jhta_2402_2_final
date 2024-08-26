package org.example.jhta_2402_2_final.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("ROLE_ADMIN"),
    WAREHOUSE_MANAGER("ROLE_WAREHOUSE_MANAGER"),
    STORE_MANAGER("ROLE_STORE_MANAGER"),
    ACCOUNT_MANAGER("ROLE_ACCOUNT_MANAGER"),
    SALES_MANAGER("ROLE_SALES_MANAGER"),
    HR_MANAGER("ROLE_HR_MANAGER"),
    ORDER_MANAGER("ROLE_ORDER_MANAGER");

    private final String role;
}