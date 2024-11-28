package com.dwellsmart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwellsmart.constants.OperationType;
import com.dwellsmart.dto.MeterData;
import com.dwellsmart.dto.MeterInfo;
import com.dwellsmart.dto.MeterOperationPayload;
import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.factory.MeterFactory;
import com.dwellsmart.modbus.IMeter;

import net.wimpi.modbus.net.RTUTCPMasterConnection;

@Service
public class MeterOperationService {

	@Autowired
	private ModbusService modbusService;

	@Autowired
	private MeterFactory meterFactory;

	public void processOperation(MeterOperationPayload request) {
		RTUTCPMasterConnection connection = new RTUTCPMasterConnection();
		OperationType operationCode = request.getOpType();
		try {
//			connection = modbusService.getConnectionToModbusServer(request.getIpAddress());
			request.setIpStatus(connection.isConnected());
			

			for (MeterInfo meterInfo : request.getMeters()) {
				MeterData data = meterInfo.getData();
				IMeter meter = meterFactory.getMeter(meterInfo.getMetersTypeId(), meterInfo.getMeterId(), connection);

				switch (operationCode) {
				case W_LOAD:
				case W_PP:
					// Validate that the data object is present and `ebLoad` is not null
					if (meterInfo.getData() == null || meterInfo.getData().getEbLoad() == null) {
						throw new ApplicationException("Data is required for operation: " + operationCode);
					}
					break;

				case W_ID:
					if (meterInfo.getData() == null || meterInfo.getData().getMeterId() == null) {
						throw new ApplicationException("Data is required for operation: " + operationCode);
					}
					boolean isSuccess = meter.setUnitId(meterInfo.getData().getMeterId());
					meterInfo.getData().setStatus(isSuccess);

					break;

				case DISCONNECT:
					if (data != null) {
						meterInfo.getData().setStatus(meter.disconnect());
					} else {
						meterInfo.setData(MeterData.builder().status(meter.disconnect()).build());
					}
					break;

				case CONNECT:
					if (data != null) {
						meterInfo.getData().setStatus(meter.connect());
					} else {
						meterInfo.setData(MeterData.builder().status(meter.connect()).build());
					}
					break;

				case READ:
					meterInfo.setData(meter.readMeter());
					break;

				default:
					throw new ApplicationException("Unsupported operation: " + operationCode);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Unable to process operation");
		} finally {
			if (connection.isConnected()) {
				connection.close();
			}

		}

	}

//	private void handleReadOperation(MeterInfo info, RTUTCPMasterConnection connection) {
//		System.out.println("Performing READ operation on meters...");
//		System.out.println("Reading data from Meter: " + info.getMetersTypeId() + ", Meter ID: " + info.getMeterId());
//		
//	}
//
//	private void handleConnectOperation(MeterInfo info, RTUTCPMasterConnection connection) {
//		System.out.println("Performing CONNECT operation on meters...");
//		System.out.println("Connecting Meter: " + info.getMetersTypeId() + ", Meter ID: " + info.getMeterId());
//
//	}

//	private void handleDisconnectOperation(MeterInfo info, RTUTCPMasterConnection connection) {
//		System.out.println("Performing DISCONNECT operation on meters...");
//		System.out.println("Disconnecting Meter: " + info.getMetersTypeId() + ", Meter ID: " + info.getMeterId());
//
//		
//	}
}
