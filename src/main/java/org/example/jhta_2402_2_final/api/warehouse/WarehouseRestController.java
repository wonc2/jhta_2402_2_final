package org.example.jhta_2402_2_final.api.warehouse;

import lombok.RequiredArgsConstructor;
import org.example.jhta_2402_2_final.model.dto.distribution.wareHouseChartDto;
import org.example.jhta_2402_2_final.service.distribution.LogisticsWareHouseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wareHouse")
public class WarehouseRestController {

   private final LogisticsWareHouseService logisticsWareHouseService;
    @GetMapping("/getSource")
    public List<Map<String, Object>> getSource() {
        List<Map<String, Object>> sourceList=logisticsWareHouseService.getSource();

        return sourceList;
    }
    @GetMapping("/getCompany")
    public List<Map<String, Object>> getCompany() {
        List<Map<String, Object>> companyList=logisticsWareHouseService.getCompany();

        return companyList;
    }

    @GetMapping("/chart")
    public List<wareHouseChartDto> getChartData(@RequestParam String sourceName,
                                                @RequestParam String companyName,
                                                @RequestParam String startDate,
                                                @RequestParam String endDate) {

        wareHouseChartDto dto = wareHouseChartDto.builder()
                .companyName(companyName)
                .startDate(startDate)
                .endDate(endDate)
                .sourceName(sourceName)
                .build();

        List<wareHouseChartDto> result = logisticsWareHouseService.getChartData(dto);
        return result;
    }
}
