package com.algamoney.api.service;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.algamoney.api.model.Categoria;
import com.algamoney.api.repository.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public List<Categoria> listar() {
		return this.categoriaRepository.findAll();
	}
	
	public Categoria criar(Categoria categoria) {
		return this.categoriaRepository.save(categoria);
	}
	
	public Categoria buscar(Long id) {
		return this.categoriaRepository.findById(id).orElseThrow(null);
	}
	
	public void remover(Long id) {
		this.categoriaRepository.deleteById(id);
	}
	
	public Categoria atualizar(Categoria categoria, Long id) {
		Categoria categoriaSalva = this.categoriaRepository.findById(id).orElseThrow(() -> new EmptyResultDataAccessException(1));
		BeanUtils.copyProperties(categoria, categoriaSalva, "id");
		return categoriaRepository.save(categoriaSalva);
	}

}
