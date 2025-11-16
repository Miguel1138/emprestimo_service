package br.com.scripta_api.emprestimo_service.integration;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.Emprestimo;
import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.StatusEmprestimo;
import br.com.scripta_api.emprestimo_service.infra.data.EmprestimoEntity;
import br.com.scripta_api.emprestimo_service.infra.gateways.EmprestimoEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * TODO: Implementar public void decrementarEstoque(Emprestimo emprestimo, String token):
 **
 * TODO: Dentro de um try-catch, chamar client.decrementarEstoque(...).
 *
 * TODO: No try (sucesso): Buscar a EmprestimoEntity por emprestimo.getId(), atualizar status para CONFIRMADO, calcular e setar dataPrevistaDevolucao (Regra 2), e salvar.
 *
 * TODO: Implementar um método @Recover para decrementarEstoque:
 *
 * TODO: Receber (Exception e, Emprestimo emprestimo, String token).
 *
 * TODO: Buscar a EmprestimoEntity, atualizar status para FALHOU, e salvar.
 *
 * TODO: Implementar public void incrementarEstoque(Long livroId, String token):
 *
 * TODO: Anotar com @Async e @Retryable.
 *
 * TODO: Chamar client.incrementarEstoque(...). (Em caso de falha no @Recover, apenas logar o erro, pois a devolução já foi registrada).
 */

@Service
@RequiredArgsConstructor
public class CatalogoServiceOrquestrador {
    private final CatalogoServiceClient client;
    private final EmprestimoEntityRepository repository;

    @Async
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 3000))
    public void decrementarEstoque(Emprestimo emprestimo, String token) {
        try {
            client.decrementarEstoque(emprestimo.getLivroId(), token);
            EmprestimoEntity entity = repository.findById(emprestimo.getId()).get();
            entity.setStatus(StatusEmprestimo.CONFIRMADO);
        }

    }

}
