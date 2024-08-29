package org.example.jhta_2402_2_final.service;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.ProductDao;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductDao productDao;

    // 주문번호(productOrderId), 생산업체명(productCompanyName), 생산품(sourceName), 생산품가격(sourcePrice),
    // 갯수(quantity), 총주문가격(totalPrice), 주문일자(productOrderDate), 상태(productOrderStatus)
    // 아직 dto 안만들어서 map 으로 내리는중

    public List<Map<String, Object>> findAll(){
        return productDao.findAll();
    }

    // 검색 조건 포함 리스트 (검색 조건 없으면 findAll() 이랑 같은 기능, 이거 기본적으로 사용중)
    public List<Map<String, Object>> getProductListByParams(Map<String, Object> params) {
        return productDao.getProductListByParams(params);
    }

    // 검색 조건 셀렉트에 뿌리는용
    public List<ProductCompanyDto> getAllCompanies() {
        return productDao.getAllCompanies();
    }
    // StatusDto 생성 + CommonService 로 옮길수도
    public List<Map<String, Object>> getAllStatus() {
        return productDao.getAllStatus();
    }
}
