package org.example.jhta_2402_2_final.controller.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.common.AlertDto;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyInsertDto;
import org.example.jhta_2402_2_final.service.product.ProductAdminService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String insertProductCompany(ProductCompanyInsertDto productCompanyInsertDto , RedirectAttributes redirectAttributes){
        AlertDto alertDto = null;
        if(productAdminService.insertProductCompany(productCompanyInsertDto)==1){
            String userId = productAdminService.getProductMemberId(productCompanyInsertDto.getUserEmail());
            String productCompanyId = productAdminService.getProductCompanyId(productCompanyInsertDto.getProductCompanyName());
            productAdminService.insertProductCompanyMember(userId,productCompanyId);
            alertDto = AlertDto.builder()
                    .title("OK")
                    .text("등록되었습니다.")
                    .icon("success")
                    .build();
            redirectAttributes.addFlashAttribute("alertDto",alertDto);
        } else {
            alertDto = AlertDto.builder()
                    .title("Fail")
                    .text("등록에 실패했습니다.")
                    .icon("error")
                    .build();
            redirectAttributes.addFlashAttribute("alertDto",alertDto);
            return "redirect:/product/admin/main";
        }

        return "redirect:/product/admin/main";
    }
}
