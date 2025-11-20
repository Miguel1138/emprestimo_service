package br.com.scripta_api.emprestimo_service.scheduler;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.StatusEmprestimo;
import br.com.scripta_api.emprestimo_service.infra.data.EmprestimoEntity;
import br.com.scripta_api.emprestimo_service.infra.gateways.EmprestimoEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VerificacaoAtrasoJob {
    private final EmprestimoEntityRepository repository;

    /**
     * Verifica diariamente por atrasos  a 1h da manhã
     * A niveis de atomicidade, considera apenas mudanças commitadas no banco
     *
     * @author miguel.silva
     */
    @Scheduled(cron = "0 0 1 * * ?")
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void verificarAtrasos() {
        List<EmprestimoEntity> emprestimosAtrasados =
                repository.findByStatusAndDataPrevistaDevolucaoBefore(StatusEmprestimo.CONFIRMADO, LocalDate.now());
        emprestimosAtrasados.forEach(e -> e.setStatus(StatusEmprestimo.ATRASADO));
        repository.saveAll(emprestimosAtrasados);
    }
}
