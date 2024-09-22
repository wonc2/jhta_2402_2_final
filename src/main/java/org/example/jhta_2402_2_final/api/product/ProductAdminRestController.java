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
    /*
    * [code-review] 아래처럼 미리 변수를 선언해야 할 이유가 있을까요? 코드의 깔끔함 때문이라면 메서드에서 선언해서 처리해주세요.
    * 그리고 해당 방법으로 하게되면 요청이 많아질 경우 A 유저가 요청한 값이 B 유저에게 보여질 수 있습니다.
    * */
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
        /*
        * [code-review] 많은 고민이 느껴지는 if 구문 이네요. 해결 방법으로는 mybatis 사용중 이시니
        * 실제 쿼리문에서 다이나믹하게 처리할 수 있는 방법이 있습니다. productCompany.xml 사용하시고 계신 분이 있으니 참고하시는 것도 좋아 보입니다.
        * 추가적으로는 QueryDsl도 있는데 해당 프로젝트에는 적용이 지금에는 힘들기 때문에 다른 프로젝트에서 적용해보시길 바랍니다.
        * */
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
