package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.dto.input.FormasPagamentoInputDTO;
import com.algaworks.algafood.domain.model.FormasPagamento;

@Component
public class FormasPagamentoInputDissassembler {

	private ModelMapper modelMapper;

	public FormasPagamentoInputDissassembler(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public FormasPagamento toDomainObject(FormasPagamentoInputDTO formasPagamentoInputDTO) {
		return modelMapper.map(formasPagamentoInputDTO, FormasPagamento.class);
	}

	public void copyToDomainObject(FormasPagamentoInputDTO formasPagamentoInputDTO, FormasPagamento formasPagamento) {
		modelMapper.map(formasPagamentoInputDTO, formasPagamento);
	}
}
