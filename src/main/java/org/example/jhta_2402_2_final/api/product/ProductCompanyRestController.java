package org.example.jhta_2402_2_final.api.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.CustomUserDetails;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyChartDto;
import org.example.jhta_2402_2_final.model.dto.productCompany.*;
import org.example.jhta_2402_2_final.service.product.ProductCompanyService;
import org.springframework.http.HttpStatus;
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

    // companyId 가져와서 모든 컨트롤러에서 사용함
    @ModelAttribute("companyId")
    public String getCompanyId(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return productCompanyService.getCompanyIdByUserId(userDetails.getMemberDto().getUserId());
    }

    @GetMapping("add")
    public ResponseEntity<CompanySourceTableDto> getSourcesByCompanyName(@ModelAttribute("companyId") String companyId){
        CompanySourceTableDto response = productCompanyService.findAll(companyId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("add")
    public ResponseEntity<Void> addSourceToCompany(@ModelAttribute("companyId") String companyId, @RequestBody AddSourceDto paramData){
        AddSourceDto addSourceDto = paramData.toBuilder().companyId(companyId).build();
        productCompanyService.addSourceToCompany(addSourceDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("add/{companySourceId}")
    public ResponseEntity<Void> updateSource(@PathVariable String companySourceId, @RequestBody SourcePriceUpdateDto paramData){
        SourcePriceUpdateDto updateDto = paramData.toBuilder().companySourceId(companySourceId).build();
        productCompanyService.sourcePriceUpdate(updateDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("add/{companySourceId}")
    public ResponseEntity<Void> deleteSourceFromCompany(@PathVariable String companySourceId){
        productCompanyService.deleteSourceFromCompany(companySourceId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("produce")
    public ResponseEntity<Void> produce(@RequestBody Map<String ,Object> paramData){
        // RequestBody: { "sourcePriceId": "sourcePriceUUID 넣어야함", "sourceQuantity": 30 }
        productCompanyService.produceSource(paramData);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // @@: produce -> warehouse ?
    @GetMapping("produce")
    public ResponseEntity<List<ProductCompanyWarehouseDto>> getWarehouseSources(@ModelAttribute("companyId") String companyId){
        List<ProductCompanyWarehouseDto> response = productCompanyService.getWarehouseSources(companyId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("order")
    public ResponseEntity<List<ProductCompanyOrderDto>> getOrderList(@ModelAttribute("companyId") String companyId, @ModelAttribute ProductCompanySearchOptionDto paramData){
        ProductCompanySearchOptionDto searchOptionDto = paramData.toBuilder().companyId(companyId).build();
        List<ProductCompanyOrderDto> response= productCompanyService.getProductOrderList(searchOptionDto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("order")
    public ResponseEntity<Void> orderProcess(@RequestBody ProductCompanyOrderProcessDto paramData){
        productCompanyService.orderProcess(paramData);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("allCompanySources")
    public ResponseEntity<List<String>> selectAllCompanySource(@ModelAttribute("companyId") String companyId){
        List<String> responseData = productCompanyService.selectAllCompanySource(companyId);
        return ResponseEntity.ok().body(responseData);
    }

    @GetMapping("chart")
    public ResponseEntity<List<ProductCompanyChartDto>> getChart(@ModelAttribute("companyId") String companyId){
        List<ProductCompanyChartDto> response = productCompanyService.getChart(companyId);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("orderChart")
    public ResponseEntity<List<ProductCompanyChartDto>> orderChart(@ModelAttribute("companyId") String companyId, @ModelAttribute ProductCompanySearchOptionDto paramData){
        ProductCompanySearchOptionDto searchOptionDto = paramData.toBuilder().companyId(companyId).build();
        List<ProductCompanyChartDto> response = productCompanyService.orderChart(searchOptionDto);
        return ResponseEntity.ok().body(response);
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
