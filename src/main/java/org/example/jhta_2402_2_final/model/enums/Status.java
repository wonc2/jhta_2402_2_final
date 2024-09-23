package org.example.jhta_2402_2_final.model.enums;

import lombok.Getter;

@Getter
public enum Status {
    BEFORE_PROCESSING(1, "처리전"),
    IN_PROCESS(2, "처리중"),
    COMPLETED(3, "처리완료"),
    CANCELED(4, "취소"),
    WAITING_FOR_WAREHOUSING(5, "입고대기"),
    WAITING_FOR_SHIPMENT(6, "출고대기"),
    WAREHOUSED(7, "입고"),
    SHIPPED(8, "출고");

    private int statusId;
    private String statusName;

    Status(int statusId, String statusName) {
        this.statusId = statusId;
        this.statusName = statusName;
    }

}
