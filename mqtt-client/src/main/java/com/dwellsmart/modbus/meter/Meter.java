package com.dwellsmart.modbus.meter;

import com.dwellsmart.modbus.IMeter;

import net.wimpi.modbus.net.RTUTCPMasterConnection;

public abstract class Meter implements IMeter {

	protected final short slaveId;
	protected final RTUTCPMasterConnection connection; 

	public Meter(short slaveId, RTUTCPMasterConnection connection) {
		this.slaveId = slaveId;
		this.connection = connection;
	}

}
