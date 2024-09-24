package org.example.jhta_2402_2_final.service.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.exception.dao.MemberDao;
import org.example.jhta_2402_2_final.exception.dao.product.ProductAdminDao;
import org.example.jhta_2402_2_final.model.dto.member.MemberDto;
import org.example.jhta_2402_2_final.model.dto.common.SourceMinPriceDto;
import org.example.jhta_2402_2_final.model.dto.common.SourcePriceViewDto;
import org.example.jhta_2402_2_final.model.dto.product.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAdminService {
    private final ProductAdminDao productDao;
    private final MemberDao memberDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    /* Admin */
    public String getProductMemberId(String userEmail){
        return memberDao.getProductMemberId(userEmail);
    }
    public String getProductCompanyId(String productCompanyName){
        return productDao.getProductCompanyId(productCompanyName);
    }
    public int insertProductCompanyMember(String userId , String productCompanyId){
       return memberDao.insertProductCompanyMember(userId,productCompanyId);
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
    public List<ProductOrderQuantityDto> getProductOrderQuantityListCompanyName(String companyName){
        return productDao.getProductOrderQuantityListCompanyName(companyName);
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
        if(memberDto.getUserId().isEmpty()){
            return 0;
        }
        if(memberDto.getUserPassword().isEmpty()){
            return 0;
        }
        if(memberDto.getUserName().isEmpty()){
            return 0;
        }
        if(memberDto.getUserTel().isEmpty()){
            return 0;
        }
        if(memberDto.getUserEmail().isEmpty()){
            return 0;
        }
        if(memberDao.checkUserIdProduct(memberDto.getUserId())==1){
            return 0;
        }else {
            memberDao.insertUser(memberDto);
            memberDao.insertRole(memberDto.getUserId());
            ProductCompanyDto productCompanyDto = ProductCompanyDto.builder()
                    .productCompanyName(productCompanyInsertDto.getProductCompanyName())
                    .productCompanyAddress(productCompanyInsertDto.getProductCompanyAddress())
                    .build();
            String companyId = productDao.getProductCompanyId(productCompanyDto.getProductCompanyName());
            if(companyId == null || companyId.isEmpty()){
                productDao.insertProductCompany(productCompanyDto);
            }
        }
        return 1;
    }
}
