package com.example.momobe;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnabledIfEnvironmentVariable(named = "Local", matches = "local")
class MomoBeApplicationTests {
	@Test
	void contextLoads() {
	}
}
