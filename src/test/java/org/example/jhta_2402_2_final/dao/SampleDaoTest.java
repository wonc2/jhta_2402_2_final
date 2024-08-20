package org.example.jhta_2402_2_final.dao;

import org.example.jhta_2402_2_final.model.dto.Sample;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class SampleDaoTest {

    @Autowired
    private SampleDao sampleDao;

    @Test
    public void testSampleInsert() {
        // Given
        Sample sample = new Sample();
        sample.setId(1L);  // ID는 명시적으로 설정 (또는 ID 생성을 DB에 맡길 수 있음)
        sample.setName("샘플샘플");

        // When
        int rs = sampleDao.insert(sample);

        // Then
        assertThat(rs).isEqualTo(1); // 삽입된 행의 수가 1인지 확인

        // 추가 검증 - 데이터가 제대로 삽입되었는지 확인
        Sample sample02 = sampleDao.findById(1L).get();
        assertThat(sample02).isNotNull();
        assertThat(sample02.getName()).isEqualTo("샘플샘플");
    }

    @Test
    public void findAllSamples() {
        List<Sample> map = sampleDao.findAll();
        assertThat(map).isNotNull();

        for (Sample sample : map) {
            System.out.println(sample.getName());
        }
    }
    @Test
    public void testInsert() {
        Sample sample = new Sample(6L, "TestName");
        sampleDao.insert(sample);

        Sample reSample = sampleDao.findById(6L).get();
        assertNotNull(reSample);
        assertEquals("TestName", reSample.getName());
    }

}
