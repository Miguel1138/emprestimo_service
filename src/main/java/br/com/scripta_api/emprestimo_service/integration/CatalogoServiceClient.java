package br.com.scripta_api.emprestimo_service.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class CatalogoServiceClient {
    private final WebClient webClient;

    public void decrementarEstoque(Long livroId, String token) {
        webClient.put().uri("/" + livroId + "/estoque/decrementar")
                .header("Authorization ", token)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public void incrementarEstoque(Long livroId, String token) {
        webClient.put().uri("/" + livroId + "/estoque/incrementar")
                .header("Authorization ", token)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

}
