package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class KitOrderProcessDto {
    private UUID kitOrderUid;
    private String kitCompanyName;
    private String kitName;
    private int quantity;
    private String kitOrderDate;
    private String status;

    public KitOrderProcessDto(UUID kitOrderUid, String kitCompanyName, String kitName, int quantity, String kitOrderDate, String status) {
        this.kitOrderUid = kitOrderUid;
        this.kitCompanyName = kitCompanyName;
        this.kitName = kitName;
        this.quantity = quantity;
        this.kitOrderDate = kitOrderDate;
        this.status = status;
    }
}
