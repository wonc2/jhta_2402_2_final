package org.example.jhta_2402_2_final.service.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.product.ProductCompanyDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductCompanyService {
    private final ProductCompanyDao productCompanyDao;
    /* Company */
    public List<Map<String, Object>> getSourcesByCompanyName(String companyName) {
        return productCompanyDao.getSourcesByCompanyName(companyName); // values: { 'companySourceId', 'sourceName', 'sourcePrice', 'companyName', 'companyAddress' }
    }
}
