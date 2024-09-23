package org.example.jhta_2402_2_final.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.jhta_2402_2_final.api.product.ProductCompanyRestController;
import org.example.jhta_2402_2_final.config.SecurityConfig;
import org.example.jhta_2402_2_final.model.dto.CustomUserDetails;
import org.example.jhta_2402_2_final.model.dto.member.MemberDto;
import org.example.jhta_2402_2_final.model.enums.Role;
import org.example.jhta_2402_2_final.service.product.ProductCompanyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductCompanyRestController.class)
public class ProductCompanyTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductCompanyService productCompanyService;


    private CustomUserDetails userDetails;
    ObjectMapper objectMapper;

    @BeforeEach
    public void init() {
        objectMapper = new ObjectMapper();
        MemberDto memberDto = MemberDto.builder()
                .userId("test-p1").userName("test-p1").roleId(Role.ROLE_PRODUCT_MANAGER.name())
                .userEmail("abc@abc.abc").userPassword("1234").userTel("123-1234-9111").build();

        userDetails = new CustomUserDetails(memberDto);
    }

    @Test
    @DisplayName("companyId 가져오기")
    public void getTest01() throws Exception {
        // given
        final String url = "/api/product/company/info";

        // 서비스 메소드 목킹 (필요한 경우)
        when(productCompanyService.getCompanyIdByUserId("test-p1")).thenReturn("test-company-id");

        // when
        final ResultActions resultActions = mockMvc.perform(
                MockMvcRequestBuilders.get(url)
                        .with(user(userDetails)) // 인증된 사용자 설정
        );

        // then
        resultActions.andExpect(status().isOk());
    }

}
