package br.com.scripta_api.emprestimo_service.integration;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.Emprestimo;
import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.EmprestimoBuilder;
import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.StatusEmprestimo;
import br.com.scripta_api.emprestimo_service.exception.RegraNegocioException;
import br.com.scripta_api.emprestimo_service.infra.data.EmprestimoEntity;
import br.com.scripta_api.emprestimo_service.infra.gateways.EmprestimoEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatalogoServiceOrquestradorTest {

    @Mock
    private CatalogoServiceClient client;
    @Mock
    private EmprestimoEntityRepository repository;

    @InjectMocks
    private CatalogoServiceOrquestrador orquestrador;

    @Test
    void decrementarEstoque_Sucesso() {
        // Arrange
        Emprestimo emprestimo = EmprestimoBuilder.builder()
                .id(1L).livroId(100L).alunoId(1L).status(StatusEmprestimo.PENDENTE).build();

        EmprestimoEntity entity = new EmprestimoEntity();
        entity.setStatus(StatusEmprestimo.PENDENTE);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        // Act
        orquestrador.decrementarEstoque(emprestimo, "token");

        // Assert
        verify(client).decrementarEstoque(100L, "token");
        // Verifica se o status foi atualizado para CONFIRMADO após sucesso do client
        assert (entity.getStatus() == StatusEmprestimo.CONFIRMADO);
    }

    @Test
    void recover_DeveAtualizarParaFalhou() {
        // Arrange
        Emprestimo emprestimo = EmprestimoBuilder.builder()
                .id(1L).livroId(100L).alunoId(1L).status(StatusEmprestimo.PENDENTE).build();

        EmprestimoEntity entity = new EmprestimoEntity();
        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        // Act
        orquestrador.recover(new RegraNegocioException(StatusEmprestimo.FALHOU), emprestimo, "token");

        // Assert
        assert (entity.getStatus() == StatusEmprestimo.FALHOU);
        verify(repository).save(entity);
    }
}