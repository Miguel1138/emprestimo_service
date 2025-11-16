package br.com.scripta_api.emprestimo_service.repository;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.Emprestimo;
import br.com.scripta_api.emprestimo_service.application.gateways.EmprestimoService;
import br.com.scripta_api.emprestimo_service.dto.DevolucaoRequest;
import br.com.scripta_api.emprestimo_service.dto.SolicitacaoEmprestimoRequest;
import br.com.scripta_api.emprestimo_service.infra.gateways.EmprestimoEntityRepository;
import br.com.scripta_api.emprestimo_service.repository.mapper.EmprestimoMapper;
import br.com.scripta_api.emprestimo_service.repository.mapper.PenalidadeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmprestimoRepository implements EmprestimoService {
    private final EmprestimoEntityRepository repository;
    private final EmprestimoMapper mapper;
    private final PenalidadeMapper penalidadeMapper;

    /**
     * TODO: Implementar solicitarEmprestimo (RF-A05):
     *
     * TODO: [RN 4] Verificar penalidadeRepository.findByAlunoId.... Se existir, lançar RegraNegocioException (RECUSADO_PENALIZADO).
     *
     * TODO: [RN 1] Verificar emprestimoRepository.countByAlunoId.... Se atrasado, lançar (RECUSADO_ATRASADO). Se limite >= 5, lançar (RECUSADO_LIMITE).
     *
     * TODO: Criar Emprestimo (domínio) com status PENDENTE.
     *
     * TODO: Salvar no banco (via mapper e repository).
     *
     * TODO: [RNF-08] Chamar catalogoOrquestrador.decrementarEstoque(livroId, token) (método @Async).
     *
     * TODO: Implementar registrarDevolucao (RF-B07):
     *
     * TODO: Buscar o Empréstimo. Atualizar status para DEVOLVIDO.
     *
     * TODO: [RN 4] Verificar se dataDevolucao > dataPrevista. Se sim, calcular dias corridos e salvar Penalidade.
     *
     * TODO: [RNF-08] Chamar catalogoOrquestrador.incrementarEstoque(livroId, token) (método @Async).
     *
     * TODO: Implementar renovarEmprestimo (RF-A07 / RN 3).
     *
     * TODO: Implementar os métodos de listagem (RF-A06, RF-B08).
     * @param request
     * @param token
     * @param alunoId
     * @return
     */
    @Override
    public Emprestimo solicitarEmprestimo(SolicitacaoEmprestimoRequest request, String token, Long alunoId) {
        return null;
    }

    @Override
    public Emprestimo renovarEmprestimo(Long emprestimoId, Long alunoId) {
        return null;
    }

    @Override
    public Emprestimo registrarDevolucao(DevolucaoRequest request) {
        return null;
    }

    @Override
    public List<Emprestimo> listarEmprestimosAtivosPorAluno(Long alunoId) {
        return List.of();
    }

    @Override
    public List<Emprestimo> listarHistoricoCompletoAluno(Long alunoId) {
        return List.of();
    }
}
