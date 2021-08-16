package com.algaworks.algafood.api.dto.input;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

//DTO -> para a entrada de dados.
public class CidadeInputDTO {

	@NotBlank
	private String nome;

	@Valid
	@NotNull
	private EstadoIdInputDTO estado;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public EstadoIdInputDTO getEstado() {
		return estado;
	}

	public void setEstado(EstadoIdInputDTO estado) {
		this.estado = estado;
	}

}
