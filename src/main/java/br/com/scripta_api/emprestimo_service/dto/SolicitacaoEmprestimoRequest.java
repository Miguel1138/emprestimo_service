package br.com.scripta_api.emprestimo_service.dto;

import jakarta.validation.constraints.NotNull;

public record SolicitacaoEmprestimoRequest(
        @NotNull Long livroId
) {
}
