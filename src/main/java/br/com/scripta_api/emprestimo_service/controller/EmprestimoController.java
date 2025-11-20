package br.com.scripta_api.emprestimo_service.controller;

import br.com.scripta_api.emprestimo_service.application.domain.emprestimo.Emprestimo;
import br.com.scripta_api.emprestimo_service.application.gateways.EmprestimoService;
import br.com.scripta_api.emprestimo_service.dto.DevolucaoRequest;
import br.com.scripta_api.emprestimo_service.dto.EmprestimoResponse;
import br.com.scripta_api.emprestimo_service.dto.SolicitacaoEmprestimoRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    /**
     * Solicita o empréstimo de um livro.
     * Acesso: ROLE_ALUNO
     *
     * @param request DTO com o ID do livro.
     * @param token Token JWT (necessário para chamadas externas ao Catálogo).
     * @param authentication Dados do usuário logado (extraídos do Token pelo Spring Security).
     * @return 202 Accepted (Processamento Assíncrono/Eventual) com os dados preliminares.
     */
    @PostMapping
    public ResponseEntity<EmprestimoResponse> solicitarEmprestimo(
            @Valid @RequestBody SolicitacaoEmprestimoRequest request,
            @RequestHeader("Authorization") String token,
            Authentication authentication) {

        // Extrai o ID do aluno do token (Subject).
        // OBS: Se a matrícula for String e o Service esperar Long, faça a conversão aqui.
        // Assumindo que o subject do token é o ID ou uma matrícula numérica válida.
        Long alunoId = Long.parseLong(authentication.getName());

        Emprestimo emprestimo = emprestimoService.solicitarEmprestimo(request, token, alunoId);
        return ResponseEntity.accepted()
                .body(EmprestimoResponse.fromDomain(emprestimo));
    }

    /**
     * Renova um empréstimo existente.
     * Acesso: ROLE_ALUNO
     *
     * @param id ID do empréstimo a ser renovado.
     * @param authentication Dados do usuário logado.
     * @return 200 OK com os dados atualizados.
     */
    @PostMapping("/{id}/renovar")
    public ResponseEntity<EmprestimoResponse> renovarEmprestimo(
            @PathVariable Long id,
            Authentication authentication) {

        Long alunoId = Long.parseLong(authentication.getName());
        Emprestimo emprestimoRenovado = emprestimoService.renovarEmprestimo(id, alunoId);

        return ResponseEntity.ok(EmprestimoResponse.fromDomain(emprestimoRenovado));
    }

    /**
     * Registra a devolução de um livro.
     * Acesso: ROLE_BIBLIOTECARIO
     *
     * @param request DTO com o ID do empréstimo.
     * @return 200 OK com os dados atualizados (status DEVOLVIDO).
     */
    @PostMapping("/devolucao")
    public ResponseEntity<EmprestimoResponse> registrarDevolucao(
            @Valid @RequestBody DevolucaoRequest request,
            @RequestHeader("Authorization") String token
    ) {

        // O bibliotecário registra a devolução, não precisa validar o ID do usuário no token
        Emprestimo emprestimoDevolvido = emprestimoService.registrarDevolucao(request, token);

        return ResponseEntity.ok(EmprestimoResponse.fromDomain(emprestimoDevolvido));
    }

    /**
     * Lista os empréstimos ativos (não devolvidos) do aluno logado.
     * Acesso: ROLE_ALUNO
     *
     * @param authentication Dados do usuário logado.
     * @return Lista de empréstimos.
     */
    @GetMapping("/meus")
    public ResponseEntity<List<EmprestimoResponse>> listarMeusEmprestimos(
            Authentication authentication) {

        Long alunoId = Long.parseLong(authentication.getName());

        List<Emprestimo> emprestimos = emprestimoService.listarEmprestimosAtivosPorAluno(alunoId);

        List<EmprestimoResponse> response = emprestimos.stream()
                .map(EmprestimoResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * Lista o histórico completo de empréstimos de um aluno específico.
     * Acesso: ROLE_BIBLIOTECARIO
     *
     * @param alunoId ID do aluno a ser consultado.
     * @return Lista de empréstimos.
     */
    @GetMapping("/historico/{alunoId}")
    public ResponseEntity<List<EmprestimoResponse>> listarHistoricoAluno(
            @PathVariable Long alunoId) {

        List<Emprestimo> emprestimos = emprestimoService.listarHistoricoCompletoAluno(alunoId);

        List<EmprestimoResponse> response = emprestimos.stream()
                .map(EmprestimoResponse::fromDomain)
                .toList();

        return ResponseEntity.ok(response);
    }
}