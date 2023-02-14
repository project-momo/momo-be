package com.example.momobe.common.log.aop;

import com.example.momobe.common.log.entity.ControllerLog;
import com.example.momobe.common.log.service.ControllerLogService;
import com.example.momobe.security.domain.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import static com.example.momobe.security.constants.SecurityConstants.*;

@Aspect
@Component
@RequiredArgsConstructor
public class ControllerLoggingAspect {
    private final ControllerLogService logService;
    private final JwtTokenUtil jwtTokenUtil;
    private static final Logger log = LoggerFactory.getLogger(ControllerLoggingAspect.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) && !@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void onRequest() {}

    @Around("onRequest()")
    public Object advice(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            HttpServletRequest request = getHttpServletRequest();
            ControllerLog log = createLog(request);
            logService.saveLog(log);
            return joinPoint.proceed();
        } catch (Exception e) {
            log.error("", e);
            return joinPoint.proceed();
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        return request;
    }

    private String extractUserId(String tokenHeader) {
        String userSequenceId;
        Claims claims = jwtTokenUtil.parseAccessToken(tokenHeader);
        userSequenceId = String.valueOf(claims.get(ID));
        return userSequenceId;
    }

    private ControllerLog createLog(HttpServletRequest request) {
        String requestMethod = request.getMethod();
        String requestIP = request.getRemoteAddr();
        String requestURI = request.getRequestURI();
        String tokenHeader = request.getHeader(JWT_HEADER);
        String userSequenceId = extractId(tokenHeader);

        return ControllerLog.builder()
                .httpMethod(requestMethod)
                .operatorIP(requestIP)
                .operatorId(userSequenceId)
                .requestUri(requestURI)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private String extractId(String tokenHeader) {
        String userSequenceId = "NULL";

        if (hasSecurityHeader(tokenHeader)) {
            userSequenceId = extractUserId(tokenHeader);
        }
        return userSequenceId;
    }

    private boolean hasSecurityHeader(String tokenHeader) {
        return StringUtils.hasText(tokenHeader);
    }
}
