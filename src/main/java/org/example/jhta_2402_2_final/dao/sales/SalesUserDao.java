package org.example.jhta_2402_2_final.dao.sales;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.salesUser.UserKitOrderDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface SalesUserDao {

    UserKitOrderDto selectKitCompanyIdByUserId(String userId);

    List<UserKitOrderDto> selectKitOrderByKitCompanyId(String kitCompanyId);

    int insertKitOrder(UserKitOrderDto dto);

    List<Map<String, Object>> selectKitStorage(String kitCompanyId);

    List<Integer> selectMonthly(String kitCompanyId);
}
