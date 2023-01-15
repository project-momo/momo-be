// package com.example.momobe.settlement.ui;

// import com.example.momobe.MomoBeApplication;
// import com.example.momobe.common.config.SecurityTestConfig;
// import com.example.momobe.common.resolver.JwtArgumentResolver;
// import com.example.momobe.settlement.application.SettlementWithdrawalService;

// import com.example.momobe.settlement.dto.in.PointWithdrawalDto;
// import com.example.momobe.user.application.UserFindService;
// import com.example.momobe.user.domain.NotEnoughPointException;
// import com.example.momobe.user.domain.User;
// import com.example.momobe.user.domain.UserPoint;
// import com.example.momobe.user.domain.UserRepository;
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

// import java.util.Optional;

// import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentRequest;
// import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentResponse;
// import static com.example.momobe.common.enums.TestConstants.*;
// import static org.aspectj.apache.bcel.generic.Type.LONG;
// import static org.mockito.ArgumentMatchers.anyLong;
// import static org.mockito.BDDMockito.given;
// import static org.springframework.http.MediaType.APPLICATION_JSON;
// import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
// import static org.springframework.restdocs.payload.PayloadDocumentation.*;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// @WebMvcTest(SettlementWithdrawalController.class)
// @MockBean(JpaMetamodelMappingContext.class)
// @Import(SecurityTestConfig.class)
// @AutoConfigureRestDocs
// @WithMockUser
// @ContextConfiguration(classes = MomoBeApplication.class)
// public class SettlementControllerTest {
//     @Autowired
//     MockMvc mockMvc;
//     @Autowired
//     ObjectMapper objectMapper;

//     @MockBean
//     private JwtArgumentResolver resolver;
//     @MockBean
//     private SettlementWithdrawalService withdrawalService;
//     @MockBean
//     private UserFindService userFindService;
//     @MockBean
//     private UserRepository userRepository;

//     User user1;
//     User user2;
//     UserPoint userPoint;

//     @BeforeTransaction
//     void userInit(){
//         user1 = User.builder()
//                 .id(1L)
//                 .userPoint(new UserPoint(0L))
//                 .build();
//         user2 = User.builder()
//                 .id(2L)
//                 .userPoint(new UserPoint(7000L))
//                 .build();
//     }
//     @BeforeEach
//     void init() {

//     }

//     @Test
//     @DisplayName("현재 포인트보다 인출할 포인트 금액이 클 경우 409 반환")
//     void test01() throws Exception {
//         given(userFindService.verifyUser(anyLong())).willReturn(user1);
//         given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user1));
//         given(withdrawalService.deductPoint(anyLong(), anyLong())).willThrow(NotEnoughPointException.class);
//         PointWithdrawalDto request = PointWithdrawalDto.builder().amount(5000L).build();

//         String content = objectMapper.writeValueAsString(request);

//         ResultActions perform = mockMvc
//                 .perform(
//                         patch("/settlement/point/withdrawal")
//                                 .contentType(APPLICATION_JSON)
//                                 .header(JWT_HEADER, ACCESS_TOKEN)
//                                 .content(content)
//                 );

//         perform.andExpect(status().isOk())
//                 .andDo(document("settlement/withdrawal/409",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         REQUEST_HEADER_JWT,
//                         requestFields(
//                                 fieldWithPath("amount").type(LONG).description("인출 포인트 금액")
//                         )
//                 ));

//     }

//     @Test
//     @DisplayName("정상 동작")
//     void test02() throws Exception {
//         given(withdrawalService.deductPoint(anyLong(), anyLong())).willReturn(true);
//         PointWithdrawalDto request = PointWithdrawalDto.builder().amount(5000L).build();

//         String content = objectMapper.writeValueAsString(request);

//         ResultActions perform = mockMvc
//                 .perform(
//                         patch("/settlement/point/withdrawal")
//                                 .contentType(APPLICATION_JSON)
//                                 .header(JWT_HEADER, ACCESS_TOKEN)
//                                 .content(content)
//                 );

//         perform.andExpect(status().isOk())
//                 .andDo(document("settlement/withdrawal/200",
//                         getDocumentRequest(),
//                         getDocumentResponse(),
//                         REQUEST_HEADER_JWT,
//                         requestFields(
//                                 fieldWithPath("amount").type(LONG).description("인출 포인트 금액")
//                         )
//                 ));

//     }

// }
