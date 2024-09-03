package org.example.jhta_2402_2_final.controller.product;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/product/company")
public class ProductCompanyController {
    @GetMapping()
    public String productMainPage() {return "product/productCompanyMainPage";}
}
