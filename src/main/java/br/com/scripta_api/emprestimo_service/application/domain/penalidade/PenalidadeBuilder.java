package br.com.scripta_api.emprestimo_service.application.domain.penalidade;

import java.time.LocalDate;

public class PenalidadeBuilder {
    private Penalidade penalidade;

    private PenalidadeBuilder() {
        this.penalidade = new Penalidade();
    }

    public static PenalidadeBuilder buidler() {
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

    // TODO: [TDD/RN] No método build(), adicionar validações (ex: alunoId e dataFimPenalidade não podem ser nulos).
    public Penalidade build() {
        return this.penalidade;
    }

}
