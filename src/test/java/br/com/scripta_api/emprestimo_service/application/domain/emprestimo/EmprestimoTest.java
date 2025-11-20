package br.com.scripta_api.emprestimo_service.application.domain.emprestimo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class EmprestimoTest {

    @Test
    @DisplayName("Deve criar Emprestimo válido usando o Builder")
    void deveCriarEmprestimoValido() {
        Emprestimo emprestimo = EmprestimoBuilder.builder()
                .alunoId(1L)
                .livroId(100L)
                .status(StatusEmprestimo.PENDENTE)
                .dataEmprestimo(LocalDate.now())
                .build();

        assertNotNull(emprestimo);
        assertEquals(1L, emprestimo.getAlunoId());
        assertEquals(100L, emprestimo.getLivroId());
        assertEquals(StatusEmprestimo.PENDENTE, emprestimo.getStatus());
        assertFalse(emprestimo.isRenovado());
    }

    @Test
    @DisplayName("Deve falhar ao criar Emprestimo sem Aluno ID")
    void deveFalharSemAlunoId() {
        EmprestimoBuilder builder = EmprestimoBuilder.builder()
                .livroId(100L)
                .status(StatusEmprestimo.PENDENTE);

        RuntimeException exception = assertThrows(RuntimeException.class, builder::build);
        assertEquals("AlunoId vazio!", exception.getMessage());
    }

    @Test
    @DisplayName("Deve falhar ao criar Emprestimo sem Livro ID")
    void deveFalharSemLivroId() {
        EmprestimoBuilder builder = EmprestimoBuilder.builder()
                .alunoId(1L)
                .status(StatusEmprestimo.PENDENTE);

        RuntimeException exception = assertThrows(RuntimeException.class, builder::build);
        assertEquals("LivroId nulo", exception.getMessage());
    }

    @Test
    @DisplayName("Deve falhar ao criar Emprestimo sem Status")
    void deveFalharSemStatus() {
        EmprestimoBuilder builder = EmprestimoBuilder.builder()
                .alunoId(1L)
                .livroId(100L);

        RuntimeException exception = assertThrows(RuntimeException.class, builder::build);
        assertEquals("Status do emprestiom está nulo", exception.getMessage());
    }
}