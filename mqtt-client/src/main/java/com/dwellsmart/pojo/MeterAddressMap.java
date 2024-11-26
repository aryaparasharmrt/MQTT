package com.dwellsmart.pojo;

import com.dwellsmart.constants.MeterType;

import lombok.Data;

@Data
public class MeterAddressMap {

	private MeterType meterType;
	private int kwhRegisterAddress;
	private int kwhRegisterAddressesCount; //After dgKwhRegisterAddress
	private int connectRegisterAddress;
	private int connectRegisterValue;
	private int disconnectRegisterAddress;
	private int disconnectRegisterValue;
	private int dgKwhRegisterAddress;
	private int otherRegisterAddress;
	private int otherRegistersCount;
	private int loadRegisterAddress;
	private int overloadattemptsRegAddress;
	private int overloadattemptsdelayRegAddress;
	private boolean enableMsbFirst;
	private int passwordRegisterAddress;
	private int validatorRegisterAddress;
	private int ebdgRegisterAddress;
	private int ebValue;
	private int dgValue;

}
