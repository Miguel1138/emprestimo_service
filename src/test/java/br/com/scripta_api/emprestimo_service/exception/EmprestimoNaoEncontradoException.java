package br.com.scripta_api.emprestimo_service.exception;

public class EmprestimoNaoEncontradoException extends RuntimeException {
    public EmprestimoNaoEncontradoException(String message) {
        super(message);
    }
}
