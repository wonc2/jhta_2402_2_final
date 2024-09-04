package org.example.jhta_2402_2_final.api.product;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.common.SourcePriceViewDto;
import org.example.jhta_2402_2_final.model.dto.product.ProductOrderViewDto;
import org.example.jhta_2402_2_final.service.product.ProductAdminService;
import org.example.jhta_2402_2_final.util.Pagination;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product/admin")
public class ProductAdminRestController {
    private final ProductAdminService productAdminService;
    List<SourcePriceViewDto>paginatedSourcePriceList;
    List<ProductOrderViewDto>paginatedOrderList;
    @GetMapping("/main/data/sourcePriceList")
    @ResponseBody
    public List<SourcePriceViewDto> getSourcePriceList(@RequestParam(value = "productTableStatus" ,required = false) String productTableStatus,@RequestParam(value = "page" , defaultValue = "1") int page , @RequestParam(value = "pageScale" , defaultValue = "10") int pageScale, Model model){
        if(productTableStatus.equals("production")){
            System.out.println("productTableStatus ==" + productTableStatus + "page ==" + page);
            List<SourcePriceViewDto>sourcePriceList = productAdminService.getProductSourceList();
            paginatedSourcePriceList = List.of();
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
        }
            return paginatedSourcePriceList;
    }
    @GetMapping("/main/data/productOrderList")
    @ResponseBody
    public List<ProductOrderViewDto> getproductOrderList(@RequestParam(value = "productTableStatus" ,required = false) String productTableStatus,@RequestParam(value = "page" , defaultValue = "1") int page , @RequestParam(value = "pageScale" , defaultValue = "10") int pageScale,Model model){
        if(productTableStatus.equals("order")){
            System.out.println("productTableStatus ==" + productTableStatus + "page ==" + page);
            List<ProductOrderViewDto>productOrderList = productAdminService.getProductOrderList();
            paginatedOrderList = List.of();
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
        }
            return paginatedOrderList;
    }
}
