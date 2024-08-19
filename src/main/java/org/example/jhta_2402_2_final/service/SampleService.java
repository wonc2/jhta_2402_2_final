package org.example.jhta_2402_2_final.service;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.dao.CommonDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SampleService {
    private final CommonDao commonDao;

    public Object selectAll(Object params){
//        params - entity, dto, map, String~ 등으로 넘어오면 다운 캐스팅해서 사용 혹은 파라미터에 타입 명시
//    ex) params : {
//                  option: ,
//                  orderBy:
//                 },

        String sql = "SampleMapper.selectAll";
        return commonDao.getList(sql, params);
    }

    public Object selectOne(Object params){
        String sql = "SampleMapper.selectOne";
        return commonDao.getOne(sql, params);
    }

    public Object insert(Object params){
        String sql = "SampleMapper.insert";

        // 실행후에 -> 새로운 리스트 select 해서 -> map에 담아서 내려보내도됨
        return null;
    }

    @Transactional
    public Object delete(Object params){
        String sql = "SampleMapper.delete";

        // 실행후에 -> 새로운 리스트 select 해서 -> map에 담아서 내려보내도됨
        return null;
    }

    public Object update(Object params){
        String sql = "SampleMapper.update";

        // 실행후에 -> 새로운 리스트 select 해서 -> map에 담아서 내려보내도됨
        return null;
    }





    public Object getTestApi(Object params){
        // api test용 코드
        Map<String, String> response = new HashMap<>();
        response.put("response", "success");
        response.put("response2", "fail");
        response.put("response3", "test");
        response.put("response4", "곧집에갈시간");
        return response;
    }
}
