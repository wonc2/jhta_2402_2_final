package org.example.jhta_2402_2_final.api.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.common.SourceDto;
import org.example.jhta_2402_2_final.service.product.ProductCompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product/company")
public class ProductCompanyRestController {
    private final ProductCompanyService productCompanyService;

    @GetMapping("{companyName}")
    public ResponseEntity<Map<String, Object>> getSourcesByCompanyName(@PathVariable String companyName){
        // http://localhost:8080/api/product/company/농심공장

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> productSourceList  = productCompanyService.getSourcesByCompanyName(companyName);
        List<SourceDto> sources = productCompanyService.getAllSources();

        response.put("sources", sources);
        response.put("productSourceList", productSourceList);

        return ResponseEntity.ok().body(response);
    }

    @PostMapping("{companyName}")
    public ResponseEntity<List<Map<String, Object>>> addSource(@PathVariable String companyName, @RequestBody Map<String ,Object> dataMap){
        // http://localhost:8080/api/product/company/농심공장
        //  {
        //    "companyId": "65151a22-649e-11ef-838c-0242ac120002", <<- ${companyId}
        //    "sourceId": "65155d6e-649e-11ef-838c-0242ac120002", <<- ${sourceId}
        //    "sourcePrice": 12345
        // }

        List<Map<String, Object>> companySourceList = productCompanyService.insertCompanySource(companyName, dataMap);
        return ResponseEntity.ok().body(companySourceList);
    }


        // 할일 list
    // 생산품 상세조회?  /main/{productCompanyName}/{companySourceId}
    // 생산품 등록 Post (1. 셀렉트로 source list가져와서 그중에서 고르고 + 2. 가격 등록)
    // 생산품 수정 Put (일단 가격 ?)
    // 생산품 삭제 Delete
}
