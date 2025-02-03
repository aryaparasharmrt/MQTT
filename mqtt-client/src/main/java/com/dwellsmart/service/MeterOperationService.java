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
import com.dwellsmart.exception.ModbusConnectionException;
import com.dwellsmart.factory.MeterFactory;
import com.dwellsmart.modbus.IMeter;

import lombok.extern.slf4j.Slf4j;
import net.wimpi.modbus.net.RTUTCPMasterConnection;

@Service
@Slf4j
public class MeterOperationService {

	@Autowired
	private ModbusService modbusService;

	@Autowired
	private MeterFactory meterFactory;

	public void processOperation(MeterOperationPayload request) {
		RTUTCPMasterConnection connection = new RTUTCPMasterConnection();
		StringBuilder messageResponse = new StringBuilder();
		OperationType operationType = request.getOpType();

		StringBuilder logMessage = new StringBuilder();
		logMessage.append(String.format("Message ID: %s | Operation Type: %s | IP Address: %s | ",
				request.getMessageId(), request.getOpType(), request.getIpAddress()));

		boolean isSuccess = false;
		try {
			connection = modbusService.getConnectionToModbusServer(request.getIpAddress());
			request.setIpStatus(connection != null && connection.isConnected());
			logMessage.append(String.format("IP Status: %b | Meters: [ ", request.isIpStatus()));

			List<MeterInfo> meterInfos = request.getMeters();
			for (MeterInfo meterInfo : meterInfos) {
				logMessage.append(String.format("{ Meter ID: %d | Meter Type ID: %d | ", meterInfo.getMeterId(),
						meterInfo.getMeterTypeId().getId()));

				MeterData meterData = meterInfo.getData();
				IMeter meter = meterFactory.getMeter(meterInfo.getMeterTypeId(), meterInfo.getMeterId(), connection);

				if (meter == null || meterInfo.getMeterId() > 255 || meterInfo.getMeterId() < 0) {
					meterInfo.setData(MeterData.builder().status(false).build());
					
					messageResponse.append(String.format("{ Meter ID: %d | Meter Type ID: %d | Status: %b | Reason: %s } ", meterInfo.getMeterId(),
							meterInfo.getMeterTypeId().getId(), meterInfo.getData().isStatus(),
							(meter == null) ? "Meter not found" : "Meter ID out of range"));
					
					logMessage.append(String.format("Status: %b | Reason: %s } ", meterInfo.getData().isStatus(),
							(meter == null) ? "Meter not found" : "Meter ID out of range"));
					
					request.setMessage(messageResponse.toString());
					continue;
				}
				
				switch (operationType) {
				case W_PP:

					break;
				case W_LOAD:
//					if (meterInfos.size() > 1) {
//						throw new ApplicationException("At a time only one meter allow to process this operation");
//					} //we wil think later for this operation
					// Validate that the data object is present and `ebLoad` is not null
					if (meterData == null || meterData.getEbLoad() == null) {
						throw new ApplicationException("Data is required for operation: " + operationType);
					}

					isSuccess = meter.setLoad(meterData.getEbLoad(), meterData.getDgLoad());
					meterData.setStatus(isSuccess);
					logMessage.append(String.format("Status: %b } ", isSuccess));

					break;

				case W_ID:
					if (meterInfo.getData() == null || meterInfo.getData().getMeterId() == null) {
						throw new ApplicationException("Data is required for operation: " + operationType);
					}
					isSuccess = meter.setUnitId(meterInfo.getData().getMeterId());
					meterData.setStatus(isSuccess);
					logMessage.append(String.format("Status: %b } ", isSuccess));

					break;

				case DISCONNECT:
//					if(meterInfos.size()>1) {
//						throw new ApplicationException("At a time only one meter allow to process this operation");
//					}  //we wil think later for this operation
					if (meterData != null) {
						meterData.setStatus(meter.disconnect());
					} else {
						meterInfo.setData(MeterData.builder().status(meter.disconnect()).build());
					}
					logMessage.append(String.format("Status: %b } ", meterInfo.getData().isStatus()));
					break;

				case CONNECT:
					if (meterData != null) {
						meterData.setStatus(meter.connect());
					} else {
						meterInfo.setData(MeterData.builder().status(meter.connect()).build());
					}
					logMessage.append(String.format("Status: %b } ", meterInfo.getData().isStatus()));
					break;

				case READ:
					MeterData data = Optional.ofNullable(meter.readMeter())
							.orElse(MeterData.builder().status(false).build());
					meterInfo.setData(data);
					logMessage.append(String.format("Status: %b } ", meterInfo.getData().isStatus()));
					break;

				default:
					throw new ApplicationException("Unsupported operation: " + operationType);
				}
			}
			logMessage.append("]");

		} catch (ModbusConnectionException e) { // For Ipstatus false
			// Return Response in dynamic topic(For not getting connection, And more...)
			// //We will cover more cases
			request.setMessage(messageResponse.append(e.getMessage()).toString());
			request.setMeters(null); 
			logMessage.append(String.format("IP Status: %b ", request.isIpStatus()));
		} catch (ApplicationException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Unable to process operation: " + e.getMessage());
		} finally {
			if (connection != null && connection.isConnected()) {
				connection.close();
			}
		}

		log.info(logMessage.toString());
	}

}
