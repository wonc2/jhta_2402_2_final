package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
//주문내역 가져오기

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DistributionOrderDetailDto {
    private String 주문번호;
    private String 주문업체명;
    private String 밀키트이름;
    private double 밀키트가격;
    private int 주문개수;
    private double 총판매가;
    private Date 주문일자;
    private String 상태;

}
