package com.example.market;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@OpenAPIDefinition(info = @Info(title = "Market API", version = "1.0", description = "Market automation API"))
@SpringBootApplication
public class MarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(MarketApplication.class, args);
	}

	@Bean
	public ModelMapper getModelMapper(){
		return new ModelMapper();
	}

}
