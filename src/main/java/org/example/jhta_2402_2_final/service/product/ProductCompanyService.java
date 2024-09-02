package org.example.jhta_2402_2_final.service.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.product.ProductCompanyDao;
import org.example.jhta_2402_2_final.model.dto.common.SourceDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCompanyService {
    private final ProductCompanyDao productCompanyDao;
    /* Company */

    /* 필요한 리스트 전부 가져옴 */
    public Map<String, Object> findAll(String companyName){
        // todo: responseData, productList -> dto
        Map<String,Object> responseData = new HashMap<>();
        // productList values: { companySourceId, sourceName, sourcePrice, companyName, companyAddress, companyId }
        responseData.put("companySourceList",productCompanyDao.getSourcesByCompanyName(companyName));
        responseData.put("sources", productCompanyDao.getAllSources());
        // responseData values: { Map, SourceDto }
        return responseData;
    }

    /*  생산업체 생산품 목록에 등록 (실제 생산 x, 생산품 등록임) */
    @Transactional
    public Map<String, Object> addSourceToCompany(String companyName, Map<String ,Object> paramData) {
        // todo: paramData -> dto

        paramData.put("companyId", productCompanyDao.getCompanyIdByName(companyName));
        // sourceId 값 있으면 스킵 -> 없으면 직접입력임 중복값 있으면 해당 sourceId 가져옴 -> 둘다 없을시 입력한 재료 SOURCE 테이블에 등록
        String sourceName = (String) paramData.get("sourceName");
        if (paramData.get("sourceId") == null) {
            String sourceId = productCompanyDao.getSourceIdByName(sourceName);
            if (sourceId != null) paramData.put("sourceId", sourceId);
            else {
                sourceId = UUID.randomUUID().toString();
                productCompanyDao.addSource(sourceId, sourceName);
                paramData.put("sourceId", sourceId);
            }
        }
        // 회사 생산품 목록에 등록
        // paramData: { companyId, sourceId, sourceName, sourcePrice }
        // 필요값: { companyId, sourceId, sourcePrice }
        productCompanyDao.addSourceToCompany(paramData);

        return findAll(companyName);
    }

    @Transactional
    public Map<String, Object> deleteSourceFromCompany(String companyName, String companySourceId) {
        // 필요값: { companyId, sourceId, sourcePrice } sourcePrice 는 없어도 될듯?
        productCompanyDao.deleteSourceFromCompany(companySourceId);
        return findAll(companyName);
    }
}
