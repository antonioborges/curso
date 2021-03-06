	package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.dto.input.CidadeInputDTO;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;

@Component
public class CidadeInputDisassembler {

	private ModelMapper modelMapper;

	public CidadeInputDisassembler(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public Cidade toDomainObject(CidadeInputDTO cidadeInputDTO) {
		return modelMapper.map(cidadeInputDTO, Cidade.class);
	}

	public void copyToDomainObject(CidadeInputDTO cidadeInputDTO, Cidade cidade) {
		// Para evitar org.hibernate.HibernateException: identifier of an instance of
		// com.algaworks.algafood.domain.model.Estado was altered from 1 to 2
		cidade.setEstado(new Estado());

		modelMapper.map(cidadeInputDTO, cidade);
	}
}
