package com.dwellsmart.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dwellsmart.constants.OperationCode;
import com.dwellsmart.dto.MeterData;
import com.dwellsmart.dto.MeterInfo;
import com.dwellsmart.dto.MeterOperationPayload;
import com.dwellsmart.factory.MeterFactory;
import com.dwellsmart.modbus.IMeter;

import net.wimpi.modbus.net.RTUTCPMasterConnection;

@Service
public class MeterOperationService {

	@Autowired
	private ModbusService modbusService;

	@Autowired
	private MeterFactory meterFactory;

	private void processMeter(MeterInfo meter, OperationCode opCode) {
		// Your processing logic here...
		System.out.println("Processing meter " + meter.getMeterId() + " with operation: " + opCode);
	}

	public MeterOperationPayload processOperation(MeterOperationPayload request) {
		RTUTCPMasterConnection connection = new RTUTCPMasterConnection();
		OperationCode operationCode = request.getOpCode();
		try {
			connection = modbusService.getConnectionToModbusServer(request.getIpAddress());

			for (MeterInfo meter : request.getMeters()) {

				switch (operationCode) {
				case W_LOAD:
				case W_PP:
					// Validate that the data object is present and `ebLoad` is not null
					if (meter.getData() == null || meter.getData().getEbLoad() == null) {
						throw new IllegalArgumentException("Data is required for operation: " + operationCode);
					}
					break;

				case CONNECT:
				case DISCONNECT:
					// Validate that the data object should not be present
					if (meter.getData() != null) {
						throw new IllegalArgumentException(
								"Data should not be present for operation: " + operationCode);
					}

					MeterData data = meter.getData();
					if (data != null) {

						this.handleConnectOperation(meter, connection);
					} else {
						MeterData dataObject = new MeterData();
						meter.setData(dataObject);
						this.handleConnectOperation(meter, connection);
					}

					break;

				case READ:
					this.handleReadOperation(meter, connection);
					break;

				default:
					throw new UnsupportedOperationException("Unsupported operation: " + operationCode);
				}

				// Process the meter object
				processMeter(meter, operationCode);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (connection.isConnected()) {
				connection.close();
			}

		}
		return request;

	}

	private void handleReadOperation(MeterInfo info, RTUTCPMasterConnection connection) {
		System.out.println("Performing READ operation on meters...");
		System.out.println("Reading data from Meter: " + info.getMetersTypeId() + ", Meter ID: " + info.getMeterId());

		IMeter meter = meterFactory.getMeter(info.getMetersTypeId(), info.getMeterId(), connection);
		info.setData(meter.readMeter());
	}

	private void handleConnectOperation(MeterInfo info, RTUTCPMasterConnection connection) {
		System.out.println("Performing CONNECT operation on meters...");
		System.out.println("Connecting Meter: " + info.getMetersTypeId() + ", Meter ID: " + info.getMeterId());

		IMeter meter = meterFactory.getMeter(info.getMetersTypeId(), info.getMeterId(), connection);
		info.getData().setStatus(meter.connect());

	}

//	private void handleDisconnectOperation(MeterOperationPayload request, RTUTCPMasterConnection connection) {
//		System.out.println("Performing DISCONNECT operation on meters...");
//		for (MeterInfo meterRequest : request.getMeterRequests()) {
//			System.out.println("Disconnecting Meter: " + meterRequest.getMeterTypeId() + ", Slave ID: "
//					+ meterRequest.getSlaveId());
//			
//			IMeter meter = meterFactory.getMeter(meterRequest.getMeterTypeId(), meterRequest.getSlaveId(), connection);
//			boolean connect = meter.disconnect();
//			// Add your disconnect logic here
//		}
//	}
}
