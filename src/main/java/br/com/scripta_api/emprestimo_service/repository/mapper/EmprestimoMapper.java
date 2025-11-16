package br.com.scripta_api.emprestimo_service.repository.mapper;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.Emprestimo;
import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.EmprestimoBuilder;
import br.com.scripta_api.emprestimo_service.infra.data.EmprestimoEntity;
import org.springframework.stereotype.Component;

@Component
public class EmprestimoMapper {
    public Emprestimo toDomain(EmprestimoEntity entity) {
        return EmprestimoBuilder.builder()
                .id(entity.getId())
                .alunoId(entity.getAlunoId())
                .livroId(entity.getLivroId())
                .dataEmprestimo(entity.getDataEmprestimo())
                .dataDevolucaoReal(entity.getDataDevolucaoReal())
                .dataPrevistaDevolucao(entity.getDataPrevistaDevolucao())
                .renovado(entity.isRenovado())
                .status(entity.getStatus())
                .build();
    }

    public EmprestimoEntity toEntity(Emprestimo domain) {
        return EmprestimoEntity.builder()
                .id(domain.getId())
                .alunoId(domain.getAlunoId())
                .livroId(domain.getLivroId())
                .dataEmprestimo(domain.getDataEmprestimo())
                .dataDevolucaoReal(domain.getDataDevolucaoReal())
                .dataPrevistaDevolucao(domain.getDataPrevistaDevolucao())
                .renovado(domain.isRenovado())
                .status(domain.getStatus())
                .build();
    }
}
