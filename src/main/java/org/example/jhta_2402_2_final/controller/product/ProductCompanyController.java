package org.example.jhta_2402_2_final.controller.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.common.SourceDto;
import org.example.jhta_2402_2_final.service.product.ProductCompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product/company")
public class ProductCompanyController {

    private final ProductCompanyService productCompanyService;

    @GetMapping("/{companyName}")
    public String productMainPage(Model model, @PathVariable String companyName) {
        // 생산품 리스트 ( 일단 로그인 ID(UNIQUE) = 생산업체 Name으로 구분 )
        // 리스트{ 생산중인 재료 가격 및 이름, 또 뭐 기타등등 필요한거 있으면 추가 }

        List<Map<String, Object>> productSourceList = productCompanyService.getSourcesByCompanyName(companyName);
        List<SourceDto> sources = productCompanyService.getAllSources();

        model.addAttribute("productSourceList", productSourceList);
        model.addAttribute("sources", sources);

        return "product/productCompanyMainPage";
    }

    // 생산품 상세조회?  /main/{productCompanyName}/{companySourceId}
    // 생산품 등록 Post (1. 셀렉트로 source list가져와서 그중에서 고르고 + 2. 가격 등록)
    // 생산품 수정 Put (일단 가격 ?)
    // 생산품 삭제 Delete
}
