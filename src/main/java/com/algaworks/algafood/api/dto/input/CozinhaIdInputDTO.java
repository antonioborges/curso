package com.algaworks.algafood.api.dto.input;

import com.sun.istack.NotNull;

public class CozinhaIdInputDTO {

	@NotNull
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
