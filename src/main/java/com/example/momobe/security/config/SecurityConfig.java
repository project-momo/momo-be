package com.example.momobe.security.config;

import com.example.momobe.security.constants.SecurityConstants;
import com.example.momobe.security.filter.CustomAuthenticationEntryPoint;
import com.example.momobe.security.oauth.CustomOAuth2Service;
import com.example.momobe.security.oauth.OAuth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static com.example.momobe.security.constants.SecurityConstants.*;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationManagerConfig authenticationManagerConfig;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final CustomOAuth2Service customOAuth2Service;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .headers().frameOptions().disable()
                .and()
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .httpBasic().disable()
                .apply(authenticationManagerConfig)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .mvcMatchers(HttpMethod.GET, "/meetings/**").permitAll()
                .mvcMatchers("/meetings/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                .mvcMatchers(HttpMethod.GET, "/addresses/**").permitAll()
                .mvcMatchers(HttpMethod.GET, "/articles/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                .mvcMatchers("/mypage/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                .mvcMatchers("/payments/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                .mvcMatchers("/ranks").permitAll()
                .mvcMatchers("/auth/**").permitAll()
                .mvcMatchers("/test/user/**").hasAnyRole("MANAGER","ADMIN","USER")
                .mvcMatchers("/test/manager/**").hasAnyRole("MANAGER","ADMIN")
                .mvcMatchers("/test/admin/**").hasAnyRole("ADMIN")
                .anyRequest().denyAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .and()
                .oauth2Login()
                .successHandler(oAuth2SuccessHandler)
                .userInfoEndpoint()
                .userService(customOAuth2Service)
                .and()
                .and()
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of("http://localhost:3000", "https://momo-deploy.site"));
        config.addAllowedHeader("*");
        config.setAllowedMethods(List.of("GET", "POST", "DELETE", "PATCH", "PUT", "OPTION"));
        config.setExposedHeaders(List.of(REFRESH_HEADER, ACCESS_TOKEN));
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}

