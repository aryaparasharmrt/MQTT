package com.dwellsmart.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

public enum MeterType {
	SUNSTAR_DS(1, "Sun Star DwellSMART V9"),
	ENERTRAK(2, "Sun Star Enertrak V5"),
	SUNSTAR_DS_PP(9, "Sun Star DwellSMART V9 Password Protected");

	@Getter
	@JsonValue
	private final int id;

	@Getter
	private final String name;

	MeterType(int id, String name) {
		this.id = id;
		this.name = name;
	}

	@JsonCreator
	public static MeterType fromId(int id) {
		for (MeterType type : MeterType.values()) {
			if (type.id == id) {
				return type;
			}
		}
		throw new IllegalArgumentException("Invalid meterTypeId: " + id);
	}

//	// Method to find MeterType by ID
//	public static MeterType getById(int id) {
//		for (MeterType type : MeterType.values()) {
//			if (type.getId() == id) {
//				return type;
//			}
//		}
//		throw new IllegalArgumentException("Invalid meterTypeId: " + id);
//	}
}