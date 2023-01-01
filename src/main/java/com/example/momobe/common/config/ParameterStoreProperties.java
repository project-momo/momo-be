package com.example.momobe.common.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@NoArgsConstructor
@Configuration
public class ParameterStoreProperties {
    @Value("${encrypt.key}")
    private String encryptKey;
}
