package com.example.momobe.common.application;

import com.example.momobe.common.enums.TestConstants;
import net.minidev.json.JSONObject;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static com.example.momobe.common.enums.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ApiServiceTest {
    @Mock
    RestTemplate restTemplate;

    @InjectMocks
    ApiService<String> apiService;

    @Test
    @DisplayName("res")
    void post() {
        //given
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(httpHeaders);
        BDDMockito.given(restTemplate.postForEntity("uri", httpEntity, String.class)).willReturn(ResponseEntity.of(Optional.of(CONTENT1)));

        //when
        ResponseEntity<String> result = apiService.post("uri", httpEntity, String.class);

        //then
        Assertions.assertThat(result.getBody()).isEqualTo(CONTENT1);
    }
}