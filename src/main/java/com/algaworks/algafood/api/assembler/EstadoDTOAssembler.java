package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.dto.EstadoDTO;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class EstadoDTOAssembler {

	private ModelMapper modelMapper;

	public EstadoDTOAssembler(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public EstadoDTO toDTO(Estado estado) {
		return modelMapper.map(estado, EstadoDTO.class);
	}

	public List<EstadoDTO> toCollectionDTO(List<Estado> estados) {
		return estados.stream()

				.map(estado -> toDTO(estado))

				.collect(Collectors.toList());
	}	
}
