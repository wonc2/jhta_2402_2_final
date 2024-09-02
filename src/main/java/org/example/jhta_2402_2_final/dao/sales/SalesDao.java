package org.example.jhta_2402_2_final.dao.sales;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.jhta_2402_2_final.model.dto.sales.KitOrderDetailDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitOrderDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitOrderLogDto;
import org.example.jhta_2402_2_final.model.dto.sales.KitSourceDetailDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface SalesDao {

    List<KitOrderDto> findAll();
    Optional<KitOrderDto> findById(UUID id);
    int insert(KitOrderDto kitOrderDto);
    int update(KitOrderDto kitOrderDto);
    int delete(UUID id);

    List<KitOrderDto> search(String category, String keyword);

    // 조인 셀렉 가져오기
    List<KitOrderDetailDto> findAllDetail();

    //업체명이랑 pk가져오기
    List<Map<String, String>> getKitCompanyIdAndNames();

    //밀키트명이랑 pk가져오기
    List<Map<String, Object>> getMealkitIdAndNames();

    // 상태 변경하기
    int updateKitOrderStatus(@Param("kitOrderId") String kitOrderId, @Param("statusId") int statusId);

    //밀키트별 재료 가져오기
    List<KitSourceDetailDto> findAllKitSourceDetail();

    //재료명이랑 pk가져오기
    List<Map<String, String>> getSourceIdAndNames();

    void insertMealkit(@Param("mealkitName") String mealkitName);

    String getMealkitIdByName(@Param("name") String name);

    void insertKitSources(@Param("mealkitId") String mealkitId,
                          @Param("sourceId") String sourceId,
                          @Param("quantity") Integer quantity);

    //로그 셀렉
    List<KitOrderLogDto> selectKitOrderLogs();

    void insertLog(UUID kitOrderId, int statusId);
}