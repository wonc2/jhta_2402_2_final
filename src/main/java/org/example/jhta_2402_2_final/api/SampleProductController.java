package org.example.jhta_2402_2_final.api;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.product.ProductDto;
import org.example.jhta_2402_2_final.service.SampleProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@ResponseBody
@RequestMapping("/api/product")
public class SampleProductController {

    private final SampleProductService sampleProductService;


    @GetMapping("/selectAll")
    public String selectAll(){
        return "/selectAll";
    }

    @PostMapping("/selectAll")
    public  List<ProductDto> selectAllProcess(Model model){
        List<ProductDto> productDtos = sampleProductService.selectAll();
        model.addAttribute("products", productDtos);
        return productDtos;
    }

    @GetMapping("/insertProduct")
    public void insertProduct(@ModelAttribute ProductDto productDto) {
        ProductDto insertProductDto = ProductDto.builder()
                .productName(productDto.getProductName())
                .build();
        sampleProductService.insertNewProduct(insertProductDto);
    }

}
