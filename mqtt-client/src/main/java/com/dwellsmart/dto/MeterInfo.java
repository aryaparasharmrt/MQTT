package com.dwellsmart.dto;

import com.dwellsmart.constants.MeterType;

import lombok.Data;

@Data
public class MeterInfo {

	private short meterId;
	private MeterType metersTypeId;
	private MeterData data;

}
