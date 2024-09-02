package org.example.jhta_2402_2_final.api.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.common.StatusDto;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyDto;
import org.example.jhta_2402_2_final.service.product.ProductAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/product/admin")
public class ProductAdminRestController {
    private final ProductAdminService productAdminService;

    @GetMapping("selectAll")
    public ResponseEntity<List<Map<String, Object>>> selectAll(){
        // http://localhost:8080/api/product/admin/selectAll

        List<Map<String, Object>> proudcts = productAdminService.findAll();
        return ResponseEntity.ok().body(proudcts);
    }

    @GetMapping("selectAll/params")
    public ResponseEntity<Map<String, Object>> getProductListByParams(@RequestParam Map<String, Object> params){
        // http://localhost:8080/api/product/admin/selectAll/params?productCompanyId=&statusId=

        List<Map<String, Object>> products = productAdminService.getProductListByParams(params);
        List<ProductCompanyDto> companies = productAdminService.getAllCompanies();
        List<StatusDto> status = productAdminService.getAllStatus();

        Map<String, Object> response = new HashMap<>();
        response.put("products", products);
        response.put("companies", companies);
        response.put("status", status);
        response.put("params", params);

        return ResponseEntity.ok().body(response);
    }
}
