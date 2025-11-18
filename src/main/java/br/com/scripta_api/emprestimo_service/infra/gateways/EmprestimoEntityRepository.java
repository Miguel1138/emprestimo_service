package br.com.scripta_api.emprestimo_service.infra.gateways;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.StatusEmprestimo;
import br.com.scripta_api.emprestimo_service.infra.data.EmprestimoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface EmprestimoEntityRepository extends JpaRepository<EmprestimoEntity, Long> {
    @Query("SELECT * " +
            "FROM emprestimos e " +
            "WHERE e.alunoId = :alunoId" +
            "e.status IN (:status)" +
            "ORDER BY CASE WHEN e.status = 'ATRASADO' " +
            "   THEN 0 " +
            "   ELSE 1" +
            "    END ASC")
    List<EmprestimoEntity> findEmprestimoEntityByAlunoIdAndStatusIn(Long alunoId, List<StatusEmprestimo> status);

    List<EmprestimoEntity> findByStatusAndDataPrevistaDevolucaoBefore(StatusEmprestimo status, LocalDate today);
}
