package br.com.scripta_api.emprestimo_service.integration;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.Emprestimo;
import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.StatusEmprestimo;
import br.com.scripta_api.emprestimo_service.exception.RegraNegocioException;
import br.com.scripta_api.emprestimo_service.infra.data.EmprestimoEntity;
import br.com.scripta_api.emprestimo_service.infra.gateways.EmprestimoEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CatalogoServiceOrquestrador {
    private final CatalogoServiceClient client;
    private final EmprestimoEntityRepository repository;

    /**
     * Efetua chamada para a api de catalogo e solicita decremento do estoque
     * Efetua pelo menos 5 tentativa de conexão com a API
     * @param emprestimo
     * @param token
     * @author miguel.silva
     */
    @Async
    @Retryable(value = {RegraNegocioException.class}, maxAttempts = 5, backoff = @Backoff(delay = 3000))
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void decrementarEstoque(Emprestimo emprestimo, String token) {
        try {
            client.decrementarEstoque(emprestimo.getLivroId(), token);
            EmprestimoEntity entity = repository.findById(emprestimo.getId()).orElseThrow();
            entity.setStatus(StatusEmprestimo.CONFIRMADO);
        } catch (RegraNegocioException e) {
            throw new RegraNegocioException(StatusEmprestimo.FALHOU);
        }
    }

    /**
     * Lança exceção de conexão com a API de catalogo e atualiza status de falhou para o emprestimo
     * @author miguel.silva
     */
    @Recover
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void recover(RegraNegocioException e, Emprestimo emprestimo, String token) {
        EmprestimoEntity entity = repository.findById(emprestimo.getId()).orElseThrow();
        entity.setStatus(StatusEmprestimo.FALHOU);
        repository.save(entity);
    }

    /**
     * Efetua a solicitação para Api do catalaogo para ato de devolução do livro
     * maximo de 5 tentativas.
     * @param livroId
     * @param token
     * @author miguel.silva
     */
    @Async
    @Retryable(maxAttempts = 5, backoff = @Backoff(delay = 3000))
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void incrementarEstoque(Long livroId, String token) {
        client.incrementarEstoque(livroId, token);
    }


    @Recover
    public void recoverIncrementar(Long livroId, String token) {

    }
    /*
     * TODO: Implementar public void incrementarEstoque(Long livroId, String token):
     *
     * TODO: Anotar com @Async e @Retryable.
     *
     * TODO: Chamar client.incrementarEstoque(...). (Em caso de falha no @Recover, apenas logar o erro, pois a devolução já foi registrada).
     */


}
