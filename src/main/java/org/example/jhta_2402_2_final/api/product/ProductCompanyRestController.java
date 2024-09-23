package org.example.jhta_2402_2_final.api.product;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.CustomUserDetails;
import org.example.jhta_2402_2_final.model.dto.common.ErrorResponse;
import org.example.jhta_2402_2_final.model.dto.productCompany.*;
import org.example.jhta_2402_2_final.service.product.ProductCompanyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product/company")
public class ProductCompanyRestController {
    private final ProductCompanyService productCompanyService;

    // companyId ( 사용자 정보 기반 단일 값 ) 조회
    @GetMapping("info")
    public  ResponseEntity<Map<String, String>> getCompanyIdInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok().body(Map.of("companyId", getCompanyId(userDetails)));
    }

    // 등록된 생산품 리스트, 등록안된 생산품 리스트 조회
    @GetMapping("sources")
    public ResponseEntity<CompanySourceTableDto> getSourcesByCompanyName(@AuthenticationPrincipal CustomUserDetails userDetails) {
        CompanySourceTableDto response = productCompanyService.findAll(getCompanyId(userDetails));
        return ResponseEntity.ok().body(response);
    }

    // 신규 생산품 등록
    @PostMapping("sources")
    public ResponseEntity<?> addSourceToCompany(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody AddSourceDto paramData) {
        AddSourceDto addSourceDto = paramData.toBuilder().companyId(getCompanyId(userDetails)).build();
        productCompanyService.addSourceToCompany(addSourceDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 생산품 가격 수정
    @PutMapping("sources/{companySourceId}")
    public ResponseEntity<?> updateSource(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable String companySourceId, @Valid @RequestBody SourcePriceUpdateDto paramData, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessages));
        }
        SourcePriceUpdateDto updateDto = paramData.toBuilder().companySourceId(companySourceId).build();
        productCompanyService.sourcePriceUpdate(getCompanyId(userDetails), updateDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 생산품 삭제
    @DeleteMapping("sources/{companySourceId}")
    public ResponseEntity<?> deleteSourceFromCompany(@PathVariable String companySourceId) {
        productCompanyService.deleteSourceFromCompany(companySourceId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 생산품 입고 ( 창고에 입고할 갯수 입력 후 등록 )
    @PostMapping("produce")
    public ResponseEntity<?> produce(@AuthenticationPrincipal CustomUserDetails userDetails, @Valid @RequestBody CompanySourceStackDto paramData, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessages));
        }
        productCompanyService.produceSource(getCompanyId(userDetails), paramData);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 창고 입출고 기록 리스트 조회
    @GetMapping("warehouse")
    public ResponseEntity<List<ProductCompanyWarehouseDto>> getWarehouseSources(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute ProductCompanySearchOptionDto paramData) {
        ProductCompanySearchOptionDto searchOptionDto = paramData.toBuilder().companyId(getCompanyId(userDetails)).build();
        List<ProductCompanyWarehouseDto> response = productCompanyService.getWarehouseSources(searchOptionDto);
        return ResponseEntity.ok().body(response);
    }

    // 창고 입고 기록 삭제
    @DeleteMapping("warehouse/{sourceWarehouseId}")
    public ResponseEntity<?> deleteWarehouseProduceLog(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable String sourceWarehouseId) {
        productCompanyService.deleteWarehouseProduceLog(getCompanyId(userDetails), sourceWarehouseId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 주문 리스트 조회
    @GetMapping("order")
    public ResponseEntity<List<ProductCompanyOrderDto>> getOrderList(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute ProductCompanySearchOptionDto paramData) {
        ProductCompanySearchOptionDto searchOptionDto = paramData.toBuilder().companyId(getCompanyId(userDetails)).build();
        List<ProductCompanyOrderDto> response = productCompanyService.getProductOrderList(searchOptionDto);
        return ResponseEntity.ok().body(response);
    }

    // 주문 처리
    @PostMapping("order")
    public ResponseEntity<?> orderProcess(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ProductCompanyOrderProcessDto paramData) {
        ProductCompanyOrderProcessDto orderProcessDto = paramData.toBuilder().companyId(getCompanyId(userDetails)).build();
        productCompanyService.orderProcess(orderProcessDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 주문 잠금 ( 다른 유저 접근 방지 )
    @PostMapping("lockOrder")
    public ResponseEntity<?> orderProcessLock(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ProductCompanyOrderProcessDto paramData) {
        ProductCompanyOrderProcessDto orderProcessDto = paramData.toBuilder().companyId(getCompanyId(userDetails)).build();
        productCompanyService.orderProcessLock(orderProcessDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 주문 처리중 ( 주문 처리중인지 아닌지 확인 처리중이면 시간 업데이트 -> 스케줄러 삭제 방지 )
    @PutMapping("lockOrder/{orderId}")
    public ResponseEntity<?> updateOrderTime(@PathVariable String orderId){
        productCompanyService.updateOrderTime(orderId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 주문 잠금 해제 ( 작업 완료, 작업 중단시 -> 잠금 해제 )
    @DeleteMapping("lockOrder/{orderId}")
    public ResponseEntity<?> unlockOrder(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable String orderId) {
        productCompanyService.unlockOrder(getCompanyId(userDetails), orderId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    // 등록된 생산품 리스트 조회
    @GetMapping("allCompanySources")
    public ResponseEntity<List<String>> selectAllCompanySource(@AuthenticationPrincipal CustomUserDetails userDetails) {
        List<String> responseData = productCompanyService.selectAllCompanySource(getCompanyId(userDetails));
        return ResponseEntity.ok().body(responseData);
    }

    // 차트 리스트 ( order, warehouse ) 조회
    @GetMapping("chart")
    public ResponseEntity<ProductCompanyChartResponseDto> getChart(@AuthenticationPrincipal CustomUserDetails userDetails, @ModelAttribute ProductCompanySearchOptionDto paramData) {
        ProductCompanySearchOptionDto searchOptionDto = paramData.toBuilder().companyId(getCompanyId(userDetails)).build();
        ProductCompanyChartResponseDto response = productCompanyService.getChart(searchOptionDto);
        return ResponseEntity.ok().body(response);
    }

    private String getCompanyId(CustomUserDetails userDetails){
        return productCompanyService.getCompanyIdByUserId(userDetails.getMemberDto().getUserId());
    }
}
