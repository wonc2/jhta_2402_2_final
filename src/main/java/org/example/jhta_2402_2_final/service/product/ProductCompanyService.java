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

    public List<Map<String, Object>> getSourcesByCompanyName(String companyName) {
        return productCompanyDao.getSourcesByCompanyName(companyName); // values: { 'companySourceId', 'sourceName', 'sourcePrice', 'companyName', 'companyAddress' }
    }


    // Select 에 뿌리는 용
    public List<SourceDto> getAllSources(){
        return productCompanyDao.getAllSources();
    }

    /* Insert 회사별 생산품 리스트 */
    @Transactional
    public List<Map<String, Object>> insertCompanySource(String companyName, Map<String ,Object> dataMap) {
        String sourceName = (String) dataMap.get("sourceName");
        if (productCompanyDao.duplicationSource((String) dataMap.get("sourceName")) < 1) productCompanyDao.addSource(sourceName);
        String sourceId = productCompanyDao.getSourceIdByName(sourceName);
        String companyId = productCompanyDao.getCompanyIdByName(companyName);

        dataMap.put("sourceId", sourceId);
        dataMap.put("companyId", companyId);

        productCompanyDao.insertCompanySource(dataMap); // params: #{companyId}, #{sourceId}, #{sourcePrice})
        return productCompanyDao.getSourcesByCompanyName(companyName);
    }
}
