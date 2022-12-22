package com.example.momobe.security.oauth;

import com.example.momobe.user.domain.RandomPasswordGenerator;
import com.example.momobe.user.infrastructure.RandomPasswordGeneratorImpl;
import com.example.momobe.user.domain.Avatar;
import com.example.momobe.user.domain.User;
import com.example.momobe.user.domain.UserRepository;
import com.example.momobe.user.domain.UserState;
import com.example.momobe.user.domain.enums.UserStateType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

import static com.example.momobe.user.domain.enums.RoleName.*;

@Component
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RandomPasswordGenerator randomPasswordGeneratorImpl;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService defaultOAuth2Service = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = defaultOAuth2Service.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration()
                .getRegistrationId();

        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuth2UserDto oauth2User = OAuth2UserDto.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        saveIfNotSavedUser(oauth2User);

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(ROLE_USER.toString())),
                oAuth2User.getAttributes(), userNameAttributeName);
    }

    private void saveIfNotSavedUser(OAuth2UserDto oauth2User) {
        if (userRepository.findUserByEmail(oauth2User.getEmail()).isEmpty()) {
            saveOAuth2User(oauth2User);
        }
    }

    private void saveOAuth2User(OAuth2UserDto oauth2User) {
        String email = oauth2User.getEmail();
        String name = oauth2User.getName();
        String password = passwordEncoder.encode(randomPasswordGeneratorImpl.generateTemporaryPassword());
        userRepository.save(new User(email, name, password,
                oauth2User.getPicture() != null ? new Avatar(oauth2User.getPicture()) : null,new UserState(UserStateType.ACTIVE, LocalDateTime.now())));
    }
}
