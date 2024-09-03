package org.example.jhta_2402_2_final.controller.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.common.SourcePriceViewDto;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyInsertDto;
import org.example.jhta_2402_2_final.service.product.ProductAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product/admin")
public class ProductAdminController {

    private final ProductAdminService productAdminService;
    List<SourcePriceViewDto>sourcePriceList;
    @GetMapping("/main")
    public String productMainPage(@RequestParam(value = "productTableStatus" , required = false) String productTableStatus) {
        return "product/productAdminMainPage";
    }
    @GetMapping("/main/data/sourcePriceList")
    @ResponseBody
    public List<SourcePriceViewDto> getSourcePriceList(@RequestParam(value = "productTableStatus" ,required = false) String productTableStatus){
        if(productTableStatus==null){
            productTableStatus = "order";
        }
        if(productTableStatus.equals("production")){
            sourcePriceList = productAdminService.getProductSourceList();
            System.out.println(sourcePriceList.toString());
        }
        if(productTableStatus.equals("order")){

        }
        return sourcePriceList;
    }
    @GetMapping("/role")
    public String role(){
        return "product/roletest";
    }
    @PostMapping("/insertProductCompany")
    public String insertProductCompany(ProductCompanyInsertDto productCompanyInsertDto){
        productAdminService.insertProductCompany(productCompanyInsertDto);
        return "redirect :/product/admin/main";
    }
}
