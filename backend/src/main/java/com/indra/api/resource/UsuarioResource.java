package com.indra.api.resource;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.indra.api.model.Usuario;
import com.indra.api.model.dto.UsuarioDto;
import com.indra.api.model.dto.UsuarioNewDto;
import com.indra.api.resource.payload.ApiResponse;
import com.indra.api.service.UsuarioService;
import com.indra.api.util.PagedResult;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

	@Autowired
	private UsuarioService service;

	@ApiOperation(value = "Lista de usuários")
	@GetMapping
	public ResponseEntity<?> findPage(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
			@RequestParam(value = "limit", defaultValue = "10") Integer limit,
			@RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		Page<Usuario> listDto = service.findPage(offset, limit, orderBy, direction.toUpperCase());
		PagedResult<Usuario> result = new PagedResult<Usuario>(listDto.getContent(), listDto.getTotalElements(), listDto.getNumber(), listDto.getSize());
		
		return ResponseEntity.ok().body(result);
	}

	@ApiOperation(value = "Localizar usuário por id")
	@GetMapping("/{id}")
	public ResponseEntity<Usuario> find(@PathVariable Long id) {
		Usuario obj = service.findById(id);
		return ResponseEntity.ok().body(obj);
	}

	@ApiOperation(value = "Localizar usuário por email")
	@GetMapping("/email")
	public ResponseEntity<Usuario> find(@RequestParam(value = "email") String email) {
		Usuario obj = service.findByEmail(email);
		return ResponseEntity.ok().body(obj);
	}

	@ApiOperation(value = "Cardastrar usuário")
	@PostMapping
	public ResponseEntity<?> insert(@Valid @RequestBody UsuarioNewDto objDto) {
		Usuario obj = service.fromDTO(objDto);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.ok().body(new ApiResponse(true, "Usuário cadastrado com sucesso! uri: "+uri));
	}

	@ApiOperation(value = "Atualizar usuário")
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@Valid @RequestBody UsuarioDto objDto, @PathVariable Long id) {
		Usuario obj = service.fromDTO(objDto);
		obj.setId(id);
		obj = service.update(obj);
		return ResponseEntity.ok().body(new ApiResponse(true, "Usuário atualizado com sucesso!"));
	}

	@ApiOperation(value = "Deletar usuário")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.ok().body(new ApiResponse(true, "Usuário deletedo com sucesso!"));
	}

}
