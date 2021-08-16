package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.dto.input.EstadoInputDTO;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class EstadoInputDissassembler {

	private ModelMapper modelMapper;

	public EstadoInputDissassembler(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public Estado toDomainObject(EstadoInputDTO estadoInputDTO) {
		return modelMapper.map(estadoInputDTO, Estado.class);
	}

	public void copyToDomainObject(EstadoInputDTO estadoInputDTO, Estado estado) {
		modelMapper.map(estadoInputDTO, estado);
	}
}
