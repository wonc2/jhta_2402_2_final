package org.example.jhta_2402_2_final.controller.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.CustomUserDetails;
import org.example.jhta_2402_2_final.model.dto.common.SourceDto;
import org.example.jhta_2402_2_final.service.product.ProductCompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product/company")
public class ProductCompanyController {

    private final ProductCompanyService productCompanyService;

    @GetMapping()
    public String productMainPage() {
        // 생산품 리스트 ( 일단 로그인 ID(UNIQUE) = 생산업체 Name으로 구분? )

        return "product/productCompanyMainPage";
    }
}
