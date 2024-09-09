package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CombineLogDto {
    private String id;
    private String companyName;
    private String sourceName;
    private String sourceId;
    private int quantity;
    private Date orderDate;
    private String status;


    // Getters and setters
}
