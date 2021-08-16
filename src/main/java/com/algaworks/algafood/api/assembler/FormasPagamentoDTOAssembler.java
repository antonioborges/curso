package com.algaworks.algafood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.dto.FormasPagamentoDTO;
import com.algaworks.algafood.domain.model.FormasPagamento;

@Component
public class FormasPagamentoDTOAssembler {

	private ModelMapper modelMapper;

	public FormasPagamentoDTOAssembler(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	public FormasPagamentoDTO toDTO(FormasPagamento formasPagamento) {
		return modelMapper.map(formasPagamento, FormasPagamentoDTO.class);
	}

	public List<FormasPagamentoDTO> toCollectionDTO(List<FormasPagamento> formasPagamentos) {
		return formasPagamentos.stream()

				.map(formasPagamento -> toDTO(formasPagamento))

				.collect(Collectors.toList());

	}

}
