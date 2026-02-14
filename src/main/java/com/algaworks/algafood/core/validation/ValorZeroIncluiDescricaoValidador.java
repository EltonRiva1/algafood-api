package com.algaworks.algafood.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ValidationException;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

public class ValorZeroIncluiDescricaoValidador implements ConstraintValidator<ValorZeroIncluiDescricao, Object> {
	private String valorField, descricaoField, descricaoObrigatoria;

	@Override
	public void initialize(ValorZeroIncluiDescricao constraintAnnotation) {
		this.valorField = constraintAnnotation.valorField();
		this.descricaoField = constraintAnnotation.descricaoField();
		this.descricaoObrigatoria = constraintAnnotation.descricaoObrigatoria();
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {
		var valido = true;
		try {
			var valor = (BigDecimal) BeanUtils.getPropertyDescriptor(value.getClass(), this.valorField).getReadMethod()
					.invoke(value);
			var descricao = (String) BeanUtils.getPropertyDescriptor(value.getClass(), this.descricaoField)
					.getReadMethod().invoke(value);
			if (valor != null && BigDecimal.ZERO.compareTo(valor) == 0 && descricao != null)
				valido = descricao.toLowerCase().contains(this.descricaoObrigatoria.toLowerCase());
			return valido;
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}
}
