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
    List<ProductOrderViewDto>productOrderSearchList;
    List<SourcePriceViewDto>sourcePriceSearchList;
    @GetMapping("/main/data/sourcePriceList")
    @ResponseBody
    public List<SourcePriceViewDto> getSourcePriceList(@RequestParam(value = "productTableStatus" ,required = false) String productTableStatus,@RequestParam(value = "page" , defaultValue = "1") int page , @RequestParam(value = "pageScale" , defaultValue = "10") int pageScale, Model model){
            sourcePriceList = productAdminService.getProductSourceList();
            return sourcePriceList;

    }

    @GetMapping("/main/data/productOrderList")
    @ResponseBody
    public List<ProductOrderViewDto> getproductOrderList(@RequestParam(value = "productTableStatus" ,required = false) String productTableStatus,@RequestParam(value = "page" , defaultValue = "1") int page , @RequestParam(value = "pageScale" , defaultValue = "10") int pageScale,Model model){
            return productOrderList = productAdminService.getProductOrderList();
    }
    @GetMapping("/main/data/productOrderSearchList")
    @ResponseBody
    public List<ProductOrderViewDto> getproductOrderSearchList(@RequestParam(value = "productTableStatus" ,required = false) String productTableStatus,
                                                               @RequestParam(value = "page" , defaultValue = "1") int page ,
                                                               @RequestParam(value = "pageScale" , defaultValue = "10") int pageScale,
                                                               @RequestParam(value = "productName" , required = false)String productName,
                                                               @RequestParam(value = "companyName" , required = false)String companyName){
        if (!companyName.isEmpty() && productName.isEmpty()){
            productOrderSearchList = productAdminService.getProductOrderListCompanyName(companyName);
        }else if(!productName.isEmpty() && companyName.isEmpty()){
            productOrderSearchList = productAdminService.getProductOrderListProductName(productName);
        }else if(!companyName.isEmpty() && !productName.isEmpty()){
            productOrderSearchList = productAdminService.getProductOrderListCompanyNameProductName(companyName,productName);
        }else if(companyName.isEmpty() && productName.isEmpty()){
            productOrderSearchList = productAdminService.getProductOrderList();
        }
        return productOrderSearchList;
    }
    @GetMapping("/main/data/sourcePriceSearchList")
    @ResponseBody
    public List<SourcePriceViewDto> getSourcePriceSearchList(@RequestParam(value = "page" , defaultValue = "1") int page ,
                                                             @RequestParam(value = "pageScale" , defaultValue = "10") int pageScale,
                                                             @RequestParam(value = "productName" , required = false)String productName,
                                                             @RequestParam(value = "companyName" , required = false)String companyName){
        if (!companyName.isEmpty() && productName.isEmpty()){
            sourcePriceSearchList = productAdminService.getSourcePriceListCompanyName(companyName);
        }else if(!productName.isEmpty() && companyName.isEmpty()){
            sourcePriceSearchList = productAdminService.getSourcePriceListProductName(productName);
        }else if(!companyName.isEmpty() && !productName.isEmpty()){
            sourcePriceSearchList = productAdminService.getSourcePriceListCompanyNameProductName(companyName,productName);
        }else if(companyName.isEmpty() && productName.isEmpty()){
            sourcePriceSearchList = productAdminService.getProductSourceList();
        }
        return sourcePriceSearchList;
    }
    @GetMapping("/main/data/productOrderChart")
    @ResponseBody
    public List<ProductOrderViewDto> getProductOrderChart(){
        return productOrderList = productAdminService.getProductOrderList();
    }
}
