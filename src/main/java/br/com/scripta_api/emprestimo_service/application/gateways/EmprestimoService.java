package br.com.scripta_api.emprestimo_service.application.gateways;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.Emprestimo;
import br.com.scripta_api.emprestimo_service.dto.DevolucaoRequest;
import br.com.scripta_api.emprestimo_service.dto.SolicitacaoEmprestimoRequest;

import java.util.List;

public interface EmprestimoService {
    Emprestimo solicitarEmprestimo(SolicitacaoEmprestimoRequest request, String token, Long alunoId);

    Emprestimo renovarEmprestimo(Long emprestimoId, Long alunoId);

    Emprestimo registrarDevolucao(DevolucaoRequest request, String token);

    List<Emprestimo> listarEmprestimosAtivosPorAluno(Long alunoId);

    List<Emprestimo> listarHistoricoCompletoAluno(Long alunoId);
}
