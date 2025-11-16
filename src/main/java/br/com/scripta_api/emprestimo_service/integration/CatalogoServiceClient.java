package br.com.scripta_api.emprestimo_service.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * TODO: Anotar com @Component, @RequiredArgsConstructor.
 *
 * TODO: Injetar private final WebClient webClient;.
 *
 * TODO: Implementar public void decrementarEstoque(Long livroId, String token) (RF-S05).
 *
 * TODO: Usar webClient.put().uri("/" + livroId + "/estoque/decrementar").header("Authorization", token).retrieve().toBodilessEntity().block();.
 *
 * TODO: Implementar public void incrementarEstoque(Long livroId, String token) (RF-S05).
 */

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

    }
}
