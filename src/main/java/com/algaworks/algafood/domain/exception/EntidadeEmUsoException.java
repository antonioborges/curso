package com.algaworks.algafood.domain.exception;

public class EntidadeEmUsoException extends NegocioException {
	private static final long serialVersionUID = 1L;

	public EntidadeEmUsoException(String mensagem) {
		// chama o construtor do RumTimeExcepjtion passando a mensagem que foi passada
		// com paramentro.
		super(mensagem);

	}

}
