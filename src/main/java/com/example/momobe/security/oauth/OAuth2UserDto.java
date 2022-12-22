package com.example.momobe.security.oauth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class OAuth2UserDto {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;
    private String provider;

    public static OAuth2UserDto of(String registrationId,
                                   String usernameAttributeName,
                                   Map<String, Object> attributes) {
        if (registrationId.equals("google")) {
            return ofGoogle(usernameAttributeName, attributes);
        } else if (registrationId.equals("kakao")) {
            return ofKakao(usernameAttributeName, attributes);
        }

        throw new IllegalArgumentException();
    }

    private static OAuth2UserDto ofGoogle(String usernameAttributeName,
                                          Map<String, Object> attributes) {
        return OAuth2UserDto.builder()
                .name((String)attributes.get("name"))
                .attributes(attributes)
                .email((String)attributes.get("email"))
                .picture((String)attributes.get("picture"))
                .provider("google")
                .nameAttributeKey(usernameAttributeName)
                .build();
    }

    private static OAuth2UserDto ofKakao(String usernameAttributeName,
                                         Map<String, Object> attributes) {
        return OAuth2UserDto.builder()
                .name((String)((Map)attributes.get("properties")).get("nickname"))
                .email((String)((Map)attributes.get("kakao_account")).get("email"))
                .picture((String)((Map)attributes.get("properties")).get("profile_image"))
                .provider("kakao")
                .nameAttributeKey(usernameAttributeName)
                .build();
    }
}
