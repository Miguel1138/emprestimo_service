package br.com.scripta_api.emprestimo_service.application.domain.emprestimo;

import java.time.LocalDate;

public class Emprestimo {
    private Long id;
    private Long alunoId;
    private Long livroId;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucaoReal;
    private boolean renovado;
    private StatusEmprestimo status;

    protected Emprestimo() {

    }

    protected Emprestimo(Emprestimo emprestimo) {
        this.id = emprestimo.id;
        this.alunoId = emprestimo.alunoId;
        this.livroId = emprestimo.livroId;
        this.dataEmprestimo = emprestimo.dataEmprestimo;
        this.dataPrevistaDevolucao = emprestimo.dataPrevistaDevolucao;
        this.dataDevolucaoReal = emprestimo.dataDevolucaoReal;
        this.renovado = emprestimo.renovado;
        this.status = emprestimo.status;
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

    public Long getLivroId() {
        return livroId;
    }

    public void setLivroId(Long livroId) {
        this.livroId = livroId;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public LocalDate getDataDevolucaoReal() {
        return dataDevolucaoReal;
    }

    public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
        this.dataDevolucaoReal = dataDevolucaoReal;
    }

    public boolean isRenovado() {
        return renovado;
    }

    public void setRenovado(boolean renovado) {
        this.renovado = renovado;
    }

    public StatusEmprestimo getStatus() {
        return status;
    }

    public void setStatus(StatusEmprestimo status) {
        this.status = status;
    }
}
