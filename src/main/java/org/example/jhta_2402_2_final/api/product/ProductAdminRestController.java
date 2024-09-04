package org.example.jhta_2402_2_final.api.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.common.SourcePriceViewDto;
import org.example.jhta_2402_2_final.model.dto.product.ProductOrderViewDto;
import org.example.jhta_2402_2_final.service.product.ProductAdminService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product/admin")
public class ProductAdminRestController {
    private final ProductAdminService productAdminService;
    List<SourcePriceViewDto>paginatedSourcePriceList;
    List<SourcePriceViewDto>sourcePriceList;
    List<ProductOrderViewDto>paginatedOrderList;
    List<ProductOrderViewDto>productOrderList;
    @GetMapping("/main/data/sourcePriceList")
    @ResponseBody
    public List<SourcePriceViewDto> getSourcePriceList(@RequestParam(value = "productTableStatus" ,required = false) String productTableStatus,@RequestParam(value = "page" , defaultValue = "1") int page , @RequestParam(value = "pageScale" , defaultValue = "10") int pageScale, Model model){
            sourcePriceList = productAdminService.getProductSourceList();
            return sourcePriceList;

    }
    @GetMapping("/main/data/productOrderList")
    @ResponseBody
    public List<ProductOrderViewDto> getproductOrderList(@RequestParam(value = "productTableStatus" ,required = false) String productTableStatus,@RequestParam(value = "page" , defaultValue = "1") int page , @RequestParam(value = "pageScale" , defaultValue = "10") int pageScale,Model model){
            productOrderList = productAdminService.getProductOrderList();
            return productOrderList;
    }
}
