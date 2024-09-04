package org.example.jhta_2402_2_final.model.dto.sales;
import lombok.Data;

@Data
public class KitOrderDetailDto {

    private String kitOrderId; //주문번호
    private String kitCompanyName; //밀키트 업체 이름
    private String mealkitName; //밀키트 이름
    private int mealkitPrice; //밀키트 가격
    private int quantity; //개수
    private int total; //합계
    private String orderDate; //주문일자
    private String status; //상태
}
