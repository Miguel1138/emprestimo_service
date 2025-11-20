package br.com.scripta_api.emprestimo_service.application.domain.penalidade;

import java.time.LocalDate;

public class PenalidadeBuilder {
    private Penalidade penalidade;

    private PenalidadeBuilder() {
        this.penalidade = new Penalidade();
    }

    public static PenalidadeBuilder builder() {
        return new PenalidadeBuilder();
    }

    public PenalidadeBuilder id(Long id) {
        this.penalidade.setId(id);
        return this;
    }

    public PenalidadeBuilder alunoId(Long alunoId) {
        this.penalidade.setAlunoId(alunoId);
        return this;
    }

    public PenalidadeBuilder dataFimPenalidade(LocalDate dataFimPenalidade) {
        this.penalidade.setDataFimPenalidade(dataFimPenalidade);
        return this;
    }

    public Penalidade build() {
        if (penalidade.getAlunoId() == null) {
            throw new RuntimeException("AlunoId nulo");
        }

        if (penalidade.getDataFimPenalidade() == null) {
            throw new RuntimeException("Data de fim penalidade nulo");
        }
        return this.penalidade;
    }

}
