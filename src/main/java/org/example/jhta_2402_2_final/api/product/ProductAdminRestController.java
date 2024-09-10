package org.example.jhta_2402_2_final.api.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.common.SourceMinPriceDto;
import org.example.jhta_2402_2_final.model.dto.common.SourcePriceViewDto;
import org.example.jhta_2402_2_final.model.dto.product.*;
import org.example.jhta_2402_2_final.service.product.ProductAdminService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product/admin")
public class ProductAdminRestController {
    private final ProductAdminService productAdminService;
    List<ProductOrderViewDto> productOrderList;
    List<ProductOrderQuantityDto> productOrderQuantityList;
    List<ProductOrderViewDto>productOrderSearchList;
    List<SourcePriceViewDto>sourcePriceList;
    List<SourcePriceViewDto>sourcePriceSearchList;
    List<SourceMinPriceDto>sourceMinPriceList;
    List<ProductCompanySourcePriceDto>sourcePriceCompanyList;
    List<ProductOrderCountDto>productOrderCountList;
    List<ProductCountDto> productCountList;
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
    public List<ProductOrderViewDto> getProductOrderChart(@RequestParam(value = "productCompanyName",defaultValue = "none")String companyName){
        if(companyName.equals("none")){
            productOrderList = productAdminService.getProductOrderList();
        }else if(!companyName.equals("none")){
            productOrderList = productAdminService.getProductOrderListCompanyName(companyName);
        }
        return productOrderList;
    }
    @GetMapping("/main/data/productOrderQuantityChart")
    public List<ProductOrderQuantityDto> getProductOrderQuantityChart(@RequestParam(value = "productCompanyName",defaultValue = "none")String productCompanyName){
            productOrderQuantityList = productAdminService.getProductOrderQuantityListCompanyName(productCompanyName);
        return productOrderQuantityList;
    }
    @GetMapping("/main/data/sourcePriceChart")
    public List<SourcePriceViewDto> getSourcePriceChart(){
        sourcePriceList = productAdminService.getProductSourceList();
        return sourcePriceList;
    }
    @GetMapping("/main/data/sourceMinPriceChart")
    public List<SourceMinPriceDto> getSourceMinPriceChart(){
        sourceMinPriceList = productAdminService.getSourceMinPriceListForChart();
        return sourceMinPriceList;
    }
    @GetMapping("/main/data/sourcePriceCompanyChart")
    public List<ProductCompanySourcePriceDto> getSourcePriceCompany(String productCompanyName){
        return sourcePriceCompanyList = productAdminService.getSourcePriceCompanyList(productCompanyName);
//        return sourcePriceCompanyList = productAdminService.getSourcePriceCompanyList();
    }
    @GetMapping("/main/data/productOrderCountChart")
    public List<ProductOrderCountDto> getProductOrderCount(){
        return productOrderCountList = productAdminService.getProductOrderCount();
    }
    @GetMapping("/main/data/productCountListChart")
    public List<ProductCountDto> getProductCountList(){
        return productCountList = productAdminService.getProductCountList();
    }

}
