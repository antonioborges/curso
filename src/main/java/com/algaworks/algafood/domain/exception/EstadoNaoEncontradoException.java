package com.algaworks.algafood.domain.exception;

public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException {
	private static final long serialVersionUID = 1L;

	public EstadoNaoEncontradoException(String mensagem) {
		// chama o construtor de cima, passando o padrão notFound.
		super(mensagem);
	}

	public EstadoNaoEncontradoException(Long estadoId) {
		// chama o construtor acima.
		this(String.format("Não existe um cadastro de estado com código %d", estadoId));
	}

}
