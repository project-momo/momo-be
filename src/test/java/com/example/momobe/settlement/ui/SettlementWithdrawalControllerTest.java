// package com.example.momobe.settlement.ui;
//
// import com.example.momobe.MomoBeApplication;
// import com.example.momobe.common.config.SecurityTestConfig;
// import com.example.momobe.common.enums.TestConstants;
// import com.example.momobe.common.resolver.JwtArgumentResolver;
// import com.example.momobe.security.domain.JwtTokenUtil;
// import com.example.momobe.settlement.application.OpenApiService;
// import com.example.momobe.settlement.application.SettlementWithdrawalService;
//
// import com.example.momobe.settlement.domain.enums.BankCode;
// import com.example.momobe.settlement.domain.exception.CanNotWithdrawalException;
// import com.example.momobe.settlement.dto.in.PointWithdrawalDto;
// import com.example.momobe.settlement.dto.out.PointWithdrawalResponseDto;
// import com.example.momobe.user.application.UserFindService;
// import com.example.momobe.user.domain.*;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
// import org.springframework.boot.test.mock.mockito.MockBean;
// import org.springframework.context.annotation.Import;
// import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
// import org.springframework.security.test.context.support.WithMockUser;
// import org.springframework.test.context.ContextConfiguration;
// import org.springframework.test.context.transaction.BeforeTransaction;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.web.servlet.ResultActions;
//
// import java.util.Optional;
//
// import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentRequest;
// import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentResponse;
// import static com.example.momobe.common.enums.TestConstants.*;
// import static com.example.momobe.common.exception.enums.ErrorCode.REQUEST_CONFLICT;
// import static org.aspectj.apache.bcel.generic.Type.*;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.BDDMockito.given;
// import static org.springframework.http.MediaType.APPLICATION_JSON;
// import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
// import static org.springframework.restdocs.payload.PayloadDocumentation.*;
// import static org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType.BEARER;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
// @WebMvcTest(controllers = {SettlementWithdrawalController.class})
// @MockBean(JpaMetamodelMappingContext.class)
// @Import(SecurityTestConfig.class)
// @AutoConfigureRestDocs
// @WithMockUser
// @ContextConfiguration(classes = MomoBeApplication.class)
// public class SettlementWithdrawalControllerTest {
//     @Autowired
//     MockMvc mockMvc;
//     @Autowired
//     ObjectMapper objectMapper;
//
//     @MockBean
//     private JwtArgumentResolver resolver;
//     @MockBean
//     private SettlementWithdrawalService withdrawalService;
//     @MockBean
//     private OpenApiService openApiService;
//     @MockBean
//     private UserFindService userFindService;
//     @MockBean
//     private UserRepository userRepository;
//
//     PointWithdrawalResponseDto.WithdrawalDto withdrawalDto;
//     PointWithdrawalDto.BankAccountInfo bankAccountInfo;
//     PointWithdrawalDto pointWithdrawalDto;
//
//     @Test
//     @DisplayName("?????? ?????? ???????????? ?????? ?????? 404 ??????")
//     void test01() throws Exception {
//         bankAccountInfo = PointWithdrawalDto.BankAccountInfo.builder()
//                 .account("123")
//                 .bank(BankCode.??????)
//                 .name("???")
//                 .build();
//         withdrawalDto = PointWithdrawalResponseDto.WithdrawalDto.builder()
//                 .withdrawal(true)
//                 .currentPoint(1000L)
//                 .minusPoint(1500L)
//                 .build();
//         pointWithdrawalDto = PointWithdrawalDto.builder()
//                 .amount(1500L)
//                 .accountInfo(bankAccountInfo)
//                 .build();
//
//         given(openApiService.verifyBankAccount(any(PointWithdrawalDto.BankAccountInfo.class))).willReturn(false);
//         given(withdrawalService.deductPoint(anyLong(), anyLong())).willReturn(withdrawalDto);
//
//         String content = objectMapper.writeValueAsString(pointWithdrawalDto);
//
//         ResultActions perform = mockMvc
//                 .perform(
//                         patch("/mypage/point/withdrawal")
//                                 .contentType(APPLICATION_JSON)
//                                 .header(JWT_HEADER, ACCESS_TOKEN)
//                                 .content(content)
//                 );
//
//         perform.andExpect(status().isNotFound())
//                 .andDo(document("settlement/withdrawal/404",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         REQUEST_HEADER_JWT,
//                         requestFields(
//                                 fieldWithPath("amount").type(LONG).description("?????? ?????? ??????"),
//                                 fieldWithPath("accountInfo.name").type(STRING).description("?????? ????????????"),
//                                 fieldWithPath("accountInfo.bank").type(BankCode.class).description("?????????"),
//                                 fieldWithPath("accountInfo.account").type(STRING).description("????????????")
//
//                         )
//                 ));
//
//     }
//
//     @Test
//     @DisplayName("?????? ??????????????? ????????? ????????? ????????? ??? ?????? 409 ??????")
//     void test02() throws Exception {
//         bankAccountInfo = PointWithdrawalDto.BankAccountInfo.builder()
//                 .account("1234567890123456")
//                 .bank(BankCode.??????)
//                 .name("?????????")
//                 .build();
//         withdrawalDto = PointWithdrawalResponseDto.WithdrawalDto.builder()
//                 .withdrawal(true)
//                 .currentPoint(1000L)
//                 .minusPoint(1500L)
//                 .build();
//         pointWithdrawalDto = PointWithdrawalDto.builder()
//                 .amount(1500L)
//                 .accountInfo(bankAccountInfo)
//                 .build();
//
//         given(openApiService.verifyBankAccount(any(PointWithdrawalDto.BankAccountInfo.class))).willReturn(true);
//         given(withdrawalService.deductPoint(anyLong(), anyLong())).willThrow(new CanNotWithdrawalException(REQUEST_CONFLICT));
//
//         String content = objectMapper.writeValueAsString(pointWithdrawalDto);
//
//         ResultActions perform = mockMvc
//                 .perform(
//                         patch("/mypage/point/withdrawal")
//                                 .contentType(APPLICATION_JSON)
//                                 .header(JWT_HEADER, ACCESS_TOKEN)
//                                 .content(content)
//                 );
//
//         perform.andExpect(status().isConflict())
//                 .andDo(document("settlement/withdrawal/409",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         REQUEST_HEADER_JWT,
//                         requestFields(
//                                 fieldWithPath("amount").type(LONG).description("?????? ?????? ??????"),
//                                 fieldWithPath("accountInfo.name").type(STRING).description("?????? ????????????"),
//                                 fieldWithPath("accountInfo.bank").type(BankCode.class).description("?????????"),
//                                 fieldWithPath("accountInfo.account").type(STRING).description("????????????")
//                         )
//                 ));
//
//     }
//
//
//     @Test
//     @DisplayName("?????? ??????")
//     void test03() throws Exception {
//         bankAccountInfo = PointWithdrawalDto.BankAccountInfo.builder()
//                 .account("1234567890123456")
//                 .bank(BankCode.??????)
//                 .name("?????????")
//                 .build();
//         withdrawalDto = PointWithdrawalResponseDto.WithdrawalDto.builder()
//                 .withdrawal(true)
//                 .currentPoint(7000L)
//                 .minusPoint(1500L)
//                 .build();
//         pointWithdrawalDto = PointWithdrawalDto.builder()
//                 .amount(1500L)
//                 .accountInfo(bankAccountInfo)
//                 .build();
//
//         given(openApiService.verifyBankAccount(any(PointWithdrawalDto.BankAccountInfo.class))).willReturn(true);
//         given(withdrawalService.deductPoint(anyLong(),anyLong())).willReturn(withdrawalDto);
//
//         String content = objectMapper.writeValueAsString(pointWithdrawalDto);
//
//         ResultActions perform = mockMvc
//                 .perform(
//                         patch("/mypage/point/withdrawal")
//                                 .contentType(APPLICATION_JSON)
//                                 .header(JWT_HEADER, ACCESS_TOKEN)
//                                 .content(content)
//                 );
//
//         perform.andExpect(status().isOk())
//                 .andExpect(jsonPath("withdrawal.withdrawal").value(true))
//                 .andExpect(jsonPath("withdrawal.minusPoint").value(1500L))
//                 .andExpect(jsonPath("accountAuth").value(true))
//                 .andDo(print())
//                 .andDo(log())
//                 .andDo(document("settlement/withdrawal/200",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         REQUEST_HEADER_JWT,
//                         requestFields(
//                                 fieldWithPath("amount").type(LONG).description("?????? ?????? ??????"),
//                                 fieldWithPath("accountInfo.name").type(STRING).description("?????? ????????????"),
//                                 fieldWithPath("accountInfo.bank").type(BankCode.class).description("?????????"),
//                                 fieldWithPath("accountInfo.account").type(STRING).description("????????????")
//
//                         ),
//                         responseFields(
//                                 fieldWithPath("withdrawal.withdrawal").type(BOOLEAN).description("?????? ?????? ??????"),
//                                 fieldWithPath("withdrawal.minusPoint").type(BOOLEAN).description("?????? ??????"),
//                                 fieldWithPath("withdrawal.currentPoint").type(BOOLEAN).description("?????? ??? ?????? ??????"),
//                                 fieldWithPath("accountAuth").type(BOOLEAN).description("?????? ?????? ?????? ??????")
//                         )
//                 ));
//
//     }
//
// }
