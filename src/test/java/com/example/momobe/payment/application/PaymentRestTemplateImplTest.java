package com.example.momobe.payment.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;


import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

@SpringBootTest(classes = RestTemplate.class)
class PaymentRestTemplateImplTest {
    @Autowired
    RestTemplate restTemplate;

    private MockRestServiceServer mockRestServiceServer;


    @BeforeEach
    void init() throws URISyntaxException {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
        mockRestServiceServer.expect(ExpectedCount.once(),
                requestTo(new URI("http://localhost:8080/payments/success?paymentKey=paymentKey&orderId=orderId&amount=10000")))
                .andExpect(method(HttpMethod.POST))
                .andRespond(withStatus(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body("ok"));
    }

    @Test
    void test() {
        //given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("", headers);

        //when
        ResponseEntity<String> result = restTemplate.exchange("http://localhost:8080/payments/success?paymentKey=paymentKey&orderId=orderId&amount=10000", HttpMethod.POST, entity, String.class);

        //then
        Assertions.assertThat(result.getBody()).isEqualTo("ok");
    }
}