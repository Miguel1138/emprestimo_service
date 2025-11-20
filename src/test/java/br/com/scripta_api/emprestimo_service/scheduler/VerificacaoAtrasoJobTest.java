package br.com.scripta_api.emprestimo_service.scheduler;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.StatusEmprestimo;
import br.com.scripta_api.emprestimo_service.infra.data.EmprestimoEntity;
import br.com.scripta_api.emprestimo_service.infra.gateways.EmprestimoEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificacaoAtrasoJobTest {

    @Mock
    private EmprestimoEntityRepository repository;

    @InjectMocks
    private VerificacaoAtrasoJob job;

    @Test
    void verificarAtrasos_DeveAtualizarStatus() {
        // Arrange
        EmprestimoEntity emprestimoVencido = new EmprestimoEntity();
        emprestimoVencido.setId(1L);
        emprestimoVencido.setStatus(StatusEmprestimo.CONFIRMADO);
        emprestimoVencido.setDataPrevistaDevolucao(LocalDate.now().minusDays(1));

        when(repository.findByStatusAndDataPrevistaDevolucaoBefore(eq(StatusEmprestimo.CONFIRMADO), any(LocalDate.class)))
                .thenReturn(List.of(emprestimoVencido));

        // Act
        job.verificarAtrasos();

        // Assert
        assertEquals(StatusEmprestimo.ATRASADO, emprestimoVencido.getStatus());
        verify(repository).saveAll(List.of(emprestimoVencido));
    }
}