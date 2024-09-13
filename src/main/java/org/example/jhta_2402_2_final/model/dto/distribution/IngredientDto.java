package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Getter
@Setter
public class IngredientDto {
    private String name;        // 재료 이름
    private int quantity;       // 재료 수량
    private int price;          // 최저가
    private String supplier;    // 공급업체 이름
    private UUID kitOrderId;
}