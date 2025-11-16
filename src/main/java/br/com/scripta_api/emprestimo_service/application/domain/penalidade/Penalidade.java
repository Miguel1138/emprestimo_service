package br.com.scripta_api.emprestimo_service.application.domain.penalidade;

import java.time.LocalDate;

public class Penalidade {
    private Long id;
    private Long alunoId;
    private LocalDate dataFimPenalidade;

    protected Penalidade() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public LocalDate getDataFimPenalidade() {
        return dataFimPenalidade;
    }

    public void setDataFimPenalidade(LocalDate dataFimPenalidade) {
        this.dataFimPenalidade = dataFimPenalidade;
    }
}
