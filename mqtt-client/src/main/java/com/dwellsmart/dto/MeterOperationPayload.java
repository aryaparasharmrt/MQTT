package com.dwellsmart.dto;

import java.util.List;

import com.dwellsmart.constants.OperationCode;

import lombok.Data;

@Data
public class MeterOperationPayload {

	private String messageId;
	private OperationCode opCode;
	private String ipAddress;
	private List<MeterInfo> meters;
}
