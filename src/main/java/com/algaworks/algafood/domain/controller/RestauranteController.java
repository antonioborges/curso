package com.algaworks.algafood.domain.controller;

import static com.algaworks.algafood.domain.service.infraestrutura.repository.spec.RestauranteSpecs.comFreteGratis;
import static com.algaworks.algafood.domain.service.infraestrutura.repository.spec.RestauranteSpecs.comNomeSemelhante;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.algafood.api.assembler.RestauranteDTOAssembler;
import com.algaworks.algafood.api.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.dto.RestauranteDTO;
import com.algaworks.algafood.api.dto.input.RestauranteInputDTO;
import com.algaworks.algafood.domain.exception.CozinhaNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;

@RestController
@RequestMapping(value = "/restaurantes")
public class RestauranteController {

	private RestauranteRepository restauranteRepository;

	private CadastroRestauranteService restauranteService;

	private RestauranteDTOAssembler restauranteDTOAssembler;

	private RestauranteInputDisassembler restauranteInputDisassembler;

	public RestauranteController(RestauranteRepository restauranteRepository,
			CadastroRestauranteService restauranteService, RestauranteDTOAssembler restauranteDTOAssembler,
			RestauranteInputDisassembler restauranteInputDisassembler) {
		this.restauranteRepository = restauranteRepository;
		this.restauranteService = restauranteService;
		this.restauranteDTOAssembler = restauranteDTOAssembler;
		this.restauranteInputDisassembler = restauranteInputDisassembler;
	}

	@GetMapping
	public List<RestauranteDTO> listar() {
		return restauranteDTOAssembler.toCollectionDTO(restauranteRepository.findAll());
	}

	@GetMapping(value = "/por-nome")
	public List<Restaurante> consultarPorNome(String nome, Long id) {
		return restauranteRepository.consultarPorNome(nome, id);
	}

	@GetMapping(value = "/por-nome-e-frete")
	public List<Restaurante> restaurantePorNomeFrete(String nome, BigDecimal taxaFreteInicial,
			BigDecimal taxaFreteFinal) {

		return restauranteRepository.find(nome, taxaFreteInicial, taxaFreteFinal);
	}

	@GetMapping(value = "/{restauranteId}")
	public RestauranteDTO buscar(@PathVariable Long restauranteId) {
		Restaurante restaurante = restauranteService.buscarOuFalhar(restauranteId);

		return restauranteDTOAssembler.toDTO(restaurante);
	}

	@GetMapping(value = "/com-frete-gratis")
	public List<Restaurante> RestauranteComFreteGratis(String nome) {

		return restauranteRepository.findAll(comFreteGratis().and(comNomeSemelhante(nome)));
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public RestauranteDTO adicionar(@RequestBody @Valid RestauranteInputDTO restauranteInputDTO) {
		Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInputDTO);
		try {

			return restauranteDTOAssembler.toDTO(restauranteService.salvar(restaurante));
		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@PutMapping("/{restauranteId}")
	public RestauranteDTO atualizar(@PathVariable Long restauranteId,
			@RequestBody @Valid RestauranteInputDTO restauranteInputDTO) {
		try {

			Restaurante restauranteAtual = restauranteService.buscarOuFalhar(restauranteId);

			restauranteInputDisassembler.copyToDomainObject(restauranteInputDTO, restauranteAtual);

			return restauranteDTOAssembler.toDTO(restauranteService.salvar(restauranteAtual));

		} catch (CozinhaNaoEncontradaException e) {
			throw new NegocioException(e.getMessage());
		}
	}

	@GetMapping(value = "/primeiro")	
	public Optional<Restaurante> restaurantePrimeiro() {
		return restauranteRepository.buscarPrimeiro();
	}

	@PutMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void ativar(@PathVariable Long restauranteId) {
		restauranteService.ativar(restauranteId);
	}

	@DeleteMapping("/{restauranteId}/ativo")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void inativar(@PathVariable Long restauranteId) {
		restauranteService.inativar(restauranteId);
	}
}
