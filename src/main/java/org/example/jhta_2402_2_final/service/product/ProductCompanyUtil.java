package org.example.jhta_2402_2_final.service.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.product.ProductCompanyDao;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductCompanyUtil {
    private final ProductCompanyDao productCompanyDao;

//    // @@: 캐시 시간 설정, 로그아웃시 캐시 삭제, AOP 로 모든 companyService 메서드에 적용 ?
//    @Cacheable(value = "companyIdCache", key = "#companyName")
//    public void getCompanyId(String companyName) {
//        String companyId = productCompanyDao.getCompanyIdByName(companyName);
//        if (companyId == null) {
//            throw new IllegalArgumentException("등록된 생산 업체가 아닙니다: " + companyName);
//        }
////        return companyId;
//    }

}
