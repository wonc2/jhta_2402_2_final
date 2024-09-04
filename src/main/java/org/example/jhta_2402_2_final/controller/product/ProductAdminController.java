package org.example.jhta_2402_2_final.controller.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.common.SourcePriceViewDto;
import org.example.jhta_2402_2_final.model.dto.product.ProductCompanyInsertDto;
import org.example.jhta_2402_2_final.model.dto.product.ProductOrderViewDto;
import org.example.jhta_2402_2_final.service.product.ProductAdminService;
import org.example.jhta_2402_2_final.util.Pagination;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product/admin")
public class ProductAdminController {

    private final ProductAdminService productAdminService;

    @GetMapping("/main")
    public String productMainPage(Model model , @RequestParam(value = "page" , defaultValue = "1") int page , @RequestParam(value = "pageScale" , defaultValue = "10") int pageScale,@RequestParam(value="productTableStatus" , defaultValue = "order") String productTableStatus) {
        System.out.println("productTableStatus=="+productTableStatus);
        if(productTableStatus.equals("order")){
            List<ProductOrderViewDto>productOrderList = productAdminService.getProductOrderList();
            List<ProductOrderViewDto>paginatedOrderList = List.of();
            Pagination pagination = Pagination.builder()
                    .totalContent(productOrderList.size())
                    .currentPage(page)
                    .pageScale(pageScale)
                    .blockScale(5)
                    .totalPage((int)Math.ceil((double)productOrderList.size()/pageScale))
                    .build();
            int currentBlock = (int) Math.ceil((double) pagination.getCurrentPage() / pagination.getBlockScale());
            int startPage = (currentBlock - 1) * pagination.getBlockScale() + 1;
            System.out.println("startpage=="+startPage);
            int endPage = Math.min(startPage + pagination.getBlockScale() - 1, pagination.getTotalPage());
            System.out.println("endpage ==" + endPage);
            pagination.setStartPage(startPage);
            pagination.setEndPage(endPage);

            int start = (page-1)*pageScale;
            int end = Math.min(page * pageScale, pagination.getTotalContent());
            if (start < pagination.getTotalContent()) {
                paginatedOrderList = productOrderList.subList(start, end);
            } else {
                paginatedOrderList = List.of(); // 잘못된 페이지의 경우 빈 리스트 반환
            }
            model.addAttribute("pagination",pagination);
            model.addAttribute("paginatedOrderList",paginatedOrderList);
        }
        if(productTableStatus.equals("production")){
            List<SourcePriceViewDto>sourcePriceList = productAdminService.getProductSourceList();
            List<SourcePriceViewDto>paginatedSourcePriceList = List.of();
            Pagination pagination = Pagination.builder()
                    .totalContent(sourcePriceList.size())
                    .currentPage(page)
                    .pageScale(pageScale)
                    .blockScale(5)
                    .totalPage((int)Math.ceil((double)sourcePriceList.size()/pageScale))
                    .build();
            int currentBlock = (int) Math.ceil((double) pagination.getCurrentPage() / pagination.getBlockScale());
            int startPage = (currentBlock - 1) * pagination.getBlockScale() + 1;
            System.out.println("startpage=="+startPage);
            int endPage = Math.min(startPage + pagination.getBlockScale() - 1, pagination.getTotalPage());
            System.out.println("endpage ==" + endPage);
            pagination.setStartPage(startPage);
            pagination.setEndPage(endPage);

            int start = (page-1)*pageScale;
            int end = Math.min(page * pageScale, pagination.getTotalContent());
            if (start < pagination.getTotalContent()) {
                paginatedSourcePriceList = sourcePriceList.subList(start, end);
            } else {
                paginatedSourcePriceList = List.of(); // 잘못된 페이지의 경우 빈 리스트 반환
            }
            model.addAttribute("pagination",pagination);
            model.addAttribute("paginatedSourcePriceList",paginatedSourcePriceList);
        }
        return "product/productAdminMainPage";
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
