package org.example.jhta_2402_2_final.dao.product;

import org.apache.ibatis.annotations.Mapper;
import org.example.jhta_2402_2_final.model.dto.common.SourceMinPriceDto;
import org.example.jhta_2402_2_final.model.dto.common.SourcePriceViewDto;
import org.example.jhta_2402_2_final.model.dto.common.StatusDto;
import org.example.jhta_2402_2_final.model.dto.product.*;

import java.util.List;
import java.util.Map;
@Mapper
public interface ProductAdminDao {
    // 리스트 가져옴
    int insertProductCompany(ProductCompanyDto productCompanyDto);
    String getProductCompanyId(String productCompanyName);
    List<SourcePriceViewDto> getProductSourceList();
    List<ProductOrderViewDto> getProductOrderList();
    List<ProductOrderViewDto> getProductOrderListProductName(String productName);
    List<ProductOrderViewDto> getProductOrderListCompanyName(String companyName);
    List<ProductOrderQuantityDto> getProductOrderQuantityListCompanyName(String companyName);
    List<ProductOrderViewDto> getProductOrderListCompanyNameProductName (String companyName , String productName);
    List<SourcePriceViewDto> getSourcePriceListCompanyName(String companyName);
    List<SourcePriceViewDto> getSourcePriceListProductName(String productName);
    List<SourcePriceViewDto> getSourcePriceListCompanyNameProductName(String companyName , String productName);
    List<SourceMinPriceDto> getSourceMinPriceListForChart();
    List<ProductCompanySourcePriceDto> getSourcePriceCompanyList(String productCompanyName);
    List<ProductOrderCountDto> getProductOrderCount();
    List<ProductCountDto> getProductCountList();
}
