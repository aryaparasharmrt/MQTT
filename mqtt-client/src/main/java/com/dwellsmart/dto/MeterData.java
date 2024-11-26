package com.dwellsmart.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeterData {

	private boolean status;

	private Double ebLoad;
	private Double dgLoad;
	private Double gridReading;
	private Double dgReading;

	private String serialNo;
	private String relayStatus;
	private String firmwareVersion;
	private String opMode;

	private Double pFactorR;
	private Double pFactorY;
	private Double pFactorB;

	private Double vPhaseR;
	private Double vPhaseY;
	private Double vPhaseB;

	private Double cPhaseR;
	private Double cPhaseY;
	private Double cPhaseB;

	private Double kWPhaseR;
	private Double kWPhaseY;
	private Double kWPhaseB;

	private Double kvaPhaseR;
	private Double kvaPhaseY;
	private Double kvaPhaseB;

	private Boolean ebRelayR;
	private Boolean ebRelayY;
	private Boolean ebRelayB;

	private Boolean dgRelayR;
	private Boolean dgRelayY;
	private Boolean dgRelayB;

	private Double powerKW;
	private Double powerKva;
	private Double powerFactor;

	private Double frequency;
	private String energySource;

	private String readingDateTime;

}
