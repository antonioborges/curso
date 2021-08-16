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

import com.algaworks.algafood.api.assembler.CidadeDTOAssembler;
import com.algaworks.algafood.api.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.dto.CidadeDTO;
import com.algaworks.algafood.api.dto.input.CidadeInputDTO;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;

@RestController
@RequestMapping(value = "/cidades")
public class CidadeController {

	private CidadeRepository cidadeRepository;

	private CadastroCidadeService cidadeService;

	private CidadeDTOAssembler cidadeDTOAssembler;

	private CidadeInputDisassembler cidadeInputDisassembler;

	public CidadeController(CidadeRepository cidadeRepository, CadastroCidadeService cidadeService,
			CidadeDTOAssembler cidadeDTOAssembler, CidadeInputDisassembler cidadeInputDisassembler) {
		this.cidadeRepository = cidadeRepository;
		this.cidadeService = cidadeService;
		this.cidadeDTOAssembler = cidadeDTOAssembler;
		this.cidadeInputDisassembler = cidadeInputDisassembler;
	}

	@GetMapping
	public List<CidadeDTO> listar() {
		List<Cidade> todasCidades = cidadeRepository.findAll();
		return cidadeDTOAssembler.toCollectionDTO(todasCidades);
	}

	@GetMapping(value = "/{cidadeId}")
	public CidadeDTO buscar(@PathVariable Long cidadeId) {
		Cidade cidade = cidadeService.buscarOuFalhar(cidadeId);
		return cidadeDTOAssembler.toDTO(cidade);
	}

	@PostMapping
	public CidadeDTO adicionar(@RequestBody @Valid CidadeInputDTO cidadeInputDTO) {
		try {
			Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInputDTO);

			cidade = cidadeService.salvar(cidade);

			return cidadeDTOAssembler.toDTO(cidade);

		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@PutMapping(value = "/{cidadeId}")
	public CidadeDTO atualizar(@PathVariable Long cidadeId, @RequestBody @Valid CidadeInputDTO cidadeInputDTO) {
		try {

			Cidade cidadeAtual = cidadeService.buscarOuFalhar(cidadeId);

			cidadeInputDisassembler.copyToDomainObject(cidadeInputDTO, cidadeAtual);
			;

			cidadeAtual = cidadeService.salvar(cidadeAtual);

			return cidadeDTOAssembler.toDTO(cidadeAtual);

		} catch (EstadoNaoEncontradoException e) {
			throw new NegocioException(e.getMessage(), e);
		}
	}

	@DeleteMapping("/{cidadeId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long cidadeId) {
		cidadeService.excluir(cidadeId);
	}

}
