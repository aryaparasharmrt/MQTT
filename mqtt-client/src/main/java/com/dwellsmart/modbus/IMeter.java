package com.dwellsmart.modbus;

import org.springframework.validation.annotation.Validated;

import com.dwellsmart.dto.MeterData;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Validated
public interface IMeter {

	boolean connect();

	boolean disconnect();

	MeterData readMeter();

	boolean setUnitId(@NotNull @Min(0) @Max(255) Short unitId);

	boolean setLoad(Double ebLoad, Double dgLoad);

}
