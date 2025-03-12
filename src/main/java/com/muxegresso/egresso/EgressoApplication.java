package com.muxegresso.egresso;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info=@Info(title="API do Portal do Egresso",version = "1",description = "API desenvolvida para suporte ao sistema Portal do Egresso"))
@SpringBootApplication
public class EgressoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EgressoApplication.class, args);
	}

}
