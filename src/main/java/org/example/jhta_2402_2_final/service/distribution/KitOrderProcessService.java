package org.example.jhta_2402_2_final.service.distribution;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.distribution.KitOrderProcessDao;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class KitOrderProcessService {

    private final KitOrderProcessDao kitOrderProcessDao;
    public List<Map<String, Object>> findAllOrder() {
        return kitOrderProcessDao.findAllOrder();
    }

    public List<Map<String, Object>> findKitSource() { return kitOrderProcessDao.findKitSource();
    }
}
