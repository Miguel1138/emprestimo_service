package br.com.scripta_api.emprestimo_service.repository;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.Emprestimo;
import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.EmprestimoBuilder;
import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.StatusEmprestimo;
import br.com.scripta_api.emprestimo_service.application.gateways.EmprestimoService;
import br.com.scripta_api.emprestimo_service.dto.DevolucaoRequest;
import br.com.scripta_api.emprestimo_service.dto.SolicitacaoEmprestimoRequest;
import br.com.scripta_api.emprestimo_service.exception.RegraNegocioException;
import br.com.scripta_api.emprestimo_service.infra.data.EmprestimoEntity;
import br.com.scripta_api.emprestimo_service.infra.data.PenalidadeEntity;
import br.com.scripta_api.emprestimo_service.infra.gateways.EmprestimoEntityRepository;
import br.com.scripta_api.emprestimo_service.infra.gateways.PenalidadeEntityRepository;
import br.com.scripta_api.emprestimo_service.integration.CatalogoServiceOrquestrador;
import br.com.scripta_api.emprestimo_service.repository.mapper.EmprestimoMapper;
import br.com.scripta_api.emprestimo_service.repository.mapper.PenalidadeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EmprestimoRepository implements EmprestimoService {
    private final EmprestimoEntityRepository emprestimoEntityRepository;
    private final PenalidadeEntityRepository penalidadeEntityRepository;
    private final EmprestimoMapper emprestimoMapper;
    private final PenalidadeMapper penalidadeMapper;
    private final CatalogoServiceOrquestrador serviceOrquestrador;

    @Transactional
    @Override
    public Emprestimo solicitarEmprestimo(SolicitacaoEmprestimoRequest request, String token, Long alunoId) {
        Optional<PenalidadeEntity> temPenalidade =
                penalidadeEntityRepository.findByAlunoIdAndDataFimPenalidadeAfter(alunoId, LocalDate.now());
        if (temPenalidade.isPresent()) {
            throw new RegraNegocioException(StatusEmprestimo.RECUSADO_PENALIZADO);
        }
        List<EmprestimoEntity> livrosLocadosPeloAluno =
                emprestimoEntityRepository.findEmprestimoEntityByAlunoIdAndStatusIn(
                        alunoId,
                        List.of(StatusEmprestimo.CONFIRMADO, StatusEmprestimo.ATRASADO));

        if (livrosLocadosPeloAluno.size() >= 5) {
            throw new RegraNegocioException(StatusEmprestimo.RECUSADO_LIMITE);
        }

        if (livrosLocadosPeloAluno.stream().findFirst().get().getStatus() == StatusEmprestimo.ATRASADO) {
            throw new RegraNegocioException(StatusEmprestimo.RECUSADO_ATRASADO);
        }

        Emprestimo domain = EmprestimoBuilder.builder()
                .alunoId(alunoId)
                .livroId(request.getLivroId())
                .status(StatusEmprestimo.PENDENTE)
                .dataEmprestimo(LocalDate.now())
                .build();

        EmprestimoEntity savedEntity = emprestimoEntityRepository.save(emprestimoMapper.toEntity(domain));
        domain = emprestimoMapper.toDomain(savedEntity);
        try {
            serviceOrquestrador.decrementarEstoque(domain, token);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return domain;
    }

    @Transactional
    @Override
    public Emprestimo renovarEmprestimo(Long emprestimoId, Long alunoId) {
        if (penalidadeEntityRepository.existsById(alunoId)) {
            throw new RegraNegocioException(StatusEmprestimo.RECUSADO_PENALIZADO);
        }

        var renovaLivro = emprestimoEntityRepository.findById(emprestimoId).orElseThrow();
        if (renovaLivro.isRenovado()) {
            throw new RegraNegocioException(StatusEmprestimo.JA_RENOVOU);
        }

        renovaLivro.setRenovado(true);
        renovaLivro.setDataPrevistaDevolucao(renovaLivro
                .getDataPrevistaDevolucao()
                .plusDays(30));
        return emprestimoMapper.toDomain(emprestimoEntityRepository.save(renovaLivro));
    }

    @Transactional
    @Override
    public Emprestimo registrarDevolucao(DevolucaoRequest request, String token) {
        Optional<EmprestimoEntity> livroEntity = emprestimoEntityRepository.findById(request.getEmprestimoId());
        if (livroEntity.isEmpty()) {
            throw new RegraNegocioException(StatusEmprestimo.FALHOU);
        }

        EmprestimoEntity livroDevolvido = livroEntity.get();
        livroDevolvido.setStatus(StatusEmprestimo.DEVOLVIDO);
        if (livroDevolvido.getDataDevolucaoReal().isAfter(livroDevolvido.getDataPrevistaDevolucao())) {
            Period penalidadePorAtraso = Period.between(livroDevolvido.getDataPrevistaDevolucao(), livroDevolvido.getDataDevolucaoReal());
            if (!penalidadeEntityRepository.existsByAlunoId(livroDevolvido.getAlunoId())) {
                PenalidadeEntity newPenalidade = PenalidadeEntity.builder()
                        .alunoId(livroDevolvido.getAlunoId())
                        .dataFimPenalidade(LocalDate.of(
                                penalidadePorAtraso.getYears(),
                                penalidadePorAtraso.getMonths(),
                                penalidadePorAtraso.getDays())
                        ).build();
                penalidadeEntityRepository.save(newPenalidade);
            }
        }
        serviceOrquestrador.incrementarEstoque(livroDevolvido.getLivroId(), token);
        return emprestimoMapper.toDomain(livroDevolvido);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Emprestimo> listarEmprestimosAtivosPorAluno(Long alunoId) {
        return emprestimoEntityRepository.findAllByAlunoIdAndStatusIn(
                        alunoId,
                        List.of(StatusEmprestimo.PENDENTE, StatusEmprestimo.CONFIRMADO, StatusEmprestimo.ATRASADO))
                .map(emprestimoMapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Emprestimo> listarHistoricoCompletoAluno(Long alunoId) {
        return emprestimoEntityRepository.findAllByAlunoId(alunoId)
                .map(emprestimoMapper::toDomain)
                .toList();
    }
}
