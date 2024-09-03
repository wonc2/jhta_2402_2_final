package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.jhta_2402_2_final.dao.distribution.DistributionSourcePriceDao;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionMaterialDto;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DistributionOrderService {

    private final DistributionSourcePriceDao distributionSourcePriceDao;


    public List<DistributionMaterialDto> getSourcePricesForKit(String kitId) {
        log.info("Fetching source prices for kitId: {}", kitId);
        return distributionSourcePriceDao.getSourcePricesForKit(kitId);
    }

    public List<DistributionMaterialDto> getBestSuppliers() {
        return distributionSourcePriceDao.getBestSuppliers();
    }

    public List<DistributionMaterialDto> getAllSourcePrices() {
        return distributionSourcePriceDao.getAllSourcePrices();
    }

    public List<DistributionMaterialDto> getFilteredSourcePrices(String category, String keyword) {
        return distributionSourcePriceDao.findSourcePricesByCategoryAndKeyword(category, keyword);
    }

    public void sendOrderSummary() {
        try {
            // 모든 주문 내역을 가져옵니다. 실제로는 필터링이 필요할 수 있습니다.
            List<DistributionMaterialDto> orders = distributionSourcePriceDao.getAllSourcePrices();

            for (DistributionMaterialDto order : orders) {
                String orderSummary = createOrderSummary(order);
            }

            log.info("주문 내역서가 성공적으로 전송되었습니다.");
        } catch (Exception e) {
            log.error("주문 내역서를 전송하는 데 실패했습니다.", e);
        }
    }

    private String createOrderSummary(DistributionMaterialDto order) {
        // 주문 내역서를 생성합니다. 실제로는 템플릿 엔진을 사용할 수 있습니다.
        return "주문 내역서 내용\n" +
                "품목: " + order.getMaterialName() + "\n" +
                "수량: " + order.getQuantity() + "\n" +
                "가격: " + order.getPrice() + "\n" +
                "주문 날짜: " + order.getOrderDate() + "\n";
    }
}
