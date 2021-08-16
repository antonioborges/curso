	package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.dto.CidadeDTO;
import com.algaworks.algafood.domain.model.Cidade;

@Component
public class CidadeDTOAssembler {

	private ModelMapper modelMapper;

	public CidadeDTOAssembler(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public CidadeDTO toDTO(Cidade cidade) {
		return modelMapper.map(cidade, CidadeDTO.class);
	}

	public List<CidadeDTO> toCollectionDTO(List<Cidade> cidades) {
		return cidades.stream()

				.map(cidade -> toDTO(cidade))

				.collect(Collectors.toList());
	}

}
