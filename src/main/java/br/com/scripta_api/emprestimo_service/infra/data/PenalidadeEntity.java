package br.com.scripta_api.emprestimo_service.infra.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "penalidade")
@Table(name = "penalidade")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenalidadeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private Long alunoId;
    private LocalDate dataFimPenalidade;
}
