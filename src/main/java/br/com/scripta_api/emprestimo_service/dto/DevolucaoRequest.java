package br.com.scripta_api.emprestimo_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DevolucaoRequest {
    private @NotNull(message = "Id do emprestimo é obrigatório") Long emprestimoId;

    public DevolucaoRequest() {

    }
}
