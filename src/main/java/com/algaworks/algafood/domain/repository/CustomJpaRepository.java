package com.algaworks.algafood.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean // O Spring Data JPA não deve instanciar uma implementaçao para essa interface.
public interface CustomJpaRepository<T, ID> extends JpaRepository<T, ID> {

	// cria a assinatura do método que será implementado.
	// usa se generics em T.
	Optional<T> buscarPrimeiro();
}
