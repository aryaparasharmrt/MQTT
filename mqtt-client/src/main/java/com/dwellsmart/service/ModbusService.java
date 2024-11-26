package com.dwellsmart.service;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Service;

import static com.dwellsmart.constants.MQTTConstants.*;

import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusRTUTCPTransaction;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteMultipleRegistersRequest;
import net.wimpi.modbus.msg.WriteMultipleRegistersResponse;
import net.wimpi.modbus.net.RTUTCPMasterConnection;
import net.wimpi.modbus.procimg.Register;

@Service
public class ModbusService {


	public ReadInputRegistersResponse readInputRegistersRequest(short slaveId, int ref, int count,
			RTUTCPMasterConnection masterConnection) {
		if (ref < 0 || count <= 0 || slaveId < 0 || slaveId > 256) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		int attempts = 0;

		while (attempts < MAX_RETRIES) {
			try {
				// Create and configure request
				ReadInputRegistersRequest request = new ReadInputRegistersRequest(ref, count);
				request.setUnitID(slaveId);

				// Create transaction
				ModbusRTUTCPTransaction transaction = new ModbusRTUTCPTransaction(masterConnection);
				transaction.setRequest(request);
				transaction.execute();

				// Handle response
				ReadInputRegistersResponse response = (ReadInputRegistersResponse) transaction.getResponse();

				if (response != null && response.getFunctionCode() == request.getFunctionCode()
						&& response.getUnitID() == request.getUnitID()) {
					return response; // Successful response
				} else {
//	                logWarning("Invalid response received. Attempt: " + (attempts + 1));
				}

			} catch (ModbusException e) {
//                Logger.getLogger(ModbusService.class.getName()).log(Level.SEVERE, "Error in readRegisters attempt " + (attempts + 1), e);
			} catch (Exception e) {
//                Logger.getLogger(MeterMapService.class.getName()).log(Level.SEVERE, "Error in readRegisters attempt " + (attempts + 1), e);
			}

			// Increment retry count and add delay
			attempts++;

			try {
				Thread.sleep(100 * (attempts + 1));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // Restore interrupted state
				break;
			}
		}

//        Logger.getLogger(ModbusService.class.getName()).log(Level.SEVERE, "Max retries reached for readRegisters");
		return null; // Return null if all attempts fail
	}

	public ReadMultipleRegistersResponse readMultipleRegistersRequest( short slaveId,int ref, int count,
			RTUTCPMasterConnection masterConnection) {
		if (ref < 0 || count <= 0 || slaveId < 0 || slaveId > 256) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		int attempts = 0;

		while (attempts < MAX_RETRIES) {
			try {
				// Create and configure request
				ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(ref, count);
				request.setUnitID(slaveId);

				// Create transaction
				ModbusRTUTCPTransaction transaction = new ModbusRTUTCPTransaction(masterConnection);
				transaction.setRequest(request);
				transaction.execute();

				// Handle response
				ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();

				if (response != null && response.getFunctionCode() == request.getFunctionCode()
						&& response.getUnitID() == request.getUnitID()) {
					return response; // Successful response
				} else {
//	                logWarning("Invalid response received. Attempt: " + (attempts + 1));
				}

			} catch (ModbusException e) {
//                Logger.getLogger(ModbusService.class.getName()).log(Level.SEVERE, "Error in readRegisters attempt " + (attempts + 1), e);
			} catch (Exception e) {
//                Logger.getLogger(MeterMapService.class.getName()).log(Level.SEVERE, "Error in readRegisters attempt " + (attempts + 1), e);
			}

			// Increment retry count and add delay
			attempts++;

			try {
				Thread.sleep(100 * (attempts + 1));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // Restore interrupted state
				break;
			}
		}

//        Logger.getLogger(ModbusService.class.getName()).log(Level.SEVERE, "Max retries reached for readRegisters");
		return null; // Return null if all attempts fail
	}

	public boolean writeMultipleRegistersRequest( short slaveId,int registerRef, Register[] registers,
			RTUTCPMasterConnection masterConnection) {
		if (slaveId < 0 || slaveId > 256) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		int attempts = 0;

		while (attempts < MAX_RETRIES) {
			try {
				// Create and configure request
				WriteMultipleRegistersRequest request = new WriteMultipleRegistersRequest(registerRef, registers);
				request.setUnitID(slaveId);

				// Create transaction
				ModbusRTUTCPTransaction transaction = new ModbusRTUTCPTransaction(masterConnection);
				transaction.setRequest(request);
				transaction.execute();

				// Handle response
				WriteMultipleRegistersResponse response = (WriteMultipleRegistersResponse) transaction.getResponse();

				if (response != null && response.getFunctionCode() == request.getFunctionCode()
						&& response.getUnitID() == request.getUnitID()) {
					return true; // Successful response
				} else {
//	                logWarning("Invalid response received. Attempt: " + (attempts + 1));
				}

			} catch (ModbusException e) {
//                Logger.getLogger(ModbusService.class.getName()).log(Level.SEVERE, "Error in readRegisters attempt " + (attempts + 1), e);
			} catch (Exception e) {
//                Logger.getLogger(MeterMapService.class.getName()).log(Level.SEVERE, "Error in readRegisters attempt " + (attempts + 1), e);
			}

			// Increment retry count and add delay
			attempts++;

			try {
				Thread.sleep(100 * (attempts + 1));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // Restore interrupted state
				break;
			}
		}

//        Logger.getLogger(ModbusService.class.getName()).log(Level.SEVERE, "Max retries reached for readRegisters");
		return false;
	}

	public RTUTCPMasterConnection getConnectionToModbusServer(String conAddress)
			throws UnknownHostException, NumberFormatException, IOException, Exception {
//	        logger.log(Level.INFO, "Method Entry getConnectionToModbusServer at ..." + System.currentTimeMillis());
		RTUTCPMasterConnection con = new RTUTCPMasterConnection();
		int port;
		String inetAddress = conAddress;
		int idx = inetAddress.indexOf(':');
		if (idx > 0) {
			port = Integer.parseInt(inetAddress.substring(idx + 1));
			inetAddress = inetAddress.substring(0, idx);
			InetAddress addr = InetAddress.getByName(inetAddress);
			// Open the connection
			con = new RTUTCPMasterConnection(addr, port);
			con.connect();
			System.out.println("Connection established...");
		}
//	        else {
//	            serverSocket = singletonSocketServer.getServerSocketforPort(conAddress);
////	            startServer(Integer.parseInt(conAddress));
//	            Thread.sleep(1000);
//	            con.getConnection(serverSocket);
//	        }
//	        logger.log(Level.INFO, "Method Exit getConnectionToModbusServer at ..." + System.currentTimeMillis());
		return con;
	}

}
