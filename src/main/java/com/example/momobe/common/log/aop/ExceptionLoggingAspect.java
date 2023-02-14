package com.example.momobe.common.log.aop;

import com.example.momobe.common.log.entity.ExceptionLog;
import com.example.momobe.common.log.service.ExceptionLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class ExceptionLoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(ExceptionLoggingAspect.class);
    private final ExceptionLogService exceptionLogService;

    @AfterThrowing(pointcut = "within(com.example.momobe.reservation.application..*)", throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) throws Throwable {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        printErrorLog(exception, className, methodName);
        saveExceptionLog(exception, className, methodName);

        throw exception;
    }

    private void saveExceptionLog(Throwable exception, String className, String methodName) {
        String exceptionType = exception.getClass().getName();
        String exceptionMessage = exception.getMessage();
        String stackTrace = Arrays.toString(exception.getStackTrace());
        ExceptionLog exceptionLog = ExceptionLog.builder()
                .className(className)
                .methodName(methodName)
                .exceptionType(exceptionType)
                .exceptionMessage(exceptionMessage)
                .stackTrace(stackTrace)
                .createdAt(LocalDateTime.now())
                .build();

        exceptionLogService.save(exceptionLog);
    }

    private void printErrorLog(Throwable exception, String className, String methodName) {
        logger.error("Exception in {}.{}() whith cause = {}", className, methodName,
                exception.getCause() != null ? exception.getCause() : "NULL");
    }

}
