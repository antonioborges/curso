package com.algaworks.algafood.domain.service.infraestrutura.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;

//repositório SDJ customizado consulta FIXAS.

/*@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositroyQueries {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

		var jpql = "from Restaurante where nome like :nome and taxaFrete between :taxaInicial and :taxaFinal";

		return manager.createQuery(jpql, Restaurante.class).setParameter("nome", "%" + nome + "%")
				.setParameter("taxaInicial", taxaFreteInicial).setParameter("taxaFinal", taxaFreteFinal)
				.getResultList();
	}
}*/

// CONSULTA DINÂMICA COM JPQL

/*@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositroyQueries {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

		var jpql = new StringBuilder();
		jpql.append("from Restaurante where 0 = 0 "); // 0 = 0 é sempre verdadeiro.

		var parametros = new HashMap<String, Object>();

		// classe utilitária que trabalha com String, tem um método hasLength(),
		// verifica se não esta nulo e nem vazio, se o Length da String é maior que 0.
		if (StringUtils.hasLength(nome)) {
			jpql.append("and nome like :nome ");
			parametros.put("nome", "%" + nome + "%");
		}

		if (taxaFreteInicial != null) {
			jpql.append("and taxaFrete >= :taxaInicial ");
			parametros.put("taxaInicial", taxaFreteInicial);
		}

		if (taxaFreteFinal != null) {
			jpql.append("and taxaFrete <= :taxaFinal ");
			parametros.put("taxaFinal", taxaFreteFinal);
		}

		// as atribuições de parametros também precisam ser Dinãmicas
		TypedQuery<Restaurante> query = manager.createQuery(jpql.toString(), Restaurante.class);

		parametros.forEach((chave, valor) -> query.setParameter(chave, valor));

		return query.getResultList();

	}

}*/

//Criteria API não dinâmica.
/* @Repository
public class RestauranteRepositoryImpl implements RestauranteRepositroyQueries {

	@PersistenceContext
	private EntityManager manager;

	@Override
	public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

		CriteriaBuilder builder = manager.getCriteriaBuilder();

		CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);
		Root<Restaurante> root = criteria.from(Restaurante.class);

		Predicate nomePredicate = builder.like(root.get("nome"), "%" + nome + "%");

		Predicate taxaInicialPredicate = builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial);

		Predicate taxaFinalPredicate = builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal);

		criteria.where(nomePredicate, taxaInicialPredicate, taxaFinalPredicate);

		TypedQuery<Restaurante> query = manager.createQuery(criteria);
		return query.getResultList();
	}

}*/

//Criteria API dinâmica.
@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

	@PersistenceContext
	private EntityManager manager;

	private RestauranteRepository restauranteRepository;

	@Lazy // não instancia essa dependência
	// só deve ser instanciada no momento em que for usar.
	public RestauranteRepositoryImpl(RestauranteRepository restauranteRepository) {
		this.restauranteRepository = restauranteRepository;
	}

	@Override
	public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

		// CriteriaBuilder builder = manager.getCriteriaBuilder();
		var builder = manager.getCriteriaBuilder();

		// CriteriaQuery<Restaurante> criteria = builder.createQuery(Restaurante.class);
		var criteria = builder.createQuery(Restaurante.class);

		// Root<Restaurante> criteria.from(Restaurante.class);
		var root = criteria.from(Restaurante.class);

		// declara uma ArrayList, tipado com Predicate.
		var predicates = new ArrayList<Predicate>();

		// se tiver algum texto da variável nome passada como parâmetro
		if (StringUtils.hasText(nome)) {
			predicates.add(builder.like(root.get("nome"), "%" + nome + "%"));
		}
		if (taxaFreteInicial != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("taxaFrete"), taxaFreteInicial));
		}

		if (taxaFreteFinal != null) {
			predicates.add(builder.lessThanOrEqualTo(root.get("taxaFrete"), taxaFreteFinal));
		}

		// uma das formas de converter um List em um Array.
		criteria.where(predicates.toArray(new Predicate[0]));

		// TypedQuery<Restaurante> query = manager.createQuery(criteria);
		var query = manager.createQuery(criteria);
		return query.getResultList();
	}

	@Override
	public List<Restaurante> findComFreteGratis(String nome) {
		return restauranteRepository.findComFreteGratis(nome);
	}

}
