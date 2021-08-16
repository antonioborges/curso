package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.dto.CozinhaDTO;
import com.algaworks.algafood.domain.model.Cozinha;

@Component // Indica que uma classe anotada é um "componente.
public class CozinhaDTOAssembler {

	private ModelMapper modelMapper;

	public CozinhaDTOAssembler(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public CozinhaDTO toDTO(Cozinha cozinha) {
		return modelMapper.map(cozinha, CozinhaDTO.class);
	}

	public List<CozinhaDTO> toCollectionDTO(List<Cozinha> cozinhas) {
		return cozinhas.stream()

				.map(cozinha -> toDTO(cozinha))

				.collect(Collectors.toList());
	}
}
