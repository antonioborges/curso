package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.dto.input.CozinhaInputDTO;
import com.algaworks.algafood.domain.model.Cozinha;

@Component
public class CozinhaInputDisassembler {

	private ModelMapper modelMapper;

	public CozinhaInputDisassembler(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public Cozinha toDomainObject(CozinhaInputDTO cozinhaInputDTO) {
		return modelMapper.map(cozinhaInputDTO, Cozinha.class);
	}

	public void copyToDomainObject(CozinhaInputDTO cozinhaInputDTO, Cozinha cozinha) {
		modelMapper.map(cozinhaInputDTO, cozinha);
	}
}
