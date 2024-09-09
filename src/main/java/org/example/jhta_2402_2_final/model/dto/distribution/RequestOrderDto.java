package org.example.jhta_2402_2_final.model.dto.distribution;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestOrderDto {
    private String KitOrderId;
    private List<OrderDetail> orderDetailList;
}

