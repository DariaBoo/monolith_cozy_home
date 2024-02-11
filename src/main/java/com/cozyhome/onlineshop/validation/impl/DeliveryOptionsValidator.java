package com.cozyhome.onlineshop.validation.impl;

import com.cozyhome.onlineshop.dto.order.DeliveryDto;
import com.cozyhome.onlineshop.validation.ValidDeliveryOptions;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DeliveryOptionsValidator implements ConstraintValidator<ValidDeliveryOptions, DeliveryDto> {

	@Override
	public void initialize(ValidDeliveryOptions constraintAnnotation) {
		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(DeliveryDto delivery, ConstraintValidatorContext constraintValidatorContext) {

		boolean firstDeliveryOptions = delivery.getStreet() != null && !delivery.getStreet().isEmpty() &&
				delivery.getHouse() != null && !delivery.getHouse().isEmpty() && delivery.getApartment() != null;

		boolean secondDeliveryOptions = delivery.getDeliveryCompanyName() != null && !delivery.getDeliveryCompanyName().isEmpty() &&
				delivery.getRegion() != null && !delivery.getRegion().isEmpty() &&
				delivery.getPostOffice() != null && !delivery.getPostOffice().isEmpty();

		if (firstDeliveryOptions) {
			return true;
		} else return secondDeliveryOptions;
	}
}
