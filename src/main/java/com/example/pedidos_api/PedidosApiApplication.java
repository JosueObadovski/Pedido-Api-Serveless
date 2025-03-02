package com.example.pedidos_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.function.context.config.ContextFunctionCatalogAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {ContextFunctionCatalogAutoConfiguration.class}) // Excluindo configuração problemática
public class PedidosApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(PedidosApiApplication.class, args);
	}
}
