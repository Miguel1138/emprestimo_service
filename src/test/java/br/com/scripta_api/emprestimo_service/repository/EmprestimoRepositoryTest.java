package br.com.scripta_api.emprestimo_service.repository;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.Emprestimo;
import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.EmprestimoBuilder;
import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.StatusEmprestimo;
import br.com.scripta_api.emprestimo_service.dto.DevolucaoRequest;
import br.com.scripta_api.emprestimo_service.dto.SolicitacaoEmprestimoRequest;
import br.com.scripta_api.emprestimo_service.exception.RegraNegocioException;
import br.com.scripta_api.emprestimo_service.infra.data.EmprestimoEntity;
import br.com.scripta_api.emprestimo_service.infra.data.PenalidadeEntity;
import br.com.scripta_api.emprestimo_service.infra.gateways.EmprestimoEntityRepository;
import br.com.scripta_api.emprestimo_service.infra.gateways.PenalidadeEntityRepository;
import br.com.scripta_api.emprestimo_service.integration.CatalogoServiceOrquestrador;
import br.com.scripta_api.emprestimo_service.repository.mapper.EmprestimoMapper;
import br.com.scripta_api.emprestimo_service.repository.mapper.PenalidadeMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmprestimoRepositoryTest {

    @Mock
    private EmprestimoEntityRepository emprestimoEntityRepository;
    @Mock
    private PenalidadeEntityRepository penalidadeEntityRepository;
    @Mock
    private EmprestimoMapper emprestimoMapper;
    @Mock
    private PenalidadeMapper penalidadeMapper;
    @Mock
    private CatalogoServiceOrquestrador catalogoOrquestrador;

    @InjectMocks
    private EmprestimoRepository emprestimoRepository;

    private final Long ALUNO_ID = 1L;
    private final String TOKEN = "token";

    @Test
    @DisplayName("Deve solicitar emprestimo com sucesso (Happy Path)")
    void solicitarEmprestimo_Sucesso() {
        // Arrange
        SolicitacaoEmprestimoRequest request = new SolicitacaoEmprestimoRequest();
        request.setLivroId(100L);

        // Mocks de validação (Passando em todas as regras)
        when(penalidadeEntityRepository.findByAlunoIdAndDataFimPenalidadeAfter(eq(ALUNO_ID), any())).thenReturn(Optional.empty());
        when(emprestimoEntityRepository.findEmprestimoEntityByAlunoIdAndStatusIn(eq(ALUNO_ID), anyList())).thenReturn(List.of()); // Nenhum livro locado

        // Mocks de persistência
        EmprestimoEntity entitySalva = new EmprestimoEntity();
        entitySalva.setId(1L);
        Emprestimo domainSalvo = EmprestimoBuilder.builder()
                .id(1L).alunoId(ALUNO_ID).livroId(100L).status(StatusEmprestimo.PENDENTE)
                .build();

        when(emprestimoMapper.toEntity(any())).thenReturn(new EmprestimoEntity());
        when(emprestimoEntityRepository.save(any())).thenReturn(entitySalva);
        when(emprestimoMapper.toDomain(any())).thenReturn(domainSalvo);

        // Act
        Emprestimo resultado = emprestimoRepository.solicitarEmprestimo(request, TOKEN, ALUNO_ID);

        // Assert
        assertNotNull(resultado);
        assertEquals(StatusEmprestimo.PENDENTE, resultado.getStatus());
        verify(catalogoOrquestrador, times(1)).decrementarEstoque(any(), eq(TOKEN));
    }

    @Test
    @DisplayName("Deve falhar se aluno tiver penalidade ativa (RN 4)")
    void solicitarEmprestimo_FalhaPenalidade() {
        SolicitacaoEmprestimoRequest request = new SolicitacaoEmprestimoRequest();
        request.setLivroId(100L);

        when(penalidadeEntityRepository.findByAlunoIdAndDataFimPenalidadeAfter(eq(ALUNO_ID), any()))
                .thenReturn(Optional.of(new PenalidadeEntity()));

        assertThrows(RegraNegocioException.class, () ->
                emprestimoRepository.solicitarEmprestimo(request, TOKEN, ALUNO_ID));

        verify(catalogoOrquestrador, never()).decrementarEstoque(any(), any());
    }

    @Test
    @DisplayName("Deve falhar se aluno tiver livro atrasado (RN 1)")
    void solicitarEmprestimo_FalhaAtraso() {
        SolicitacaoEmprestimoRequest request = new SolicitacaoEmprestimoRequest();
        request.setLivroId(100L);

        EmprestimoEntity atrasado = new EmprestimoEntity();
        atrasado.setStatus(StatusEmprestimo.ATRASADO);

        when(penalidadeEntityRepository.findByAlunoIdAndDataFimPenalidadeAfter(eq(ALUNO_ID), any())).thenReturn(Optional.empty());
        when(emprestimoEntityRepository.findEmprestimoEntityByAlunoIdAndStatusIn(eq(ALUNO_ID), anyList()))
                .thenReturn(List.of(atrasado));

        assertThrows(RegraNegocioException.class, () ->
                emprestimoRepository.solicitarEmprestimo(request, TOKEN, ALUNO_ID));
    }

    @Test
    @DisplayName("Deve registrar devolução com atraso e gerar penalidade (RN 4)")
    void registrarDevolucao_ComAtraso() {
        // Arrange
        DevolucaoRequest request = new DevolucaoRequest();
        request.setEmprestimoId(10L);

        EmprestimoEntity entity = new EmprestimoEntity();
        entity.setId(10L);
        entity.setAlunoId(ALUNO_ID);
        entity.setLivroId(500L);
        entity.setStatus(StatusEmprestimo.CONFIRMADO);
        entity.setDataPrevistaDevolucao(LocalDate.now().minusDays(5)); // Venceu há 5 dias

        when(emprestimoEntityRepository.findById(10L)).thenReturn(Optional.of(entity));
        when(emprestimoMapper.toDomain(any())).thenReturn(EmprestimoBuilder.builder().id(10L).status(StatusEmprestimo.DEVOLVIDO).alunoId(ALUNO_ID).livroId(500L).build());

        // Act
        emprestimoRepository.registrarDevolucao(request, TOKEN);

        // Assert
        // Verifica se salvou a penalidade
        verify(penalidadeEntityRepository, times(1)).save(any());
        // Verifica se chamou o catálogo para devolver
        verify(catalogoOrquestrador, times(1)).incrementarEstoque(eq(500L), eq(TOKEN));
        // Verifica se salvou o empréstimo como devolvido
        assertEquals(StatusEmprestimo.DEVOLVIDO, entity.getStatus());
    }
}