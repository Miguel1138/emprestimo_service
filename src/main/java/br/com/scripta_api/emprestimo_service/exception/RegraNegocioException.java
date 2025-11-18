package br.com.scripta_api.emprestimo_service.exception;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.StatusEmprestimo;

public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(StatusEmprestimo statusEmprestimo) {
        super(statusEmprestimo.toString());
    }
}
