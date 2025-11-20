package br.com.scripta_api.emprestimo_service.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SolicitacaoEmprestimoRequest {
    private @NotNull Long livroId;

    public SolicitacaoEmprestimoRequest() {

    }
}
