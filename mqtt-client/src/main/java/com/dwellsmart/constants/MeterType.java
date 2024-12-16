package com.dwellsmart.constants;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum MeterType {
	SUNSTAR_DS(1, "Sun Star DwellSMART V9"),
	ENERTRAK(2, "Sun Star Enertrak V5"),
	SUNSTAR_DS_PP(9, "Sun Star DwellSMART V9 Password Protected"), 
	UNKNOWN(-1,"Unknown meter for this client");

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
	public static MeterType fromId(int id) {  // Method to find MeterType by ID
		for (MeterType type : MeterType.values()) {
			if (type.id == id) {
				return type;
			}
		}
		log.warn("Invalid MeterType ID received: {}", id);
	    return MeterType.UNKNOWN; 
	}
}