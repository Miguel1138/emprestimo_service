package br.com.scripta_api.emprestimo_service.infra.data;


import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.StatusEmprestimo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "emprestimos")
@Table(name = "emprestimos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long alunoId;
    @Column(unique = true, nullable = false)
    private Long livroId;
    private LocalDate dataEmprestimo;
    private LocalDate dataPrevistaDevolucao;
    private LocalDate dataDevolucaoReal;
    @Column(nullable = false)
    private boolean renovado;
    @Enumerated(EnumType.STRING)
    private StatusEmprestimo status;
}
