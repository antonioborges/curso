package com.algaworks.algafood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.dto.input.RestauranteInputDTO;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteInputDisassembler {

	private ModelMapper modelMapper;

	public RestauranteInputDisassembler(ModelMapper modelMapper) {
		this.modelMapper = modelMapper;
	}

	// Recebe um restaurante inputDTO e retorna um restaurante.
	public Restaurante toDomainObject(RestauranteInputDTO restauranteInputDTO) {

		// passando o destino com um tipo.
		return modelMapper.map(restauranteInputDTO, Restaurante.class);
	}

	public void copyToDomainObject(RestauranteInputDTO restauranteInputDTO, Restaurante restaurante) {
		// Para evitar org.hibernate.HibernateException: identifier of an instance of
		// com.algaworks.algafood.domain.model.Cozinha was altered from 1 to 2
		restaurante.setCozinha(new Cozinha());
		modelMapper.map(restauranteInputDTO, restaurante);
	}

}
