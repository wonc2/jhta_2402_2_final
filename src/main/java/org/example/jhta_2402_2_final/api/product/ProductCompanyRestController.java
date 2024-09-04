package org.example.jhta_2402_2_final.api.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.CustomUserDetails;
import org.example.jhta_2402_2_final.service.product.ProductCompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product/company")
public class ProductCompanyRestController {
    private final ProductCompanyService productCompanyService;

    @ModelAttribute("companyName")
    public String getCompanyName(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // return userDetails.getMemberDto().getUserName();
        return "농협";
    }

    @GetMapping("add")
    public ResponseEntity<Map<String, Object>> getSourcesByCompanyName(@ModelAttribute("companyName") String companyName){
        Map<String, Object> responseData  = productCompanyService.findAll(companyName);
        return ResponseEntity.ok().body(responseData);
    }

    @PostMapping("add")
    public ResponseEntity<Map<String, Object>> addSourceToCompany(@ModelAttribute("companyName") String companyName, @RequestBody Map<String ,Object> paramData){
        // RequestBody: { "sourceName": "버터", "sourcePrice": 5000 }
        Map<String, Object> responseData = productCompanyService.addSourceToCompany(companyName, paramData);
        return ResponseEntity.ok().body(responseData);
    }

    @PutMapping("add/{companySourceId}")
    public ResponseEntity<List<Map<String, Object>>> updateSource(@ModelAttribute("companyName") String companyName,
                                                            @PathVariable String companySourceId,
                                                            @RequestBody Map<String ,Object> paramData){
        List<Map<String, Object>> responseData = productCompanyService.sourcePriceUpdate(companyName, companySourceId, paramData);
        return null;
    }

    @DeleteMapping("add/{companySourceId}")
    public ResponseEntity<Map<String, Object>> deleteSourceFromCompany(@ModelAttribute("companyName") String companyName, @PathVariable String companySourceId){
        Map<String, Object> responseData = productCompanyService.deleteSourceFromCompany(companyName, companySourceId);
        return ResponseEntity.ok().body(responseData);
    }

    @PostMapping("produce")
    public ResponseEntity<List<Map<String, Object>>> produce(@ModelAttribute("companyName") String companyName, @RequestBody Map<String ,Object> paramData){
        // RequestBody: { "sourcePriceId": "sourcePriceUUID 넣어야함", "sourceQuantity": 30 }
        List<Map<String, Object>> responseData = productCompanyService.produceSource(companyName, paramData);
        return ResponseEntity.ok().body(responseData);
    }

    @GetMapping("produce")
    public ResponseEntity<List<Map<String, Object>>> getWarehouseSources(@ModelAttribute("companyName") String companyName){
        List<Map<String, Object>> responseData = productCompanyService.getWarehouseSources(companyName);
        return ResponseEntity.ok().body(responseData);
    }

    @GetMapping("order")
    public ResponseEntity<List<Map<String, Object>>> getOrderList(@ModelAttribute("companyName") String companyName, @RequestParam Map<String ,Object> paramData){
        List<Map<String, Object>> responseData = productCompanyService.getProductOrderList(companyName, paramData);
        return ResponseEntity.ok().body(responseData);
    }

    @PostMapping("order")
    public ResponseEntity<List<Map<String, Object>>> orderProcess(@ModelAttribute("companyName") String companyName, @RequestBody Map<String ,Object> paramData){
        List<Map<String, Object>> responseData = productCompanyService.orderProcess(companyName, paramData);
        return ResponseEntity.ok().body(responseData);
    }



    /* 할일 list */

    // 실제생산 리스트? 생산품중 생산중인 리스트 ?
    // 실제생산 수정
    // 실제생산 삭제?

    // 등록된 생산품 상세조회?  / 지금까지 생산한거 품목별로 리스트로 보여주기 ?
    // 등록된 생산품 수정 Put (일단 가격 ?)
    // 등록된 생산품 삭제 Delete

    // 차트에 입력할 값
}
