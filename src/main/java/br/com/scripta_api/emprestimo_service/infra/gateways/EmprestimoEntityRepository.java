package br.com.scripta_api.emprestimo_service.infra.gateways;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.StatusEmprestimo;
import br.com.scripta_api.emprestimo_service.infra.data.EmprestimoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EmprestimoEntityRepository extends JpaRepository<EmprestimoEntity, Long> {

    Long countByAlunoIdAndStatusIn(Long alunoId, List<StatusEmprestimo> statu);

    List<EmprestimoEntity> findByStatusAndDataPrevistaDevolucaoBefore(StatusEmprestimo status, LocalDate today);
}
