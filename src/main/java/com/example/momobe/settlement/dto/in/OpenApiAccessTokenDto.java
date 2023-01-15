package com.example.momobe.settlement.dto.in;

import com.example.momobe.settlement.domain.OpenApiAccessToken;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiAccessTokenDto {
    String access_token;
    String token_type;
    String expires_in;
    String scope;
    String client_use_code;

    public OpenApiAccessToken toEntity(){
        LocalDateTime expireDate = LocalDateTime
                .now(ZoneId.of("Asia/Seoul"))
                .plusSeconds(Integer.parseInt(expires_in));
        return OpenApiAccessToken.builder()
                .assessToken(access_token)
                .tokenType(token_type)
                .expireDate(expireDate.toString())
                .scope(scope)
                .clientUseCode(client_use_code)
                .build();

    }

}
