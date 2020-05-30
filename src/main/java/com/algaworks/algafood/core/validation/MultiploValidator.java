package com.algaworks.algafood.core.validation;

import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.var;

public class MultiploValidator implements ConstraintValidator<Multiplo, Number> {
	private int numeroMultiplo;

	@Override
	public void initialize(Multiplo constraintAnnotation) {
		// TODO Auto-generated method stub
		this.numeroMultiplo = constraintAnnotation.numero();
	}

	@Override
	public boolean isValid(Number value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		boolean valido = true;
		if (value != null) {
			var valorDecimal = BigDecimal.valueOf(value.doubleValue()),
					multiploDecimal = BigDecimal.valueOf(this.numeroMultiplo),
					resto = valorDecimal.remainder(multiploDecimal);
			valido = BigDecimal.ZERO.compareTo(resto) == 0;
		}
		return valido;
	}

}
