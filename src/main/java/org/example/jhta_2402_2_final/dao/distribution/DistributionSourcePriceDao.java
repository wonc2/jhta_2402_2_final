package org.example.jhta_2402_2_final.dao.distribution;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionOrderDetailDto;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionSourcePriceDto;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionBestSupplierDto;

import java.util.List;

@Mapper
public interface DistributionSourcePriceDao {

    // 특정 밀키트에 대한 소스 가격 정보를 가져오는 메서드
    List<DistributionSourcePriceDto> getSourcePricesForKit(@Param("kitId") String kitId);

    // 각 재료의 최저가 제공 업체 정보를 가져오는 메서드
    List<DistributionBestSupplierDto> getBestSuppliers();

    // 주문 내역을 가져오는 메서드
    List<DistributionOrderDetailDto> getOrderDetails();

    // 전체 가격 리스트를 가져오는 메서드 추가
    List<DistributionSourcePriceDto> getAllSourcePrices();
}
