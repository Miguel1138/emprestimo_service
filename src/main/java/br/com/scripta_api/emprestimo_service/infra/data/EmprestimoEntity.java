package br.com.scripta_api.emprestimo_service.infra.data;


import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.StatusEmprestimo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "emprestimos")
@Table(name = "emprestimoss")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // TODO VERIFICAR INTEGRAÇÃO COM OUTROS SERVIÇOS PARA ESSAS INFOS.
    private Long alunoId;
    private Long livroId;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucaoReal;
    private boolean renovado;
    @Enumerated(EnumType.STRING)
    private StatusEmprestimo status;
}
