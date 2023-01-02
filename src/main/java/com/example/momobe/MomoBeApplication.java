package com.example.momobe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class MomoBeApplication {

	public static void main(String[] args) {
		System.setProperty("aws.accessKeyId", "AKIAU66CN3NBOVJUA6G4");
		System.setProperty("aws.secretKey", "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");
		System.setProperty("aws.region", "ap-northeast-2");
		SpringApplication.run(MomoBeApplication.class, args);
	}

}
