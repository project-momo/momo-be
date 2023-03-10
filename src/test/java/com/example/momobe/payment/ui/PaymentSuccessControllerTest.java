package com.example.momobe.payment.ui;

import com.example.momobe.common.config.SecurityTestConfig;
import com.example.momobe.common.exception.ui.ExceptionController;
import com.example.momobe.common.resolver.JwtArgumentResolver;
import com.example.momobe.payment.application.PaymentProgressService;
import com.example.momobe.payment.domain.PaymentException;
import com.example.momobe.payment.dto.PaymentResultDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
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

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentRequest;
import static com.example.momobe.common.config.ApiDocumentUtils.getDocumentResponse;
import static com.example.momobe.common.enums.TestConstants.*;
import static com.example.momobe.common.exception.enums.ErrorCode.*;
import static org.aspectj.apache.bcel.generic.ObjectType.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser
@AutoConfigureRestDocs
@Import(SecurityTestConfig.class)
@MockBean(JpaMetamodelMappingContext.class)
@WebMvcTest({PaymentSuccessController.class, ExceptionController.class})
class PaymentSuccessControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    JwtArgumentResolver jwtArgumentResolver;

    @MockBean
    PaymentProgressService paymentProgressService;

    @Autowired
    ObjectMapper objectMapper;

    private final String ORDER_ID = "ORDER_ID";
    private final String PAYMENT_KEY = "PAYMENT_KEY";
    private final Long AMOUNT = 10000L;

    @Test
    @DisplayName("?????? 404 ?????????")
    void getSuccess_fail1() throws Exception {
        //given
        given(paymentProgressService.progress(ORDER_ID, PAYMENT_KEY, AMOUNT))
                .willThrow(new PaymentException(DATA_NOT_FOUND));

        //when
        ResultActions perform = mockMvc.perform(get("/payments/success")
                .param("paymentKey", PAYMENT_KEY)
                .param("orderId", ORDER_ID)
                .param("amount", String.valueOf(AMOUNT))
        );

        //then
        perform.andExpect(status().isNotFound())
                .andDo(document("payments/success/404",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("paymentKey").description("?????? ?????? ?????? ??? ?????? paymentKey"),
                                parameterWithName("orderId").description("????????? ???????????? ?????? orderId"),
                                parameterWithName("amount").description("?????? ??????")
                        )));
    }

    @Test
    @DisplayName("?????? 500 ?????????")
    void getSuccess_fail2() throws Exception {
        //given
        given(paymentProgressService.progress(ORDER_ID, PAYMENT_KEY, AMOUNT))
                .willThrow(new PaymentException(UNABLE_TO_PROCESS));

        //when
        ResultActions perform = mockMvc.perform(get("/payments/success")
                .param("paymentKey", PAYMENT_KEY)
                .param("orderId", ORDER_ID)
                .param("amount", String.valueOf(AMOUNT))
        );

        //then
        perform.andExpect(status().isServiceUnavailable())
                .andDo(document("payments/success/500",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("paymentKey").description("?????? ?????? ?????? ??? ?????? paymentKey"),
                                parameterWithName("orderId").description("???????????? ????????? orderId"),
                                parameterWithName("amount").description("?????? ??????")
                        )));
    }

    @Test
    @DisplayName("?????? 200 ?????????")
    void getSuccess_success() throws Exception {
        //given
        String approvedAt = LocalDateTime.now().toString();
        String requestedAt = LocalDateTime.now().minus(1, ChronoUnit.MINUTES).toString();

        PaymentResultDto response = PaymentResultDto.builder()
                .mId(ID1.toString())
                .version("1.3")
                .paymentKey(PAYMENT_KEY)
                .orderId(ORDER_ID)
                .orderName(CONTENT1)
                .currency("KRW")
                .method("card")
                .totalAmount(AMOUNT.toString())
                .balanceAmount(AMOUNT.toString())
                .vat(String.valueOf((1 / AMOUNT)))
                .suppliedAmount(String.valueOf(9 / AMOUNT))
                .status("DONE")
                .approvedAt(approvedAt)
                .requestedAt(requestedAt)
                .useEscrow("false")
                .cultureExpense("false")
                .card(PaymentResultDto.PaymentCardDto.builder()
                        .acquireStatus("READY")
                        .approveNo("0000000")
                        .cardType("??????")
                        .company("??????")
                        .installmentPlanMonth("0")
                        .number("444444****11111")
                        .isInterestFree("false")
                        .ownerType("??????")
                        .receiptUrl("https://merchants.tosspayments.com/web/serve/merchant")
                        .useCardPoint("0")
                        .build()
                )
                .cancels(null)
                .build();

        given(paymentProgressService.progress(ORDER_ID, PAYMENT_KEY, AMOUNT))
                .willReturn(response);

        //when
        ResultActions perform = mockMvc.perform(get("/payments/success")
                .param("paymentKey", PAYMENT_KEY)
                .param("orderId", ORDER_ID)
                .param("amount", String.valueOf(AMOUNT))
        );

        //then
        perform.andExpect(status().isOk())
                .andDo(document("payments/success/200",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestParameters(
                                parameterWithName("paymentKey").description("?????? ?????? ?????? ??? ?????? paymentKey"),
                                parameterWithName("orderId").description("???????????? ????????? orderId"),
                                parameterWithName("amount").description("?????? ??????")),
                        responseFields(
                                fieldWithPath("mid").type(STRING).description("????????? ID"),
                                fieldWithPath("version").type(STRING).description("payment ?????? ?????? ??????"),
                                fieldWithPath("paymentKey").type(STRING).description("??????????????? ???????????? ????????? ???"),
                                fieldWithPath("orderId").type(STRING).description("?????? ??????"),
                                fieldWithPath("orderName").type(STRING).description("?????? ???"),
                                fieldWithPath("currency").type(STRING).description("?????? ??????"),
                                fieldWithPath("method").type(STRING).description("?????? ??????"),
                                fieldWithPath("totalAmount").type(STRING).description("??? ?????? ??????"),
                                fieldWithPath("balanceAmount").type(STRING).description("??????"),
                                fieldWithPath("suppliedAmount").type(STRING).description("????????? ????????? ??????"),
                                fieldWithPath("vat").type(STRING).description("?????????"),
                                fieldWithPath("status").type(STRING).description("?????? ?????? ??????"),
                                fieldWithPath("approvedAt").type(STRING).description("?????? ?????? ??????"),
                                fieldWithPath("useEscrow").type(BOOLEAN).description("???????????? ?????? ??????"),
                                fieldWithPath("cultureExpense").type(BOOLEAN).description("????????? ??????"),
                                fieldWithPath("card").type(OBJECT).description("?????? ?????? ??????"),
                                fieldWithPath("cancels").type(OBJECT).description("?????? ?????? ??????"),
                                fieldWithPath("type").type(STRING).description("?????? ?????? ??????"),
                                fieldWithPath("requestedAt").type(STRING).description("?????? ?????? ??????"),
                                fieldWithPath("card.company").type(STRING).description("?????????"),
                                fieldWithPath("card.number").type(STRING).description("?????? ?????? ??????"),
                                fieldWithPath("card.installmentPlanMonth").type(STRING).description("?????? ??????"),
                                fieldWithPath("card.isInterestFree").type(STRING).description("????????? ??????"),
                                fieldWithPath("card.approveNo").type(STRING).description("?????? ????????????"),
                                fieldWithPath("card.useCardPoint").type(STRING).description("??????????????? ?????? ??????"),
                                fieldWithPath("card.cardType").type(STRING).description("?????? ??????"),
                                fieldWithPath("card.ownerType").type(STRING).description("?????? ?????? ??????"),
                                fieldWithPath("card.acquireStatus").type(STRING).description("?????? ????????? ??????"),
                                fieldWithPath("card.receiptUrl").type(STRING).description("?????? ????????? url")
                        )));
    }
}