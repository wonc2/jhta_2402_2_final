package org.example.jhta_2402_2_final.service.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.MemberDao;
import org.example.jhta_2402_2_final.dao.product.ProductAdminDao;
import org.example.jhta_2402_2_final.model.dto.MemberDto;
import org.example.jhta_2402_2_final.model.dto.common.StatusDto;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyDto;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyInsertDto;
import org.example.jhta_2402_2_final.model.dto.product.ProductDto;
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
