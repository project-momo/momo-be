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
import java.util.*;

import static org.mockito.BDDMockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
class PaymentRequestServiceTest {
    @InjectMocks
    PaymentRequestService paymentRequestService;

    @Mock
    ApiService<PaymentResultDto> apiService;

    @Test
    @DisplayName("요청 흐름 테스트")
    void test1()  {
        //given
        PaymentResultDto response = PaymentResultDto.builder().build();
        given(apiService.post(any(),any(), any())).willReturn(ResponseEntity.of(Optional.of(response)));

        String paymentKey = "paymentKey";
        Map<String, Object> map = new HashMap<>();
        map.put("map", "map");
        map.put("num", "num");

        //when
        PaymentResultDto result = paymentRequestService.process(paymentKey, map);

        //then
        Assertions.assertThat(result).isEqualTo(response);
    }
}