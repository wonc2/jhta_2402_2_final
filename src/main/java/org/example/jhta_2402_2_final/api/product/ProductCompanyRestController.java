package org.example.jhta_2402_2_final.api.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.service.product.ProductCompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product/company")
public class ProductCompanyRestController {
    private final ProductCompanyService productCompanyService;

    @GetMapping("{companyName}")
    ResponseEntity<List<Map<String, Object>>> getSourcesByCompanyName(@PathVariable String companyName){
        List<Map<String, Object>> sources  = productCompanyService.getSourcesByCompanyName(companyName);
        return ResponseEntity.ok().body(sources);
    }
}
