package com.dwellsmart.modbus.meter;

import org.springframework.beans.factory.annotation.Autowired;

import com.dwellsmart.modbus.IMeter;
import com.dwellsmart.pojo.MeterAddressMap;
import com.dwellsmart.service.ModbusService;

import net.wimpi.modbus.net.RTUTCPMasterConnection;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;

public abstract class SunStar implements IMeter {

	protected final short meterId;
	protected final MeterAddressMap addressMap;
	protected final RTUTCPMasterConnection connection;

	@Autowired
	protected ModbusService modbusService;

	public SunStar(short meterId, MeterAddressMap addressMap, RTUTCPMasterConnection connection) {
		this.meterId = meterId;
		this.addressMap = addressMap;
		this.connection = connection;
	}

	@Override
	public boolean connect() {
		return modbusService.writeMultipleRegistersRequest(meterId, addressMap.getConnectRegisterAddress(),
				new Register[] { new SimpleRegister(addressMap.getConnectRegisterValue()) }, connection);
	}

	@Override
	public boolean disconnect() {
		return modbusService.writeMultipleRegistersRequest(meterId, addressMap.getDisconnectRegisterAddress(),
				new Register[] { new SimpleRegister(addressMap.getDisconnectRegisterValue()) }, connection);
	}

}