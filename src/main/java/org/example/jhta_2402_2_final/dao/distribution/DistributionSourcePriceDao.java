package org.example.jhta_2402_2_final.dao.distribution;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.jhta_2402_2_final.model.dto.distribution.DistributionMaterialDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DistributionSourcePriceDao {
    List<DistributionMaterialDto> getMaterialWithBestSupplier();

    List<DistributionMaterialDto> getSourcePricesForKit(@Param("kitId") String kitId);

    List<DistributionMaterialDto> getBestSuppliers();

    List<DistributionMaterialDto> getOrderDetails();

    List<DistributionMaterialDto> getAllSourcePrices();

    List<DistributionMaterialDto> findSourcePricesByCategoryAndKeyword(String category, String keyword);
}


