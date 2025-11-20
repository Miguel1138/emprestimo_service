package br.com.scripta_api.emprestimo_service.application.domain.penalidade;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PenalidadeTest {

    @Test
    @DisplayName("Deve criar Penalidade válida")
    void deveCriarPenalidadeValida() {
        LocalDate fimPenalidade = LocalDate.now().plusDays(5);
        Penalidade penalidade = PenalidadeBuilder.builder()
                .alunoId(1L)
                .dataFimPenalidade(fimPenalidade)
                .build();

        assertNotNull(penalidade);
        assertEquals(1L, penalidade.getAlunoId());
        assertEquals(fimPenalidade, penalidade.getDataFimPenalidade());
    }


    @Test
    @DisplayName("Deve falhar se alunoId for nulo")
    void deveFalharSemAlunoId() {
        assertThrows(RuntimeException.class, () ->
                PenalidadeBuilder.builder().dataFimPenalidade(LocalDate.now()).build()
        );
    }
}