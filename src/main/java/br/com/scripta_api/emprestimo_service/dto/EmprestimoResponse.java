package br.com.scripta_api.emprestimo_service.dto;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.Emprestimo;
import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.StatusEmprestimo;
import lombok.Builder;

import java.time.LocalDate;

/**
 * TODO:Ajustar EMprestimo para trazer a matricula do aluno.
 */

@Builder
public record EmprestimoResponse(
        Long id,
        Long livroId,
        String alunoMatricula,
        StatusEmprestimo status,
        LocalDate dataEmprestimo,
        LocalDate dataPrevistaDevolucao,
        boolean renovado
) {
    public static EmprestimoResponse fromDomain(Emprestimo domain) {
        return EmprestimoResponse.builder()
                .id(domain.getId())
                .livroId(domain.getLivroId())
                .dataEmprestimo(domain.getDataEmprestimo())
                .dataPrevistaDevolucao(domain.getDataPrevistaDevolucao())
                .renovado(domain.isRenovado())
                .build();
    }
}
