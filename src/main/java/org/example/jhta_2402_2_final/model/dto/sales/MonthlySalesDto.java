package org.example.jhta_2402_2_final.model.dto.sales;

import lombok.Data;

@Data
public class MonthlySalesDto {
    private String companyName;
    private int orderMonth;
    private int totalSales;
}
