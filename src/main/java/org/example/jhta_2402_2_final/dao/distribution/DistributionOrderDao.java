package org.example.jhta_2402_2_final.dao.distribution;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.distribution.*;
import org.example.jhta_2402_2_final.model.dto.distribution.MinPriceOrderDto;

import java.util.List;
import java.util.UUID;

@Mapper
public interface DistributionOrderDao {
    // kit_order 테이블 전체 셀렉
    List<KitOrderDistDto> selectKitOrder();

    //kit_order 테이블 조인해서 상세정보 보기 편하게 셀렉
    List<KitOrderNameDto> selectKitNameOrder();

    //kit_oder 테이블 조인해서 밀키트 재료 셀렉
    List<KitOrderSourceNameDto> selectSourceNameOrder();

    //kti_order sourcePrice 테이블로 가격 셀랙
    List<MinPriceSourceDto> selectMinPriceSource();

    UUID selectSourcePriceId(UUID sourceId, UUID productCompanyId);

    List<MinPriceOrderDto> selectKitOrderId();


    int insertProductOrder(UUID productOrderId, UUID productCompanyId, UUID sourceId,
                           int quantity,int price,UUID kitOrderId);
int insertProductOrder2(IngredientDto ingredientDto);
    int insertProductOrderLog(String productOrderId);

    String getProductOrderId(IngredientDto ingredientDto);


    void updateKitOrderStatus(UUID kitOrderId);

    List<IngredientDto> getIngredientsByKitOrderId(UUID kitOrderId);


    UUID selectProductCompanyIdByName(String supplierName);

    UUID selectSourceIdByName(String ingredientName);
}
