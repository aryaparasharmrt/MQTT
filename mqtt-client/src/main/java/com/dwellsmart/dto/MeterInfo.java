package com.dwellsmart.dto;

import com.dwellsmart.constants.MeterType;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MeterInfo {

	@NotNull
	private Short meterId;

	@NotNull
	private MeterType meterTypeId;

	private MeterData data;

}
