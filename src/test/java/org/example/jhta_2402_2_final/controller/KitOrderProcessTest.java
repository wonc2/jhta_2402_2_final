package org.example.jhta_2402_2_final.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.jhta_2402_2_final.controller.distribution.KitOrderProcessController;
import org.example.jhta_2402_2_final.service.distribution.KitOrderProcessService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Nested
@SpringBootTest
@AutoConfigureMockMvc
public class KitOrderProcessTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private KitOrderProcessService kitOrderProcessService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ObjectMapper objectMapper;

    // 각각의 권한으로 /distribution URL에 접근 테스트
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})  // 관리자 권한으로 Mock 사용자 설정
    public void testAdminAccessToDistribution() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/distribution/main"))
                .andExpect(status().isOk());  // 접근이 허용된 상태여야 함
    }

    @Test
    @WithMockUser(username = "logisticsUser", roles = {"LOGISTICS_MANAGER"})  // 물류 매니저 권한 설정
    public void testLogisticsManagerAccessToDistribution() throws Exception {
        // LOGISTICS_MANAGER 권한으로 /distribution URL에 접근 테스트
        mockMvc.perform(MockMvcRequestBuilders.get("/distribution/main"))
                .andExpect(status().isOk());  // 접근이 허용된 상태여야 함
    }

    @Test
    @WithMockUser(username = "salesUser", roles = {"SALES_MANAGER"})  // 판매 매니저 권한 설정
    public void testSalesManagerDeniedAccessToDistribution() throws Exception {
        // SALES_MANAGER 권한으로 /distribution URL에 접근 테스트 (권한 부족)
        mockMvc.perform(MockMvcRequestBuilders.get("/distribution/main"))
                .andExpect(status().isForbidden());  // 접근이 거부된 상태여야 함
    }




    // 출고 발주 요청 처리 테스트 (rest api)
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testKitOrderReleaseSuccess() throws Exception {
        // Given
        String kitOrderId = "validKitOrderId";

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/distribution/kitOrderRelease")
                        .contentType("application/json")
                        .content("{\"kitOrderId\":\"" + kitOrderId + "\"}"))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("재료 출고가 성공적으로 완료되었습니다."));
    }


    @Test
    public void testKitOrderReleaseFailure() throws Exception {
        // Given
        String kitOrderId = "invalidKitOrderId";

        // When
        mockMvc.perform(MockMvcRequestBuilders.post("/distribution/kitOrderRelease")
                        .contentType("application/json")
                        .content("{\"kitOrderId\":\"" + kitOrderId + "\"}"))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("error"))
                .andExpect(jsonPath("$.message").value("재고가 부족하여 출고에 실패했습니다. 재고를 다시 확인하세요."));
    }

    // 발주요청 테스트
    @Test
    public void testRequestSourceOrder() throws Exception {
        String requestBody = "{ \"kitOrderId\": \"kit123\", \"orderDetailList\": [{\"sourceName\":\"재료1\", \"supplierName\":\"공급업체1\", \"minPrice\":100, \"insufficientQuantity\":10}] }";

        mockMvc.perform(MockMvcRequestBuilders.post("/distribution/requestSourceOrder")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk());
    }


}
