package com.indra.api.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import com.indra.api.model.Usuario;
import com.indra.api.service.validation.UsuarioUpdate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@UsuarioUpdate
public class UsuarioDto {

	private Long id;

	@NotEmpty(message = "Preenchimento obrigatório")
	@Length(min = 5, max = 120, message = "O tamanho deve ser entre 5 e 120 caracteres")
	private String name;

	@NotEmpty(message = "Preenchimento obrigatório")
	@Email(message = "Email inválido")
	private String email;

	public UsuarioDto(Usuario usuario) {
		this.id = usuario.getId();
		this.name = usuario.getName();
		this.email = usuario.getEmail();
	}
}
