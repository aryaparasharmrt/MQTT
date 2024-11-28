package com.dwellsmart.dto;

import java.util.List;

import com.dwellsmart.constants.OperationType;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeterOperationPayload {

	@NotBlank
	private String messageId;

	@NotNull
	private OperationType opType;

//	@Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9][0-9]?):\\d+$")

	@NotBlank
	@Pattern(regexp = "^((25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?):([1-9][0-9]{0,4})$")
	private String ipAddress;

	private boolean ipStatus;

	@NotNull
	@Size(min = 1)
	@Valid
	private List<MeterInfo> meters;
}
