package com.algaworks.algafood.domain.repository;

import java.math.BigDecimal;
import java.util.List;

import com.algaworks.algafood.domain.model.Restaurante;

public interface RestauranteRepositoryQueries {

	List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal);

	// adicionar novo método
	// sera implementado na Interface RestauranteRepostiroyImpl.java
	List<Restaurante> findComFreteGratis(String nome);

}