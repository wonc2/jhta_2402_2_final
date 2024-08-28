package org.example.jhta_2402_2_final.controller;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;
    @GetMapping("/main")
    public String productMainPage(Model model){

        List<Map<String,Object>> productList = productService.findAll();
        model.addAttribute("productList",productList);
        return "product/productMainPage";
    }
}
