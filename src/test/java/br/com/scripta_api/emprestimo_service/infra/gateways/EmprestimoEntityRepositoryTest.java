package br.com.scripta_api.emprestimo_service.infra.gateways;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.StatusEmprestimo;
import br.com.scripta_api.emprestimo_service.infra.data.EmprestimoEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test") // Garante que use o application-test.properties com H2
class EmprestimoEntityRepositoryTest {

    @Autowired
    private EmprestimoEntityRepository repository;

    @Test
    @DisplayName("Deve encontrar emprestimos por aluno e lista de status")
    void findEmprestimoEntityByAlunoIdAndStatusIn() {
        // Arrange
        EmprestimoEntity e1 = EmprestimoEntity.builder()
                .alunoId(1L).livroId(100L).status(StatusEmprestimo.CONFIRMADO).renovado(false).build();
        EmprestimoEntity e2 = EmprestimoEntity.builder()
                .alunoId(1L).livroId(101L).status(StatusEmprestimo.ATRASADO).renovado(false).build();
        EmprestimoEntity e3 = EmprestimoEntity.builder()
                .alunoId(1L).livroId(102L).status(StatusEmprestimo.DEVOLVIDO).renovado(false).build();

        repository.saveAll(List.of(e1, e2, e3));

        // Act
        List<EmprestimoEntity> resultado = repository.findEmprestimoEntityByAlunoIdAndStatusIn(
                1L,
                List.of(StatusEmprestimo.CONFIRMADO, StatusEmprestimo.ATRASADO)
        );

        // Assert
        assertEquals(2, resultado.size());
        assertTrue(resultado.stream().anyMatch(e -> e.getStatus() == StatusEmprestimo.CONFIRMADO));
        assertTrue(resultado.stream().anyMatch(e -> e.getStatus() == StatusEmprestimo.ATRASADO));
        assertTrue(resultado.stream().noneMatch(e -> e.getStatus() == StatusEmprestimo.DEVOLVIDO));
    }

    @Test
    @DisplayName("Deve encontrar emprestimos vencidos (para o Job)")
    void findByStatusAndDataPrevistaDevolucaoBefore() {
        // Arrange
        LocalDate ontem = LocalDate.now().minusDays(1);
        LocalDate amanha = LocalDate.now().plusDays(1);

        EmprestimoEntity vencido = EmprestimoEntity.builder()
                .alunoId(1L).livroId(100L).status(StatusEmprestimo.CONFIRMADO)
                .dataPrevistaDevolucao(ontem).renovado(false).build();

        EmprestimoEntity noPrazo = EmprestimoEntity.builder()
                .alunoId(2L).livroId(101L).status(StatusEmprestimo.CONFIRMADO)
                .dataPrevistaDevolucao(amanha).renovado(false).build();

        repository.saveAll(List.of(vencido, noPrazo));

        // Act
        List<EmprestimoEntity> resultado = repository.findByStatusAndDataPrevistaDevolucaoBefore(
                StatusEmprestimo.CONFIRMADO,
                LocalDate.now()
        );

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(vencido.getId(), resultado.get(0).getId());
    }
}