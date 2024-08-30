package org.example.jhta_2402_2_final.controller;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyDto;
import org.example.jhta_2402_2_final.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;


    @GetMapping("/main")
    public String productMainPage() {
        return "product/productMainPage";
    }
    @GetMapping("/adminMain")
    public String productMainPage(Model model, @RequestParam Map<String, Object> params) {
        // 모든 검색 조건 params 에 담김

        List<Map<String, Object>> productList = productService.getProductListByParams(params);
        List<ProductCompanyDto> companies = productService.getAllCompanies();
        List<Map<String, Object>> status = productService.getAllStatus();

        model.addAttribute("productList",productList);
        model.addAttribute("companies", companies);
        model.addAttribute("status", status);
        model.addAttribute("params", params);

        return "product/productAdminMainPage";
    }
    @GetMapping("/role")
    public String role(){
        return "product/roletest";
    }
}
