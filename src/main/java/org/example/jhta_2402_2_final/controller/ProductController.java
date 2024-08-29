package org.example.jhta_2402_2_final.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/main")
    public String productMainPage(){
        return "product/productMainPage";
    }
}
