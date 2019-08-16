package com.algamoney.api.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algamoney.api.model.Pessoa;
import com.algamoney.api.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public List<Pessoa> listar() {
		return this.pessoaRepository.findAll();
	}
	
	public Pessoa buscar(Long id) {
		return this.pessoaRepository.findById(id).orElse(null);
	}
		
	public Pessoa criarPessoa(Pessoa pessoa) {
		return this.pessoaRepository.save(pessoa);
	}
	
	public void remover(Long id) {
		this.pessoaRepository.deleteById(id);
	}
	
	public Pessoa atualizar(Long id, Pessoa pessoa) {
		Pessoa pessoaSalva = buscarPessoaPeloId(id);
		BeanUtils.copyProperties(pessoa, pessoaSalva, "id");
		return this.pessoaRepository.save(pessoaSalva);	
	}

	public void atualizaPropAtivo(Long id, Boolean ativo) {
		Pessoa pessoaSalva = buscarPessoaPeloId(id);
		pessoaSalva.setAtivo(ativo);
		this.pessoaRepository.save(pessoaSalva);
	}
	
	public Pessoa buscarPessoaPeloId(Long id) {
		Pessoa pessoaSalva = this.pessoaRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
		return pessoaSalva;
	}

}
