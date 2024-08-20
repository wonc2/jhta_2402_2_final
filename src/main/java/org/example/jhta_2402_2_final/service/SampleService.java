package org.example.jhta_2402_2_final.service;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.SampleDao;
import org.example.jhta_2402_2_final.model.dto.Sample;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SampleService {

    private final SampleDao sampleDao;

    public List<Sample> getAllSamples() {
        return sampleDao.findAll();
    }

    public Sample getSampleById(Long id) {
        return sampleDao.findById(id);
    }

    public int createSample(Sample sample) {
        return sampleDao.insert(sample);
    }

    public int updateSample(Sample sample) {
        return sampleDao.update(sample);
    }

    public int deleteSample(Long id) {
        return sampleDao.delete(id);
    }
}