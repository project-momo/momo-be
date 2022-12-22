package com.example.momobe.meeting.presentation;

import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.meeting.domain.MeetingRepository;
import com.example.momobe.meeting.domain.PricePolicy;
import com.example.momobe.meeting.dto.MeetingRequestDto;
import com.example.momobe.meeting.mapper.DateTimeMapper;
import com.example.momobe.meeting.mapper.LocationMapper;
import com.example.momobe.meeting.mapper.MeetingMapper;
import com.example.momobe.meeting.mapper.PriceMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;

import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentRequest;
import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentResponse;
import static com.example.momobe.common.enums.TestConstants.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({MeetingRegistrationController.class,
        MeetingMapper.class, LocationMapper.class, DateTimeMapper.class, PriceMapper.class})
@MockBean(JpaMetamodelMappingContext.class)
@Import(SecurityTestConfig.class)
@AutoConfigureRestDocs
@WithMockUser
class MeetingRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private MeetingRepository meetingRepository;
    @MockBean
    private JwtArgumentResolver jwtArgumentResolver;

    @Test
    void registerMeeting() throws Exception {
        // given
        MeetingRequestDto.PriceDto priceDto = MeetingRequestDto.PriceDto.builder()
                .pricePolicy(PricePolicy.HOUR)
                .price(1000L)
                .build();
        MeetingRequestDto.LocationDto locationDto = MeetingRequestDto.LocationDto.builder()
                .address1(ADDRESS1)
                .address2(ADDRESS2)
                .dateTimes(List.of(NOW_TIME, NOW_TIME.plus(1L, ChronoUnit.HOURS)))
                .build();
        MeetingRequestDto request = MeetingRequestDto.builder()
                .categoryId(ID1)
                .title(TITLE1)
                .content(CONTENT1)
                .tagIds(Set.of(ID1, ID2))
                .priceInfo(priceDto)
                .locations(List.of(locationDto))
                .notice("전달 사항")
                .build();

        String content = objectMapper.writeValueAsString(request);

        // when
        ResultActions actions = mockMvc.perform(
                post("/meetings")
                        .content(content)
                        .contentType(APPLICATION_JSON)
        );

        // then
        actions.andExpect(status().isCreated())
                .andDo(document("meeting/registration",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("categoryId").type(NUMBER).description("카테고리 식별자"),
                                fieldWithPath("title").type(STRING).description("제목"),
                                fieldWithPath("content").type(STRING).description("내용"),
                                fieldWithPath("tagIds").type(ARRAY).description("태그 식별자"),
                                fieldWithPath("locations").type(ARRAY).description("장소"),
                                fieldWithPath("locations[].address1").type(STRING).description("주소1"),
                                fieldWithPath("locations[].address2").type(STRING).description("주소2"),
                                fieldWithPath("locations[].dateTimes").type(ARRAY).description("시간"),
                                fieldWithPath("priceInfo").type(OBJECT).description("가격 정보"),
                                fieldWithPath("priceInfo.pricePolicy").type(STRING).description("가격 정책"),
                                fieldWithPath("priceInfo.price").type(NUMBER).description("가격"),
                                fieldWithPath("notice").type(STRING).description("전달 사항")
                        )
                ));
    }

}