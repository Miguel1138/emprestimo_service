package br.com.scripta_api.emprestimo_service.dto;

import jakarta.validation.constraints.NotNull;

public record DevolucaoRequest(
        @NotNull(message = "Id do emprestimo é obrigatório") Long emprestimoId
) {
}
