package com.algaworks.algafood.domain.controller;

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

import com.algaworks.algafood.api.assembler.CozinhaDTOAssembler;
import com.algaworks.algafood.api.assembler.CozinhaInputDisassembler;
import com.algaworks.algafood.api.dto.CozinhaDTO;
import com.algaworks.algafood.api.dto.input.CozinhaInputDTO;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;

@RestController
@RequestMapping(value = "/cozinhas")
public class CozinhaController {

	private CadastroCozinhaService cozinhaService;

	private CozinhaRepository cozinhaRepository;

	private CozinhaDTOAssembler cozinhaDTOAssembler;

	private CozinhaInputDisassembler cozinhaInputDisassembler;

	public CozinhaController(CadastroCozinhaService cozinhaService, CozinhaRepository cozinhaRepository,
			CozinhaDTOAssembler cozinhaDTOAssembler, CozinhaInputDisassembler cozinhaInputDisassembler) {
		this.cozinhaService = cozinhaService;
		this.cozinhaRepository = cozinhaRepository;
		this.cozinhaDTOAssembler = cozinhaDTOAssembler;
		this.cozinhaInputDisassembler = cozinhaInputDisassembler;
	}

	@GetMapping
	public List<CozinhaDTO> listar() {
		List<Cozinha> todasCozinhas = cozinhaRepository.findAll();
		return cozinhaDTOAssembler.toCollectionDTO(todasCozinhas);
	}

	@GetMapping("/{id}")
	public CozinhaDTO buscar(@PathVariable Long id) {
		Cozinha cozinha = cozinhaService.buscarOuFalhar(id);
		return cozinhaDTOAssembler.toDTO(cozinha);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CozinhaDTO adicionar(@RequestBody @Valid CozinhaInputDTO cozinhaInputDTO) {
		Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInputDTO);

		cozinha = cozinhaService.salvar(cozinha);

		return cozinhaDTOAssembler.toDTO(cozinha);
	}

	@PutMapping("/{cozinhaId}")
	public CozinhaDTO atualizar(@PathVariable Long cozinhaId, @RequestBody CozinhaInputDTO cozinhaInputDTO) {
		Cozinha cozinhaAtual = cozinhaService.buscarOuFalhar(cozinhaId);

		cozinhaInputDisassembler.copyToDomainObject(cozinhaInputDTO, cozinhaAtual);

		cozinhaAtual = cozinhaService.salvar(cozinhaAtual);

		return cozinhaDTOAssembler.toDTO(cozinhaAtual);

	}

	@DeleteMapping("/{cozinhaId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cozinhaId) {
		cozinhaService.excluir(cozinhaId);
	}

	@GetMapping(value = "/primeira")
	public Optional<Cozinha> cozinhaPrimeiro() {
		return cozinhaRepository.buscarPrimeiro();
	}

}