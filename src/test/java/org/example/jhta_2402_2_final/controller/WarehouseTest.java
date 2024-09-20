package org.example.jhta_2402_2_final.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.jhta_2402_2_final.model.dto.distribution.CombineLogDto;
import org.example.jhta_2402_2_final.model.dto.distribution.LogisticsWareHouseDto;
import org.example.jhta_2402_2_final.service.distribution.LogisticsWareHouseService;
import org.example.jhta_2402_2_final.service.sales.SalesService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Nested
@SpringBootTest
@AutoConfigureMockMvc
public class WarehouseTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LogisticsWareHouseService logisticsWareHouseService;

    @MockBean
    private SalesService salesService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        objectMapper = new ObjectMapper();
        System.out.println("Test Before");
    }

    @AfterEach
    public void teardown() {
        System.out.println("Test After");
    }




    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /wareHouse/selectAll - 성공 케이스")
    public void testSelectAll() throws Exception {
        List<LogisticsWareHouseDto> warehouseList = Arrays.asList(new LogisticsWareHouseDto(), new LogisticsWareHouseDto());

        Mockito.when(logisticsWareHouseService.selectAllLogisticsWarehouse()).thenReturn(warehouseList);
        Mockito.when(salesService.getMinSourcePrice()).thenReturn(Collections.emptyList());
        Mockito.when(salesService.getAllKitOrderDetail()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/wareHouse/selectAll"))
                .andExpect(status().isOk())
                .andExpect(view().name("distribution/wareHouseList"))
                .andExpect(model().attributeExists("warehouseList"))
                .andExpect(model().attributeExists("minSourcePrice"))
                .andExpect(model().attributeExists("kitOrderDetails"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /wareHouse/sales - 성공 케이스")
    public void testSalesSuccess() throws Exception {
        String kitOrderId = UUID.randomUUID().toString();
        List<String> sourceNames = Arrays.asList("Source1", "Source2");
        List<Integer> itemQuantities = Arrays.asList(10, 20);

        String sourceNamesJson = objectMapper.writeValueAsString(sourceNames);
        String itemQuantitiesJson = objectMapper.writeValueAsString(itemQuantities);

        // Mocking 서비스 메서드 호출
        Mockito.doNothing().when(logisticsWareHouseService).updateStackBySourceName(Mockito.any());
        Mockito.when(salesService.updateKitOrderStatus(UUID.fromString(kitOrderId), 8)).thenReturn(1);
        Mockito.when(salesService.insertKitOrderLog(UUID.fromString(kitOrderId))).thenReturn(1);


        mockMvc.perform(post("/wareHouse/sales")
                        .param("kitOrderIdForSale", kitOrderId)
                        .param("sourceNamesForSale", sourceNamesJson)
                        .param("itemQuantitiesForSale", itemQuantitiesJson)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("distribution/wareHouseList"));

        // 서비스 메서드가 올바르게 호출되었는지 검증
        Mockito.verify(logisticsWareHouseService).updateStackBySourceName(Mockito.any());
        Mockito.verify(salesService).updateKitOrderStatus(UUID.fromString(kitOrderId), 8);
        Mockito.verify(salesService).insertKitOrderLog(UUID.fromString(kitOrderId));
    }


    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("GET /wareHouse/selectBySourceName - 성공 케이스")
    public void testSelectBySourceName() throws Exception {
        List<LogisticsWareHouseDto> warehouseList = Arrays.asList(new LogisticsWareHouseDto(), new LogisticsWareHouseDto());
        String keyword = "test";

        Mockito.when(logisticsWareHouseService.selectBySourceNameLogisticsWarehouse(keyword)).thenReturn(warehouseList);

        mockMvc.perform(get("/wareHouse/selectBySourceName")
                        .param("keyword", keyword))
                .andExpect(status().isOk())
                .andExpect(view().name("distribution/wareHouseList"))
                .andExpect(model().attributeExists("warehouseList"));
    }

    @Test
    @WithMockUser(roles = "ADMIN") // 임의의 인증된 사용자
    public void testGetWarehouseLog() throws Exception {
        String sourceId = "testSourceId";
        List<CombineLogDto> logs = new ArrayList<>();

        Mockito.when(logisticsWareHouseService.selectKitOrderLogDetailsBySourceId(sourceId)).thenReturn(new ArrayList<>());
        Mockito.when(logisticsWareHouseService.selectProductOrderLogDetailsBySourceId(sourceId)).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/wareHouse/selectLog")
                        .param("sourceId", sourceId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @DisplayName("POST /wareHouse/singleOrder - 성공 케이스")
    public void testSingleOrder() throws Exception {
        String companyName = "Company1";
        String sourceName = "Source1";
        String price = "100.00";
        int quantity = 10;

        mockMvc.perform(post("/wareHouse/singleOrder")
                        .param("companyName", companyName)
                        .param("sourceName", sourceName)
                        .param("price", price)
                        .param("quantity", String.valueOf(quantity))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wareHouse/selectList"));

        // 서비스 메서드가 올바르게 호출되었는지 검증
        Mockito.verify(logisticsWareHouseService).insertProductOrder(Mockito.any());
    }




}
