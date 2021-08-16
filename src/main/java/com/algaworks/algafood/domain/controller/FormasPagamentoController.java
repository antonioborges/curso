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

import com.algaworks.algafood.api.assembler.FormasPagamentoDTOAssembler;
import com.algaworks.algafood.api.assembler.FormasPagamentoInputDissassembler;
import com.algaworks.algafood.api.dto.FormasPagamentoDTO;
import com.algaworks.algafood.api.dto.input.FormasPagamentoInputDTO;
import com.algaworks.algafood.domain.model.FormasPagamento;
import com.algaworks.algafood.domain.repository.FormasPagamentoRepository;
import com.algaworks.algafood.domain.service.CadastroFormasPagamentoService;

@RestController
@RequestMapping("/formas-pagamento")
public class FormasPagamentoController {

	private FormasPagamentoRepository formasPagamentoRepository;

	private CadastroFormasPagamentoService formasPagamentoService;

	private FormasPagamentoDTOAssembler formasPagamentoDTOAssembler;

	private FormasPagamentoInputDissassembler formasPagamentoInputDissassembler;

	public FormasPagamentoController(FormasPagamentoRepository formasPagamentoRepository,
			CadastroFormasPagamentoService formasPagamentoService,
			FormasPagamentoDTOAssembler formasPagamentoDTOAssembler,
			FormasPagamentoInputDissassembler formasPagamentoInputDissassembler) {
		this.formasPagamentoRepository = formasPagamentoRepository;
		this.formasPagamentoService = formasPagamentoService;
		this.formasPagamentoDTOAssembler = formasPagamentoDTOAssembler;
		this.formasPagamentoInputDissassembler = formasPagamentoInputDissassembler;
	}

	@GetMapping
	public List<FormasPagamentoDTO> listar() {

		List<FormasPagamento> todasFormasPagamento = formasPagamentoRepository.findAll();

		return formasPagamentoDTOAssembler.toCollectionDTO(todasFormasPagamento);
	}

	@GetMapping("/{id}")
	public FormasPagamentoDTO buscar(@PathVariable Long id) {

		FormasPagamento formasPagamento = formasPagamentoService.buscarOuFalhar(id);

		return formasPagamentoDTOAssembler.toDTO(formasPagamento);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public FormasPagamentoDTO adicionar(@RequestBody @Valid FormasPagamentoInputDTO formasPagamentoInputDTO) {

		FormasPagamento formasPagamento = formasPagamentoInputDissassembler.toDomainObject(formasPagamentoInputDTO);

		formasPagamento = formasPagamentoService.salvar(formasPagamento);

		return formasPagamentoDTOAssembler.toDTO(formasPagamento);
	}

	@PutMapping("{id}")
	public FormasPagamentoDTO atualizar(@PathVariable Long id,
			@RequestBody @Valid FormasPagamentoInputDTO formasPagamentoInputDTO) {

		FormasPagamento formasPagamentoAtual = formasPagamentoService.buscarOuFalhar(id);

		formasPagamentoInputDissassembler.copyToDomainObject(formasPagamentoInputDTO, formasPagamentoAtual);

		formasPagamentoAtual = formasPagamentoService.salvar(formasPagamentoAtual);

		return formasPagamentoDTOAssembler.toDTO(formasPagamentoAtual);
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long id) {

		formasPagamentoService.excluir(id);
	}
}
