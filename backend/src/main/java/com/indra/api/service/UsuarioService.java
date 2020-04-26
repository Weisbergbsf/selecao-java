package com.indra.api.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.indra.api.model.Usuario;
import com.indra.api.model.dto.UsuarioDto;
import com.indra.api.model.dto.UsuarioNewDto;
import com.indra.api.repository.UsuarioRepository;
import com.indra.api.service.exceptions.DataIntegrityException;
import com.indra.api.service.exceptions.ObjectNotFoundException;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	public Usuario findById(Long id) {

		Optional<Usuario> obj = usuarioRepository.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Usuario não encontrado! Id: " + id + ", Tipo: " + Usuario.class.getName()));

	}

	public Usuario findByEmail(String email) {

		Usuario obj = usuarioRepository.findByEmail(email);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Usuario não encontrado! Email: " + email + ", Tipo: " + Usuario.class.getName());
		}
		return obj;
	}
	
	public Page<Usuario> findPage(Integer offset, Integer limit, String orderBy, String direction) {
		direction.toUpperCase();
		PageRequest pageRequest = PageRequest.of(offset, limit, Direction.valueOf(direction), orderBy);
		return usuarioRepository.findAll(pageRequest);
	}

	@Transactional
	public Usuario insert(Usuario obj) {
		obj.setId(null);
		obj = usuarioRepository.save(obj);

		return obj;
	}

	public Usuario update(Usuario obj) {
		Usuario newObj = findById(obj.getId());
		updateData(newObj, obj);
		return usuarioRepository.save(newObj);
	}

	public void delete(Long id) {
		findById(id);
		try {
			usuarioRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há dados relacionados");
		}
	}

	private void updateData(Usuario newObj, Usuario obj) {
		newObj.setName(obj.getName());
		newObj.setEmail(obj.getEmail());
	}

	public Usuario fromDTO(UsuarioDto objDto) {
		return new Usuario(objDto.getId(), objDto.getName(), objDto.getEmail(), null);
	}

	public Usuario fromDTO(UsuarioNewDto objDto) {
		return new Usuario(null, objDto.getName(), objDto.getEmail(), passwordEncoder.encode(objDto.getPassword()));
	}
}
