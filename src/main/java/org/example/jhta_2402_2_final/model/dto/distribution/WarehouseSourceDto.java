package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.Data;
import org.springframework.security.core.parameters.P;

import java.util.UUID;

@Data
public class WarehouseSourceDto {
    private UUID sourceId;
    private String sourceName;
    private int quantity;
}
