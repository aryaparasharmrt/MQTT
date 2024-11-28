package com.dwellsmart.util;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.dwellsmart.constants.ErrorCode;
import com.dwellsmart.dto.MeterOperationPayload;
import com.dwellsmart.exception.ApplicationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
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

		Set<ConstraintViolation<MeterOperationPayload>> violations = validator.validate(request);

		if (!violations.isEmpty()) {
			StringBuilder errorMessages = new StringBuilder();

			for (ConstraintViolation<MeterOperationPayload> violation : violations) {

				Path propertyPath = violation.getPropertyPath();
				String message = violation.getMessage();

				System.out.println(propertyPath.toString() + message);

				errorMessages.append(violation.getPropertyPath()).append(": ").append(violation.getMessage())
						.append("\n");
				break;
			}

			throw new ApplicationException(ErrorCode.MISSING_FIELD); // "Validation failed: \n" + errorMessages
		}

		return request;
	}

	public <T> byte[] convertToResponseAsBytes(T obj) {

		try {
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
