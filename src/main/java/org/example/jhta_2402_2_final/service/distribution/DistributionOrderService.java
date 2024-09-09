package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.dao.distribution.DistributionOrderDao;
import org.example.jhta_2402_2_final.model.dto.distribution.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DistributionOrderService {

    private final DistributionOrderDao distributionOrderDao;


    public List<KitOrderDistDto> getAllKitOrderDto() {
        return distributionOrderDao.selectKitOrder();
    }

    public List<KitOrderNameDto> kitOrderNameDto() {
        return distributionOrderDao.selectKitNameOrder();
    }

    public List<KitOrderSourceNameDto> kitOrderSourceNameDto() {
        return distributionOrderDao.selectSourceNameOrder();
    }

    public List<MinPriceSourceDto> minPriceSourceDto() {
        return distributionOrderDao.selectMinPriceSource();
    }


    @Transactional
    public void sourceOrder(String kitOrderId, String sourceId, int quantity, int sourcePrice) {
        // 새로운 주문 ID 생성
        String productOrderId = UUID.randomUUID().toString();

        // 주문 데이터를 DAO로 전달하여 삽입
        Map<String, Object> params = new HashMap<>();
        params.put("productOrderId", productOrderId);
        params.put("kitOrderId", kitOrderId);
        params.put("sourceId", sourceId);
        params.put("quantity", quantity);
        params.put("productOrderDate", new Date()); // 현재 날짜
        params.put("statusId", 1); // 상태 ID 1로 설정

        // DAO 호출
        distributionOrderDao.insertProductOrder();
    }

    public UUID selectSourcePriceId(UUID sourceId, UUID productCompanyId) {
        return distributionOrderDao.selectSourcePriceId(sourceId, productCompanyId);
    }

    public int insertProductOrder(UUID sourcePriceId, int quantity) {
        return distributionOrderDao.insertProductOrder(sourcePriceId, quantity);
    }





}
