package com.example.momobe.common.resolver;

import com.example.momobe.security.domain.JwtTokenUtil;
import com.example.momobe.user.application.UserFindService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.example.momobe.security.enums.SecurityConstants.*;
import static org.springframework.http.HttpMethod.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserFindService userFindService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Token.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String bearerToken = webRequest.getHeader(JWT_HEADER);

        if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith(JWT_PREFIX)) {
            return null;
        }

        Claims claims = jwtTokenUtil.parseAccessToken(bearerToken);
        String email = claims.getSubject();
        List<String> roles = (List) claims.get(ROLES);
        String nickname = (String) claims.get("nickname");
        Long id;
        if (claims.get(ID).getClass().equals(Integer.class)) {
            id = ((Integer)claims.get(ID)).longValue();
        } else { id = (Long)claims.get(ID); }

        String method = getRequestMethod(webRequest);
        verifyUser(id, method);

        return new UserInfo(id, email, roles, nickname);
    }

    private void verifyUser(Long id, String method) {
        if (POST.matches(method) || PUT.matches(method) || PATCH.matches(method)) {
            userFindService.verifyUser(id);
        }
    }

    private String getRequestMethod(NativeWebRequest webRequest) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) webRequest.getNativeRequest();
        return httpServletRequest.getMethod();
    }
}
