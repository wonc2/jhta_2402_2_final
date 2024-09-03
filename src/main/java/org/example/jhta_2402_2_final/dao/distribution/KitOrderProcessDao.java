package org.example.jhta_2402_2_final.dao.distribution;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.jhta_2402_2_final.model.dto.distribution.KitOrderProcessDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mapper
public interface KitOrderProcessDao {

    List<Map<String, Object>> findAllOrder();

    int requestKitSourceOrder(KitOrderProcessDto kitOrderProcessDto);

    // 주문번호(id)를 파라미터로 해서 마이바티스 매핑 후 레시피 조회
    List<Map<String, Object>> findKitRecipe(@Param("kitOrderId") String kitOrderId);

    // 주문 내역을 조회하는 쿼리 (키워드 기반)
    List<Map<String, Object>> findOrdersByKeyword(@Param("orderKeyword") String orderKeyword);

    int saveOrder(KitOrderProcessDto dto);

    List<Map<String, Object>> findAllOrders();







    /*List<Map<String, Object>> findKitSource();*/
}
