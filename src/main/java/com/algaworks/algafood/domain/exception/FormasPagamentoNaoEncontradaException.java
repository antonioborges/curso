package com.algaworks.algafood.domain.exception;

public class FormasPagamentoNaoEncontradaException extends EntidadeNaoEncontradaException {
	private static final long serialVersionUID = 1L;

	public FormasPagamentoNaoEncontradaException(String mensagem) {
		super(mensagem);
	}

	public FormasPagamentoNaoEncontradaException(Long id) {
		this(String.format("Não existe um cadastro de forma de pagamento com o código %d", id));
	}
}
