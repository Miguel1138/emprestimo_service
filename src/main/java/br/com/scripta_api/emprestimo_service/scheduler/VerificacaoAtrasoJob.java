package br.com.scripta_api.emprestimo_service.scheduler;

/**
 * TODO: Anotar com @Component, @RequiredArgsConstructor.
 *
 * TODO: Injetar EmprestimoEntityRepository.
 *
 * TODO: Criar método public void verificarAtrasos().
 *
 * TODO: Anotar com @Scheduled(cron = "0 0 1 * * ?") (1h da manhã, todo dia).
 *
 * TODO: Chamar emprestimoRepo.findByStatusAndDataPrevistaDevolucaoBefore(CONFIRMADO, LocalDate.now()).
 *
 * TODO: Iterar na lista, setar o status de cada entidade para ATRASADO.
 *
 * TODO: Chamar emprestimoRepo.saveAll(listaModificada).
 */
public class VerificacaoAtrasoJob {
}
