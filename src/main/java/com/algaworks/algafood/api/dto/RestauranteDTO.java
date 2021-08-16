package com.algaworks.algafood.api.dto;

import java.math.BigDecimal;

//DTO -> para saída de dados.
public class RestauranteDTO {

	private Long id;

	private String nome;

	private BigDecimal taxaFrete;

	private CozinhaDTO cozinha;

	private Boolean ativo;

	private EnderecoDTO endereco;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getTaxaFrete() {
		return taxaFrete;
	}

	public void setTaxaFrete(BigDecimal taxaFrete) {
		this.taxaFrete = taxaFrete;
	}

	public CozinhaDTO getCozinha() {
		return cozinha;
	}

	public void setCozinha(CozinhaDTO cozinha) {
		this.cozinha = cozinha;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public EnderecoDTO getEndereco() {
		return endereco;
	}

	public void setEndereco(EnderecoDTO endereco) {
		this.endereco = endereco;
	}

}
