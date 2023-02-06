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
    @DisplayName("실패 404 테스트")
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
                                parameterWithName("paymentKey").description("카드 결제 요청 시 받은 paymentKey"),
                                parameterWithName("orderId").description("서버에 존재하지 않는 orderId"),
                                parameterWithName("amount").description("결제 금액")
                        )));
    }

    @Test
    @DisplayName("실패 500 테스트")
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
                                parameterWithName("paymentKey").description("카드 결제 요청 시 받은 paymentKey"),
                                parameterWithName("orderId").description("서버에서 발급한 orderId"),
                                parameterWithName("amount").description("결제 금액")
                        )));
    }

    @Test
    @DisplayName("성공 200 테스트")
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
                        .cardType("신용")
                        .company("현대")
                        .installmentPlanMonth("0")
                        .number("444444****11111")
                        .isInterestFree("false")
                        .ownerType("개인")
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
                                parameterWithName("paymentKey").description("카드 결제 요청 시 받은 paymentKey"),
                                parameterWithName("orderId").description("서버에서 발급한 orderId"),
                                parameterWithName("amount").description("결제 금액")),
                        responseFields(
                                fieldWithPath("mid").type(STRING).description("가맹점 ID"),
                                fieldWithPath("version").type(STRING).description("payment 객체 응답 버전"),
                                fieldWithPath("paymentKey").type(STRING).description("토스로부터 발급받은 결제용 키"),
                                fieldWithPath("orderId").type(STRING).description("주문 번호"),
                                fieldWithPath("orderName").type(STRING).description("주문 명"),
                                fieldWithPath("currency").type(STRING).description("결제 통화"),
                                fieldWithPath("method").type(STRING).description("결제 수단"),
                                fieldWithPath("totalAmount").type(STRING).description("총 결제 금액"),
                                fieldWithPath("balanceAmount").type(STRING).description("잔액"),
                                fieldWithPath("suppliedAmount").type(STRING).description("부가세 미포함 금액"),
                                fieldWithPath("vat").type(STRING).description("부가세"),
                                fieldWithPath("status").type(STRING).description("결제 처리 상태"),
                                fieldWithPath("approvedAt").type(STRING).description("결제 승인 일시"),
                                fieldWithPath("useEscrow").type(BOOLEAN).description("에스크로 이용 여부"),
                                fieldWithPath("cultureExpense").type(BOOLEAN).description("분화비 여부"),
                                fieldWithPath("card").type(OBJECT).description("카드 결제 정보"),
                                fieldWithPath("cancels").type(OBJECT).description("결제 취소 이력"),
                                fieldWithPath("type").type(STRING).description("결제 타입 정보"),
                                fieldWithPath("requestedAt").type(STRING).description("결제 요청 일시"),
                                fieldWithPath("card.company").type(STRING).description("카드사"),
                                fieldWithPath("card.number").type(STRING).description("결제 승인 번호"),
                                fieldWithPath("card.installmentPlanMonth").type(STRING).description("할부 개월"),
                                fieldWithPath("card.isInterestFree").type(STRING).description("무이자 여부"),
                                fieldWithPath("card.approveNo").type(STRING).description("결제 승인번호"),
                                fieldWithPath("card.useCardPoint").type(STRING).description("카드포인트 사용 여부"),
                                fieldWithPath("card.cardType").type(STRING).description("카드 타입"),
                                fieldWithPath("card.ownerType").type(STRING).description("개인 법인 여부"),
                                fieldWithPath("card.acquireStatus").type(STRING).description("카드 활성화 여부"),
                                fieldWithPath("card.receiptUrl").type(STRING).description("결제 영수증 url")
                        )));
    }
}