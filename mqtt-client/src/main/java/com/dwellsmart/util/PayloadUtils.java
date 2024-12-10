package com.dwellsmart.util;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.dwellsmart.constants.ErrorCode;
import com.dwellsmart.dto.MeterOperationPayload;
import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.exception.ConstraintViolationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

@Service
public class PayloadUtils {

	private final ObjectMapper objectMapper;
	private final Validator validator;

	public PayloadUtils(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
	}

	public MeterOperationPayload validateRequest(String jsonRequest) throws ApplicationException {
		MeterOperationPayload request;
		try {
			request = objectMapper.readValue(jsonRequest, MeterOperationPayload.class);
		} catch (JsonProcessingException e) {
			String message = this.optimizeJsonErrorMessage(e.getOriginalMessage());
			throw new ApplicationException(ErrorCode.INVALID_JSON, message);
		}
		
		System.out.println("Request passed successfully.."+request);

		Set<ConstraintViolation<MeterOperationPayload>> violations = validator.validate(request);

		if (!violations.isEmpty()) {
			StringBuilder errorMessages = new StringBuilder();

			for (ConstraintViolation<MeterOperationPayload> violation : violations) {

				Path propertyPath = violation.getPropertyPath();
				String message = violation.getMessage();

				System.out.println(propertyPath.toString() +"\n"+ message);

				errorMessages.append(violation.getPropertyPath()).append(": ").append(violation.getMessage());
				break;
			}

			throw new ApplicationException("Validation failed: " + errorMessages);
		}

		return request;
	}

	public <T> byte[] convertToResponseAsBytes(T obj) {
		

		try {
			String asString = objectMapper.writeValueAsString(obj);
			System.out.println("this is response as string: \n" + asString);
			return objectMapper.writeValueAsBytes(obj);
		} catch (JsonProcessingException e) {
			throw new ApplicationException(ErrorCode.GENERIC_EXCEPTION);
		}

	}

	private String optimizeJsonErrorMessage(String originalMessage) {
		if (originalMessage == null || originalMessage.isBlank()) {
			return "request: Unknown error occurred while processing JSON.";
		}
		if (originalMessage.contains("Unexpected character")) {
			return "structure: Unexpected character found in JSON, possibly a missing comma or bracket.";
		}
		if (originalMessage.contains("Cannot construct instance")) {
			return "request: Invalid value in JSON.";
		}
		if (originalMessage.contains("Unrecognized field")) {
			return "structure: Unrecognized field found in JSON.";
		}
		if (originalMessage.contains("Cannot deserialize value")) {
			return "structure: Unexpected value type in JSON.";
		}
		return originalMessage;
	}

}
