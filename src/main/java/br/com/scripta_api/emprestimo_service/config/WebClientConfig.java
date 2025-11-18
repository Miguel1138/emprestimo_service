package br.com.scripta_api.emprestimo_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${client.catalogo.url}")
    private String catalogoUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(catalogoUrl)
                .build();
    }
}