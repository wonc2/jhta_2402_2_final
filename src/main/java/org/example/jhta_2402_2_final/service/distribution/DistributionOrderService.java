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
    

    public void processOrder(UUID productOrderId, UUID productCompanyId, UUID sourceId, int totalQuantity, int sourcePrice, UUID kitOrderId) {
        // 상품 주문을 데이터베이스에 저장
        int result = distributionOrderDao.insertProductOrder(productOrderId, productCompanyId, sourceId, totalQuantity, sourcePrice, kitOrderId);

        if (result > 0) System.out.println("인서트 성공");
        else System.out.println("인서트 실패 ");
        
        // 로그찍기
        int result02 =  distributionOrderDao.insertProductOrderLog(productOrderId);
        
        if (result02 > 0) System.out.println("로그 인서트 성공");
        else System.out.println("로그 인서트 실패");


        distributionOrderDao.updateKitOrderStatus(kitOrderId);
        // 주문 로그를 기록
       /*distributionOrderDao.insertProductOrderLog(dto);*/
    }
}
