package com.dwellsmart.service;

import java.util.List;
import java.util.Optional;

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
		OperationType operationType = request.getOpType();
		boolean isSuccess = false;
		try {
			connection = modbusService.getConnectionToModbusServer(request.getIpAddress());
			request.setIpStatus(connection != null && connection.isConnected());
	        if (!connection.isConnected()) {
	            request.setMessage("Connection to server failed");
	            request.setMeters(null);
	            return;
	        }

			List<MeterInfo> meterInfos = request.getMeters();
			for (MeterInfo meterInfo : meterInfos) {
				MeterData meterData = meterInfo.getData();
				IMeter meter = meterFactory.getMeter(meterInfo.getMetersTypeId(), meterInfo.getMeterId(), connection);

				if (meter == null) {
					meterInfo.setData(MeterData.builder().status(false).build());
					continue;
				}
				switch (operationType) {
				case W_PP:

					break;
				case W_LOAD:
					if (meterInfos.size() > 1) {
						throw new ApplicationException("At a time only one meter allow to process this operation");
					} //we wil think later for this operation
					// Validate that the data object is present and `ebLoad` is not null
					if (meterData == null || meterData.getEbLoad() == null) {
						throw new ApplicationException("Data is required for operation: " + operationType);
					}

					isSuccess = meter.setLoad(meterData.getEbLoad(), meterData.getDgLoad());
					meterData.setStatus(isSuccess);

					break;

				case W_ID:
					if (meterInfo.getData() == null || meterInfo.getData().getMeterId() == null) {
						throw new ApplicationException("Data is required for operation: " + operationType);
					}
					isSuccess = meter.setUnitId(meterInfo.getData().getMeterId());
					meterData.setStatus(isSuccess);

					break;

				case DISCONNECT:
					if(meterInfos.size()>1) {
						throw new ApplicationException("At a time only one meter allow to process this operation");
					}  //we wil think later for this operation
					if (meterData != null) {
						meterData.setStatus(meter.disconnect());
					} else {
						meterInfo.setData(MeterData.builder().status(meter.disconnect()).build());
					}
					break;

				case CONNECT:
					if (meterData != null) {
						meterData.setStatus(meter.connect());
					} else {
						meterInfo.setData(MeterData.builder().status(meter.connect()).build());
					}
					break;

				case READ:
					MeterData data = Optional.ofNullable(meter.readMeter())
							.orElse(MeterData.builder().status(false).build());
					meterInfo.setData(data);
					break;

				default:
					throw new ApplicationException("Unsupported operation: " + operationType);
				}

			}
		} catch (ApplicationException e) {
			throw e;
		} catch (Exception e) {
//			request.setMessage(e.getMessage());
//			e.printStackTrace();
			throw new ApplicationException("Unable to process operation: " + e.getMessage());
		} finally {
			if (connection != null && connection.isConnected()) {
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
