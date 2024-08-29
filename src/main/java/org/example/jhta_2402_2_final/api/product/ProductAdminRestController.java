package org.example.jhta_2402_2_final.api.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.service.product.ProductAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product/admin")
public class ProductAdminRestController {
    private final ProductAdminService productAdminService;

    @GetMapping("selectAll")
    ResponseEntity<List<Map<String, Object>>> selectAll(){
        List<Map<String, Object>> proudcts = productAdminService.findAll();
        return ResponseEntity.ok().body(proudcts);
    }

    @GetMapping("selectAll/params")
    ResponseEntity<List<Map<String, Object>>> getProductListByParams(@RequestParam Map<String, Object> params){
        List<Map<String, Object>> products = productAdminService.getProductListByParams(params);
        return ResponseEntity.ok().body(products);
    }
}
