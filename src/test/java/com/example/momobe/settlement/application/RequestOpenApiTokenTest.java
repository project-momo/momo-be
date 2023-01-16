//package com.example.momobe.settlement.application;
//
//import com.example.momobe.common.exception.enums.ErrorCode;
//import com.example.momobe.settlement.dto.in.OpenApiAccessTokenDto;
//import com.example.momobe.user.domain.TokenNotFoundException;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URI;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Collections;
//
//@ExtendWith(MockitoExtension.class)
//public class RequestOpenApiTokenTest {
//    RestTemplate rest = new RestTemplate();
//
//    URI uri = URI.create("https://testapi.openbanking.or.kr/oauth/2.0/token");
//
//    HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//    MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
//        param.add("client_id", "dddddddd");
//        param.add("client_secret", "dddddddd");
//        param.add("scope", "oob");
//        param.add("grant_type", "client_credentials");
//
//    String now = LocalDateTime.now(ZoneId.of("Asia/Seoul")).toString();
//
//        if (accessTokenRepository.findFirstByExpireDateAfter(now).isEmpty()) {
//        OpenApiAccessTokenDto newATokenRes;
//        try {
//            newATokenRes = rest.postForObject(
//                    uri,
//                    new HttpEntity<>(param, headers),
//                    OpenApiAccessTokenDto.class
//            );
//        } catch (Exception e) {
//            throw new TokenNotFoundException(ErrorCode.EXPIRED_EXCEPTION);
//        }
//        accessTokenRepository.save(newATokenRes.toEntity());
//    }
//}
