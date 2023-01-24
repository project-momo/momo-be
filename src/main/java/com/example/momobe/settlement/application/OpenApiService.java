package com.example.momobe.settlement.application;

import com.example.momobe.common.exception.enums.ErrorCode;
import com.example.momobe.settlement.domain.exception.NotFoundBankAccountException;
import com.example.momobe.settlement.dto.in.OpenApiAccessTokenDto;
import com.example.momobe.settlement.dto.in.OpenApiAccountRealNameDto;
import com.example.momobe.settlement.dto.in.PointWithdrawalDto;
import com.example.momobe.settlement.domain.AccessTokenRepository;
import com.example.momobe.user.domain.TokenNotFoundException;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

@Service
@RequiredArgsConstructor
@Transactional
public class OpenApiService {
    private final RefreshCRDIService refreshCRDIService;
    private final AccessTokenRepository accessTokenRepository;


    @Value("${banking.client_id}")
    String clientId;

    @Value("${banking.client_secret}")
    String clientSecret;

    public boolean verifyBankAccount(PointWithdrawalDto.BankAccountInfo request){
        requestOpenApiAToken();
        String CRDI = refreshCRDIService.createCRDI();
        String code = request.getBank().getBankCode();
        try{
            requestMatchAccountRealName(CRDI,code,request.getAccount(),request.getName(),"880101");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            throw new NotFoundBankAccountException(ErrorCode.DATA_NOT_FOUND);
        }
    }

    public void requestOpenApiAToken() {
        RestTemplate rest = new RestTemplate();

        URI uri = URI.create("https://testapi.openbanking.or.kr/oauth/2.0/token");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("client_id", clientId);
        param.add("client_secret", clientSecret);
        param.add("scope", "oob");
        param.add("grant_type", "client_credentials");

        String now = LocalDateTime.now(ZoneId.of("Asia/Seoul")).toString();

        if (accessTokenRepository.findFirstByExpireDateAfter(now).isEmpty()) {
            OpenApiAccessTokenDto newATokenRes;
            try {
                newATokenRes = rest.postForObject(
                        uri,
                        new HttpEntity<>(param, headers),
                        OpenApiAccessTokenDto.class
                );
            } catch (Exception e) {
                throw new TokenNotFoundException(ErrorCode.EXPIRED_EXCEPTION);
            }
            accessTokenRepository.save(newATokenRes.toEntity());
        }
    }

    public boolean requestMatchAccountRealName(String CRDI, String bankCode, String bankAccount, String realName, String birthday) {
        if (birthday.length() != 6 || bankAccount.length() > 16) return false;
        RestTemplate rest = new RestTemplate();
        URI uri = URI.create("https://testapi.openbanking.or.kr/v2.0/inquiry/real_name");

        HttpHeaders headers = new HttpHeaders();
        String accessToken = accessTokenRepository.findFirstByExpireDateAfter(LocalDateTime.now(ZoneId.of("Asia/Seoul")).toString())
                .orElseThrow(() -> new TokenNotFoundException(ErrorCode.EXPIRED_EXCEPTION))
                .getAssessToken();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JSONObject param = new JSONObject();
        String uniqueNum = String.valueOf(System.currentTimeMillis() % 1000000000);
        param.put("bank_tran_id", CRDI);
        param.put("bank_code_std", bankCode);
        param.put("account_num", bankAccount);
        param.put("account_holder_info_type","");
        param.put("account_holder_info", birthday);
        param.put("tran_dtime", LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

        OpenApiAccountRealNameDto realNameDto;
        try {
            realNameDto = rest.postForObject(
                    uri,
                    new HttpEntity<>(param.toJSONString(),headers),
                    OpenApiAccountRealNameDto.class
            );
        }catch (Exception e){
            throw new NotFoundBankAccountException(ErrorCode.DATA_NOT_FOUND);
        }
        if (realNameDto == null) {
            throw new NotFoundBankAccountException(ErrorCode.DATA_NOT_FOUND);
        }
        if(!realNameDto.getBank_code_std().equals(bankCode)){
            throw new NotFoundBankAccountException(ErrorCode.DATA_NOT_FOUND);
        }
        return true;
    }


}
