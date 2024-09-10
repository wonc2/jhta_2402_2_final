package org.example.jhta_2402_2_final.api.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.CustomUserDetails;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyChartDto;
import org.example.jhta_2402_2_final.service.product.ProductCompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product/company")
public class ProductCompanyRestController {
    private final ProductCompanyService productCompanyService;

    @ModelAttribute("companyId")
    public String getCompanyId(@AuthenticationPrincipal CustomUserDetails userDetails) { // 개발 편의상 설정
        return productCompanyService.getCompanyIdByUserId(userDetails.getMemberDto().getUserId());
    }

    @GetMapping("add")
    public ResponseEntity<Map<String, Object>> getSourcesByCompanyName(@ModelAttribute("companyId") String companyId){
        Map<String, Object> responseData  = productCompanyService.findAll(companyId);
        return ResponseEntity.ok().body(responseData);
    }

    @PostMapping("add")
    public ResponseEntity<Map<String, Object>> addSourceToCompany(@ModelAttribute("companyId") String companyId, @RequestBody Map<String ,Object> paramData){
        // RequestBody: { "sourceName": "버터", "sourcePrice": 5000 }
        Map<String, Object> responseData = productCompanyService.addSourceToCompany(companyId, paramData);
        return ResponseEntity.ok().body(responseData);
    }

    @PutMapping("add/{companySourceId}")
    public ResponseEntity<List<Map<String, Object>>> updateSource(@ModelAttribute("companyId") String companyId,
                                                                  @PathVariable String companySourceId,
                                                                  @RequestBody Map<String ,Object> paramData){
        List<Map<String, Object>> responseData = productCompanyService.sourcePriceUpdate(companyId, companySourceId, paramData);
        return null;
    }

    @DeleteMapping("add/{companySourceId}")
    public ResponseEntity<Map<String, Object>> deleteSourceFromCompany(@ModelAttribute("companyId") String companyId, @PathVariable String companySourceId){
        Map<String, Object> responseData = productCompanyService.deleteSourceFromCompany(companyId, companySourceId);
        return ResponseEntity.ok().body(responseData);
    }

    @PostMapping("produce")
    public ResponseEntity<List<Map<String, Object>>> produce(@ModelAttribute("companyId") String companyId, @RequestBody Map<String ,Object> paramData){
        // RequestBody: { "sourcePriceId": "sourcePriceUUID 넣어야함", "sourceQuantity": 30 }
        List<Map<String, Object>> responseData = productCompanyService.produceSource(companyId, paramData);
        return ResponseEntity.ok().body(responseData);
    }

    @GetMapping("produce")
    public ResponseEntity<List<Map<String, Object>>> getWarehouseSources(@ModelAttribute("companyId") String companyId){
        List<Map<String, Object>> responseData = productCompanyService.getWarehouseSources(companyId);
        return ResponseEntity.ok().body(responseData);
    }

    @GetMapping("order")
    public ResponseEntity<List<Map<String, Object>>> getOrderList(@ModelAttribute("companyId") String companyId, @RequestParam Map<String ,Object> paramData){
        List<Map<String, Object>> responseData = productCompanyService.getProductOrderList(companyId, paramData);
        return ResponseEntity.ok().body(responseData);
    }

    @PostMapping("order")
    public ResponseEntity<List<Map<String, Object>>> orderProcess(@ModelAttribute("companyId") String companyId, @RequestBody Map<String ,Object> paramData){
        List<Map<String, Object>> responseData = productCompanyService.orderProcess(companyId, paramData);
        return ResponseEntity.ok().body(responseData);
    }
    @GetMapping("allCompanySources")
    public ResponseEntity<List<String>> selectAllCompanySource(@ModelAttribute("companyId") String companyId){
        List<String> responseData = productCompanyService.selectAllCompanySource(companyId);
        return ResponseEntity.ok().body(responseData);
    }

    @GetMapping("chart")
    public ResponseEntity<List<ProductCompanyChartDto>> getChart(@ModelAttribute("companyId") String companyId){
        List<ProductCompanyChartDto> responseData = productCompanyService.getChart(companyId);
        return ResponseEntity.ok().body(responseData);
    }
    @GetMapping("orderChart")
    public ResponseEntity<List<Map<String, Object>>> orderChart(@ModelAttribute("companyId") String companyId, @RequestParam Map<String ,Object> paramData){
        List<Map<String, Object>> responseData = productCompanyService.orderChart(companyId, paramData);
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
