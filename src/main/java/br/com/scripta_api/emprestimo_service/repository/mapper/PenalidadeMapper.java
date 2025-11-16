package br.com.scripta_api.emprestimo_service.repository.mapper;

import br.com.scripta_api.emprestimo_service.application.domain.penalidade.Penalidade;
import br.com.scripta_api.emprestimo_service.application.domain.penalidade.PenalidadeBuilder;
import br.com.scripta_api.emprestimo_service.infra.data.PenalidadeEntity;
import org.springframework.stereotype.Component;

@Component
public class PenalidadeMapper {

    public Penalidade toDomain(PenalidadeEntity entity) {
        return PenalidadeBuilder.builder()
                .id(entity.getId())
                .alunoId(entity.getAlunoId())
                .dataFimPenalidade(entity.getDataFimPenalidade())
                .build();
    }

    public PenalidadeEntity toEntity(Penalidade domain) {
        return PenalidadeEntity.builder()
                .id(domain.getId())
                .alunoId(domain.getAlunoId())
                .dataFimPenalidade(domain.getDataFimPenalidade())
                .build();
    }
}
