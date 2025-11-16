package br.com.scripta_api.emprestimo_service.application.domain.emprestimo;

import java.time.LocalDate;

public class EmprestimoBuilder {
    private final Emprestimo emprestimo;

    private EmprestimoBuilder() {
        emprestimo = new Emprestimo();
    }

    public static EmprestimoBuilder builder() {
        return new EmprestimoBuilder();
    }

    public EmprestimoBuilder id(Long id) {
        emprestimo.setId(id);
        return this;
    }

    public EmprestimoBuilder alunoId(Long alunoID) {
        emprestimo.setAlunoId(alunoID);
        return this;
    }

    public EmprestimoBuilder livroId(Long livroId) {
        emprestimo.setLivroId(livroId);
        return this;
    }

    public EmprestimoBuilder dataEmprestimo(LocalDate dataEmprestimo) {
        emprestimo.setDataEmprestimo(dataEmprestimo);
        return this;
    }

    public EmprestimoBuilder dataPrevistaDevolucao(LocalDate dataPrevistaDevolucao) {
        emprestimo.setDataPrevistaDevolucao(dataPrevistaDevolucao);
        return this;
    }

    public EmprestimoBuilder dataDevolucaoReal(LocalDate dataDevolucaoReal) {
        emprestimo.setDataDevolucaoReal(dataDevolucaoReal);
        return this;
    }

    public EmprestimoBuilder renovado(boolean renovado) {
        emprestimo.setRenovado(renovado);
        return this;
    }

    public EmprestimoBuilder status(StatusEmprestimo status) {
        emprestimo.setStatus(status);
        return this;
    }

    //    TODO: [TDD/RN] No método build(), adicionar as validações de criação (ex: alunoId, livroId e status não podem ser nulos)
    public Emprestimo build() {
        return this.emprestimo;
    }
}
