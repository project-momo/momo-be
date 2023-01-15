package com.example.momobe.payment.application;

import com.example.momobe.common.application.ApiService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class PaymentCancelServiceTest {
    @InjectMocks
    PaymentCancelService paymentCancelService;

    @Mock
    ApiService<Void> apiService;

    @Test
    void test1() {
        //given
        BDDMockito.given(apiService.post(anyString(), any(), eq(Void.class))).willReturn(ResponseEntity.of(Optional.empty()));

        String key = "paymentKey";
        Map<String, Object> map = new HashMap<>();
        map.put("map", "map");

        // when
        Void process = paymentCancelService.process(key, map);

        //then
        Assertions.assertThat(process).isNull();
    }
}