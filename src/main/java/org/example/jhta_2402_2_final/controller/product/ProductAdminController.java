package org.example.jhta_2402_2_final.controller.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyInsertDto;
import org.example.jhta_2402_2_final.service.product.ProductAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product/admin")
public class ProductAdminController {

    private final ProductAdminService productAdminService;

    @GetMapping("/main")
    public String productMainPage() {
        return "product/productAdminMainPage";
    }
    @PostMapping("/insertProductCompany")
    public String insertProductCompany(ProductCompanyInsertDto productCompanyInsertDto){
        productAdminService.insertProductCompany(productCompanyInsertDto);
        String userId = productAdminService.getProductMemberId(productCompanyInsertDto.getUserEmail());
        String productCompanyId = productAdminService.getProductCompanyId(productCompanyInsertDto.getProductCompanyName());
        productAdminService.insertProductCompanyMember(userId,productCompanyId);
        return "redirect:/product/admin/main";
    }
}
