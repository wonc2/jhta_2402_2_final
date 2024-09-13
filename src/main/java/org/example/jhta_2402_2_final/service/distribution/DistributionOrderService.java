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
    private final WebSocketNotificationService webSocketNotificationService; // 웹소켓 서비스 주입


    public List<KitOrderDistDto> getNewOrders() {
        return distributionOrderDao.selectNewOrder();
    }

    public void checkAndNotifyNewOrders() {
        List<KitOrderDistDto> newOrders = getNewOrders();
        if (!newOrders.isEmpty()) {
            for (KitOrderDistDto order : newOrders) {
                webSocketNotificationService.sendNewOrderNotification(order);
            }
        }
    }

    public void confirmOrder(UUID kitOrderId) {

    }
}







//    //controller
//    public List<KitOrderDistDto> getAllKitOrderDto() {
//        return distributionOrderDao.selectKitOrder();
//    }
//
//    public List<KitOrderNameDto> kitOrderNameDto() {
//        return distributionOrderDao.selectKitNameOrder();
//    }
//
//    public List<KitOrderSourceNameDto> kitOrderSourceNameDto() {
//        return distributionOrderDao.selectSourceNameOrder();
//    }
//
//    public List<MinPriceSourceDto> minPriceSourceDto() {
//        return distributionOrderDao.selectMinPriceSource();
//    }
//    public int insertProductOrder(IngredientDto ingredientDto){
//        return distributionOrderDao.insertProductOrder2(ingredientDto);
//    }
//    public int insertProductOrderLog(String productOrderId){
//        return distributionOrderDao.insertProductOrderLog(productOrderId);
//    }
//    public String getProductOrderId(IngredientDto ingredientDto){
//        return distributionOrderDao.getProductOrderId(ingredientDto);
//    }

    /*public void processOrder(UUID productOrderId, UUID productCompanyId, UUID sourceId, int totalQuantity, int sourcePrice, UUID kitOrderId) {
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
        distributionOrderDao.insertProductOrderLog(dto);
    }*/

//    public List<MinPriceOrderDto> minPriceOrderDto(){
//        return distributionOrderDao.selectKitOrderId();
//    }
//
//    public List<IngredientDto> getIngredientsByKitOrderId(UUID kitOrderId) {
//        return distributionOrderDao.getIngredientsByKitOrderId(kitOrderId);
//    }




    /*public void processIngredients(List<IngredientDto> ingredients, UUID kitOrderId) {
        for (IngredientDto ingredient : ingredients) {
            // 1. Insert into PRODUCT_ORDER
            UUID productOrderId = UUID.randomUUID(); // 새로운 UUID 생성
            UUID productCompanyId = getProductCompanyId(ingredient.getSupplier()); // 공급업체 ID를 가져오는 메서드 필요
            UUID sourceId = getSourceId(ingredient.getName()); // 재료 ID를 가져오는 메서드 필요

            distributionOrderDao.insertProductOrder(productOrderId, productCompanyId, sourceId,
                    ingredient.getQuantity(), ingredient.getPrice(), kitOrderId);

            // 2. Insert into PRODUCT_ORDER_LOG
            distributionOrderDao.insertProductOrderLog(productOrderId);
        }

        // 3. Update KIT_ORDER status
        distributionOrderDao.updateKitOrderStatus(kitOrderId);
    }*/
//
//    private UUID getProductCompanyId(String supplierName) {
//        return distributionOrderDao.selectProductCompanyIdByName(supplierName);
//    }
//
//    private UUID getSourceId(String ingredientName) {
//        return distributionOrderDao.selectSourceIdByName(ingredientName);
//    }
//
//
//
//}
