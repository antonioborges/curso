package com.algaworks.algafood.domain.controller;

import java.util.List;

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

import com.algaworks.algafood.api.assembler.EstadoDTOAssembler;
import com.algaworks.algafood.api.assembler.EstadoInputDissassembler;
import com.algaworks.algafood.api.dto.EstadoDTO;
import com.algaworks.algafood.api.dto.input.EstadoInputDTO;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoController {

	private EstadoRepository estadoRepository;

	private CadastroEstadoService estadoService;

	private EstadoDTOAssembler estadoDTOAssembler;

	private EstadoInputDissassembler estadoInputDissassembler;

	public EstadoController(EstadoRepository estadoRepository, CadastroEstadoService estadoService,
			EstadoDTOAssembler estadoDTOAssembler, EstadoInputDissassembler estadoInputDissassembler) {
		this.estadoRepository = estadoRepository;
		this.estadoService = estadoService;
		this.estadoDTOAssembler = estadoDTOAssembler;
		this.estadoInputDissassembler = estadoInputDissassembler;
	}

	@GetMapping
	public List<EstadoDTO> listar() {
		List<Estado> todosEstados = estadoRepository.findAll();
		return estadoDTOAssembler.toCollectionDTO(todosEstados);
	}

	@GetMapping(value = "/{id}")
	public EstadoDTO buscar(@PathVariable Long id) {
		Estado estado = estadoService.buscarOuFalhar(id);
		return estadoDTOAssembler.toDTO(estado);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EstadoDTO adicionar(@RequestBody @Valid EstadoInputDTO estadoInputDTO) {
		Estado estado = estadoInputDissassembler.toDomainObject(estadoInputDTO);

		estado = estadoService.salvar(estado);

		return estadoDTOAssembler.toDTO(estado);
	}

	@PutMapping(value = "/{id}")
	public EstadoDTO atualizar(@PathVariable Long id, @RequestBody @Valid EstadoInputDTO estadoInputDTO) {
		Estado estadoAtual = estadoService.buscarOuFalhar(id);

		estadoInputDissassembler.copyToDomainObject(estadoInputDTO, estadoAtual);

		estadoAtual = estadoService.salvar(estadoAtual);

		return estadoDTOAssembler.toDTO(estadoAtual);

	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void excluir(@PathVariable Long id) {
		estadoService.excluir(id);
	}
}
