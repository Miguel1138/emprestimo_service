package br.com.scripta_api.emprestimo_service.infra.gateways;

import br.com.scripta_api.emprestimo_service.infra.data.PenalidadeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface PenalidadeEntityRepository extends JpaRepository<PenalidadeEntity, Long> {

    Optional<PenalidadeEntity> findByAlunoIdAndDataFimPenalidadeAfter(Long alunoId, LocalDate today);

    Optional<PenalidadeEntity> findByAlunoId(Long alunoId);

    boolean existsByAlunoId(Long alunoId);
}
