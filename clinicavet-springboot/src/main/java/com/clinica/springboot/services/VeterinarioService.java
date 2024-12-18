package com.clinica.springboot.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.clinica.springboot.controller.exceptions.DatabaseException;
import com.clinica.springboot.controller.exceptions.ResourceNotFoundException;
import com.clinica.springboot.model.entities.Veterinario;
import com.clinica.springboot.repositories.VeterinarioRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class VeterinarioService {
	
	@Autowired
	private VeterinarioRepository repository;

	public List<Veterinario> findAll() {
		return repository.findAll();
	}

	public Veterinario findById(String id) {
		Optional<Veterinario> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ResourceNotFoundException(id)); // retorna o objeto do tipo Veterinario que estiver																	// dentro do optional
	}

	public Veterinario insert(Veterinario obj) {
		return repository.save(obj);
	}

	public void delete(String id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException(e.getMessage()); //Passando exception da própria camada de serviço
		}
	}

	public Veterinario update(String id, Veterinario obj) {
		try {
			Veterinario entity = repository.getReferenceById(id); //monitorar um obj pelo jpa para depois efetuar uma op no bd
			updateData(entity, obj);
			return repository.save(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException(id);
		}
	}

	private void updateData(Veterinario entity, Veterinario obj) {
		entity.setNome(obj.getNome());
		entity.setSenha(obj.getSenha());
	}
	
	public Veterinario updatePatch(String id, Veterinario obj) {
	    try {
	        Veterinario entity = repository.getReferenceById(id);
	        partialUpdateData(entity, obj); 
	        return repository.save(entity); 
	    } catch (EntityNotFoundException e) {
	        throw new ResourceNotFoundException(id); 
	    }
	}

	private void partialUpdateData(Veterinario entity, Veterinario obj) {
		if (obj.getNome() != null) {
			entity.setNome(obj.getNome());
		}
		if (obj.getSenha() != null) {
			entity.setSenha(obj.getSenha());
		}
	}
}
