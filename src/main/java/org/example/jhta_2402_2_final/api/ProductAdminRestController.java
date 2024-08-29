package org.example.jhta_2402_2_final.api;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product/admin")
public class ProductAdminRestController {
    private final ProductService productService;

    @GetMapping("selectAll")
    ResponseEntity<List<Map<String, Object>>> selectAll(){
        List<Map<String, Object>> proudcts = productService.findAll();
        return ResponseEntity.ok().body(proudcts);
    }

    @GetMapping("selectAll/params")
    ResponseEntity<List<Map<String, Object>>> getProductListByParams(@RequestParam Map<String, Object> params){
        List<Map<String, Object>> products = productService.getProductListByParams(params);
        return ResponseEntity.ok().body(products);
    }
}
