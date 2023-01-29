package com.example.momobe.meeting.ui;

import com.example.momobe.common.config.ApiDocumentUtils;
import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.common.exception.ui.ExceptionController;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.meeting.constants.MeetingConstants;
import com.example.momobe.meeting.domain.MeetingRankingStore;
import com.example.momobe.meeting.dto.out.MeetingRankDto;
import com.example.momobe.payment.ui.PaymentSuccessController;
import org.aspectj.apache.bcel.generic.ObjectType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.meeting.constants.MeetingConstants.*;
import static org.aspectj.apache.bcel.generic.ObjectType.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureRestDocs
@Import(SecurityTestConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({MeetingRankController.class, ExceptionController.class})
class MeetingRankControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtArgumentResolver jwtArgumentResolver;

    @MockBean
    MeetingRankingStore<MeetingRankDto> meetingRankingStore;

    @Test
    @DisplayName("랭킹 조회 200 테스트")
    void getRankTest() throws Exception {
        //given
        MeetingRankDto dto = MeetingRankDto.builder()
                .meetingId(ID1)
                .content(CONTENT1)
                .imageUrl(TISTORY_URL)
                .title(TITLE1)
                .build();

        BDDMockito.given(meetingRankingStore.getRanks(RANKING_CACHE_KEY, 0, 9, MeetingRankDto.class))
                .willReturn(List.of(dto, dto, dto));

        //when
        ResultActions result = mockMvc.perform(get("/ranks"));

        //then
        result.andExpect(status().isOk())
                .andDo(document("rankings/200",
                        ApiDocumentUtils.getDocumentRequest(),
                        ApiDocumentUtils.getDocumentResponse(),
                        responseFields(
                                fieldWithPath("[]").type(OBJECT_ARRAY).description("모임 배열"),
                                fieldWithPath("[].meetingId").type(LONG).description("모임 아이디"),
                                fieldWithPath("[].content").type(STRING).description("모임 설명"),
                                fieldWithPath("[].imageUrl").type(STRING).description("모임 주최자 프로필"),
                                fieldWithPath("[].title").type(STRING).description("모임 제목")
                        )
                        ));
    }
}