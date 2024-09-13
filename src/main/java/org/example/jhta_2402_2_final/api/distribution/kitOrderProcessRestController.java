package org.example.jhta_2402_2_final.api.distribution;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.distribution.WareHouseStockChartDto;
import org.example.jhta_2402_2_final.service.distribution.KitOrderProcessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("distribution/api")
public class kitOrderProcessRestController {
    private final KitOrderProcessService kitOrderProcessService;

    @GetMapping("/warehouseStockChart")
    public ResponseEntity<List<WareHouseStockChartDto>> warehouseStockChart(@ModelAttribute("logisticsWarehouseId") String logisticsId) {

        List<WareHouseStockChartDto> response = kitOrderProcessService.findLogisticsWarehouseStock();
        return ResponseEntity.ok().body(response);
    }
}

