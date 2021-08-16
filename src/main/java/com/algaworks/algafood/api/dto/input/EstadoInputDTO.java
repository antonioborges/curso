package com.algaworks.algafood.api.dto.input;

import javax.validation.constraints.NotBlank;

import org.springframework.stereotype.Component;

@Component
public class EstadoInputDTO {

	@NotBlank
	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
