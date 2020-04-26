package com.indra.api.service.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.indra.api.model.Usuario;
import com.indra.api.model.dto.UsuarioNewDto;
import com.indra.api.repository.UsuarioRepository;
import com.indra.api.resource.exception.FieldMessage;

public class UsuarioInsertValidator implements ConstraintValidator<UsuarioInsert, UsuarioNewDto> {

	@Autowired
	private UsuarioRepository repository;

	@Override
	public void initialize(UsuarioInsert constraintAnnotation) {
	}

	@Override
	public boolean isValid(UsuarioNewDto objDto, ConstraintValidatorContext context) {

		List<FieldMessage> list = new ArrayList<>();

		Usuario aux = repository.findByEmail(objDto.getEmail());
		if (aux != null) {
			list.add(new FieldMessage("email", "Email já existente"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}

}
