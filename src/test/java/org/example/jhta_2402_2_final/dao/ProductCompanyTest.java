package org.example.jhta_2402_2_final.dao;

import org.example.jhta_2402_2_final.dao.product.ProductCompanyDao;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyChartDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ProductCompanyTest {

    @Autowired
    private ProductCompanyDao productCompanyDao;

    @Test
    public void test01() {
        String a = productCompanyDao.getCompanyIdByUserId("1")
                .orElseThrow(() -> new IllegalArgumentException("생산 업체 직원이 아니거나 권한이 없음"));

    }

}
