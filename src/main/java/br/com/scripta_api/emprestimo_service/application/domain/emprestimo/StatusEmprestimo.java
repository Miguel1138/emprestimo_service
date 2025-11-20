package br.com.scripta_api.emprestimo_service.application.domain.emprestimo;

public enum StatusEmprestimo {
    PENDENTE,
    CONFIRMADO,
    ATRASADO,
    DEVOLVIDO,
    RECUSADO_LIMITE,
    RECUSADO_ATRASADO,
    RECUSADO_PENALIZADO,
    JA_RENOVOU,
    FALHOU
}
