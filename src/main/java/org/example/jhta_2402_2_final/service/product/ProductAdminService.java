package org.example.jhta_2402_2_final.service.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.MemberDao;
import org.example.jhta_2402_2_final.dao.product.ProductAdminDao;
import org.example.jhta_2402_2_final.model.dto.MemberDto;
import org.example.jhta_2402_2_final.model.dto.common.SourceMinPriceDto;
import org.example.jhta_2402_2_final.model.dto.common.SourcePriceViewDto;
import org.example.jhta_2402_2_final.model.dto.common.StatusDto;
import org.example.jhta_2402_2_final.model.dto.product.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductAdminService {
    private final ProductAdminDao productDao;
    private final MemberDao memberDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    /* Admin */
    public List<Map<String, Object>> findAll(){
        return productDao.findAll();
    }

    // 검색 조건 포함 리스트 (검색 조건 없으면 findAll() 이랑 같은 기능, 이거 기본적으로 사용중)
    public List<Map<String, Object>> getProductListByParams(Map<String, Object> params) {
        // values: { 주문번호(productOrderId), 생산업체명(productCompanyName), 생산품(sourceName), 생산품가격(sourcePrice),
        //          갯수(quantity), 총주문가격(totalPrice), 주문일자(productOrderDate), 상태(productOrderStatus) }
        return productDao.getProductListByParams(params);
    }

    // 검색 조건 셀렉트에 뿌리는용
    public List<ProductCompanyDto> getAllCompanies() {
        return productDao.getAllCompanies();
    }
    public List<StatusDto> getAllStatus() {
        return productDao.getAllStatus();
    }
    public int production(ProductDto productDto) {
        return 0;
    }
    public List<SourcePriceViewDto> getProductSourceList(){
        return productDao.getProductSourceList();
    }
    public List<ProductOrderViewDto> getProductOrderList(){
        return productDao.getProductOrderList();
    }
    public List<ProductOrderViewDto> getProductOrderListProductName(String productName){
        return productDao.getProductOrderListProductName(productName);
    }
    public List<ProductOrderViewDto> getProductOrderListCompanyName(String companyName){
        return productDao.getProductOrderListCompanyName(companyName);
    }
    public List<ProductOrderViewDto> getProductOrderListCompanyNameProductName(String companyName , String productName){
        return productDao.getProductOrderListCompanyNameProductName(companyName,productName);
    }
    public List<SourcePriceViewDto> getSourcePriceListCompanyName(String companyName){
        return productDao.getSourcePriceListCompanyName(companyName);
    }
    public List<SourcePriceViewDto> getSourcePriceListProductName(String productName){
        return productDao.getSourcePriceListProductName(productName);
    }
    public List<SourcePriceViewDto> getSourcePriceListCompanyNameProductName(String companyName , String productName){
        return productDao.getSourcePriceListCompanyNameProductName(companyName,productName);
    }
    public List<SourceMinPriceDto> getSourceMinPriceListForChart(){
        return productDao.getSourceMinPriceListForChart();
    }
    public List<ProductCompanySourcePriceDto> getSourcePriceCompanyList(String productCompanyName){
        return productDao.getSourcePriceCompanyList(productCompanyName);
//        return productDao.getSourcePriceCompanyList();
    }
    public List<ProductOrderCountDto> getProductOrderCount(){
        return productDao.getProductOrderCount();
    }
    public List<ProductCountDto> getProductCountList(){
        return productDao.getProductCountList();
    }
    public int insertProductCompany(ProductCompanyInsertDto productCompanyInsertDto){
        MemberDto memberDto = MemberDto.builder()
                .userId(productCompanyInsertDto.getUserId())
                .userPassword(bCryptPasswordEncoder.encode(productCompanyInsertDto.getUserPassword()))
                .userName(productCompanyInsertDto.getUserName())
                .userEmail(productCompanyInsertDto.getUserEmail())
                .userTel(productCompanyInsertDto.getUserTel())
                .roleId("3")
                .build();
        memberDao.insertUser(memberDto);
        memberDao.insertRole(memberDto.getUserId());
        ProductCompanyDto productCompanyDto = ProductCompanyDto.builder()
                .productCompanyName(productCompanyInsertDto.getProductCompanyName())
                .productCompanyAddress(productCompanyInsertDto.getProductCompanyAddress())
                .build();
        productDao.insertProductCompany(productCompanyDto);
        return 0;
    }
}
