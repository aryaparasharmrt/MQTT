package com.dwellsmart.dto;

import com.dwellsmart.constants.MeterType;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MeterInfo {

	@NotNull
	@Min(value = 0)
	@Max(value = 255)
	private Short meterId;

	@NotNull
	private MeterType meterTypeId;

	private MeterData data;

}
