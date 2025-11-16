package br.com.scripta_api.emprestimo_service.controller;

import br.com.scripta_api.emprestimo_service.application.gateways.EmprestimoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * TODO: Implementar POST / (Solicitar):
 *
 * TODO: Receber @RequestBody SolicitacaoEmprestimoRequest request, @RequestHeader("Authorization") String token, e Authentication authentication.
 *
 * TODO: Extrair alunoId da authentication.getName() (ou do Principal).
 *
 * TODO: Chamar emprestimoService.solicitarEmprestimo(request, token, alunoId).
 *
 * TODO: Retornar ResponseEntity.accepted() (HTTP 202) pois é @Async (RNF-08).
 *
 * TODO: Implementar os outros endpoints (renovar, devolver, listar), mapeando DTOs para o domínio e retornando EmprestimoResponse.fromDomain().
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/emprestimos")
public class EmprestimoController {
    private final EmprestimoService service;

}
