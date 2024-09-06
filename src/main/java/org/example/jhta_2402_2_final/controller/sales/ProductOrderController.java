package org.example.jhta_2402_2_final.controller.sales;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.sales.ProductOrderDetailDto;
import org.example.jhta_2402_2_final.model.dto.sales.ProductOrderLogDetailDto;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductOrderController {
    private final SalesService salesService;

    @GetMapping("/sales/product/order")
    public String ProductOrder(Model model) {
        List<ProductOrderDetailDto> productOrder = salesService.selectProductOrder();
        model.addAttribute("productOrder",productOrder);

        List<ProductOrderLogDetailDto> productOrderLog = salesService.selectProductOrderLog();
        model.addAttribute("productOrderLog", productOrderLog);

        return "sales/product";
    }
}
