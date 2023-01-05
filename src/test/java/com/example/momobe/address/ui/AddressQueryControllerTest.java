package com.example.momobe.address.ui;

import com.example.momobe.address.dao.AddressQueryRepository;
import com.example.momobe.address.dto.AddressResponseDto;
import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentRequest;
import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentResponse;
import static com.example.momobe.common.enums.TestConstants.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressQueryController.class)
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityTestConfig.class)
@AutoConfigureRestDocs
class AddressQueryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private JwtArgumentResolver jwtArgumentResolver;
    @MockBean
    private AddressQueryRepository addressQueryRepository;

    @Test
    public void getAddresses() throws Exception {
        // given
        List<AddressResponseDto> responseDtos = List.of(
                new AddressResponseDto("서울시", List.of(
                        new AddressResponseDto.AddressDto(ID1, "전체"),
                        new AddressResponseDto.AddressDto(ID2, "강남구"),
                        new AddressResponseDto.AddressDto(ID3, "강동구")
                )));
        given(addressQueryRepository.findAll())
                .willReturn(responseDtos);

        // when
        ResultActions actions = mockMvc.perform(
                get("/addresses")
        );

        // then
        actions.andExpect(status().isOk())
                .andDo(document("address/get",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[]").type(ARRAY).description("주소"),
                                fieldWithPath("[].si").type(STRING).description("주소 (시)"),
                                fieldWithPath("[].gu[].id").type(NUMBER).description("주소 식별자"),
                                fieldWithPath("[].gu[].name").type(STRING).description("주소 (구)")
                        )));
    }
}