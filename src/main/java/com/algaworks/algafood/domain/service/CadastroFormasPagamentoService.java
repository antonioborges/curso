package com.algaworks.algafood.domain.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.FormasPagamentoNaoEncontradaException;
import com.algaworks.algafood.domain.model.FormasPagamento;
import com.algaworks.algafood.domain.repository.FormasPagamentoRepository;

@Service
public class CadastroFormasPagamentoService {

	private static final String MSG_FORMA_PAGAMENTO_EM_USO = "Forma de pagamento de código %d não pode ser removida, pois está em uso";

	private FormasPagamentoRepository formasPagamentoRepository;

	public CadastroFormasPagamentoService(FormasPagamentoRepository formasPagamentoRepository) {
		this.formasPagamentoRepository = formasPagamentoRepository;
	}

	@Transactional
	public FormasPagamento salvar(FormasPagamento formasPagamento) {

		return formasPagamentoRepository.save(formasPagamento);
	}

	@Transactional
	public void excluir(Long id) {

		try {
			formasPagamentoRepository.deleteById(id);
			formasPagamentoRepository.flush();

		} catch (EmptyResultDataAccessException e) {
			throw new FormasPagamentoNaoEncontradaException(id);

		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(String.format(MSG_FORMA_PAGAMENTO_EM_USO, id));
		}
	}

	public FormasPagamento buscarOuFalhar(Long id) {

		return formasPagamentoRepository.findById(id)

				.orElseThrow(() -> new FormasPagamentoNaoEncontradaException(id));
	}
}
