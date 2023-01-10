package com.example.momobe.payment.application;

import com.example.momobe.common.application.ApiService;
import com.example.momobe.common.enums.TestConstants;
import com.example.momobe.payment.dto.PaymentResultDto;
import net.minidev.json.JSONObject;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
class PaymentRequestServiceTest {
    @InjectMocks
    PaymentRequestService paymentRequestService;

    @Mock
    ApiService<PaymentResultDto> apiService;

    HttpEntity<JSONObject> httpEntity;

    String secret ="secret";
    String url = "https://api.tosspayments.com/v1/payments/";
    String orderId = "order";
    String paymentKey = "payment";
    Long amount = 1000L;

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(paymentRequestService, "secretKey", secret);
        ReflectionTestUtils.setField(paymentRequestService, "tossUrl", url);

        HttpHeaders httpHeaders = new HttpHeaders();
        JSONObject param = new JSONObject();

        String authKey = Arrays.toString(Base64.getEncoder().encode(secret.getBytes(StandardCharsets.UTF_8)));

        httpHeaders.setBasicAuth(authKey);
        httpHeaders.setContentType(APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(APPLICATION_JSON));

        param.put("orderId", orderId);
        param.put("amount", amount);

        httpEntity = new HttpEntity<>(param, httpHeaders);
    }

    @Test
    @DisplayName("restTemplate이 1회 호출된다")
    void test1()  {
        //given
        given(apiService.post(any(),any(), any())).willReturn(ResponseEntity.of(Optional.of(PaymentResultDto.builder().build())));

        //when
        PaymentResultDto result = paymentRequestService.requestPayment(paymentKey, orderId, 1000L);

        //then
        verify(apiService, Mockito.times(1)).post(url + paymentKey, httpEntity, PaymentResultDto.class);
    }

    @Test
    @DisplayName("결제 요청 흐름 테스트")
    void test2()  {
        //given
        PaymentResultDto dto = PaymentResultDto.builder().build();
        given(apiService.post(url + paymentKey,httpEntity, PaymentResultDto.class)).willReturn(ResponseEntity.of(Optional.of(dto)));

        //when
        PaymentResultDto result = paymentRequestService.requestPayment(paymentKey, orderId, 1000L);

        //then
        Assertions.assertThat(dto).isEqualTo(result);
    }
}