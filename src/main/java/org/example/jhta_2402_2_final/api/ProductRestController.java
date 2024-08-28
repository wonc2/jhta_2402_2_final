package org.example.jhta_2402_2_final.api;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product/test")
public class ProductRestController {
    private final ProductService productService;

    @GetMapping("selectAll")
    ResponseEntity<List<Map<String, Object>>> selectAll(){
        List<Map<String, Object>> proudcts = productService.findAll();
        return ResponseEntity.ok().body(proudcts);
    }
}
