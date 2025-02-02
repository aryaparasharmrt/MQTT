package com.dwellsmart.service;

import static com.dwellsmart.constants.Constants.MODBUS_MAX_RETRIES;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.stereotype.Service;

import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.exception.ModbusConnectionException;

import lombok.extern.slf4j.Slf4j;
import net.wimpi.modbus.ModbusException;
import net.wimpi.modbus.io.ModbusRTUTCPTransaction;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteMultipleRegistersRequest;
import net.wimpi.modbus.msg.WriteMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterResponse;
import net.wimpi.modbus.net.RTUTCPMasterConnection;
import net.wimpi.modbus.procimg.Register;

@Service
@Slf4j
public class ModbusService {

	public ReadInputRegistersResponse readInputRegistersRequest(short slaveId, int ref, int count,
			RTUTCPMasterConnection masterConnection) {
		if (ref < 0 || count <= 0 || slaveId < 0 || slaveId > 256) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		int attempts = 0;

		while (attempts < MODBUS_MAX_RETRIES) {
			try {
				ReadInputRegistersRequest request = new ReadInputRegistersRequest(ref, count);
				request.setUnitID(slaveId);

				// Create transaction
				ModbusRTUTCPTransaction transaction = new ModbusRTUTCPTransaction(masterConnection);
				transaction.setRequest(request);
				transaction.execute(); // This will throw an exception

				// Handle response
				ReadInputRegistersResponse response = (ReadInputRegistersResponse) transaction.getResponse();

				if (response != null && response.getFunctionCode() == request.getFunctionCode()
						&& response.getUnitID() == request.getUnitID()) {
					return response; // Successful response
				} else {
					log.warn("Invalid response received. Attempt: " + (attempts + 1));
				}

			} catch (ModbusException e) {
				log.error("Error in readInputRegistersRequest attempt ModbusException: " + (attempts + 1), e);
			} catch (Exception e) {
				log.error("Error in readInputRegistersRequest attempt Exception: " + (attempts + 1), e);
			}

			attempts++;

			try {
				Thread.sleep(200 * (attempts + 1));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // Restore interrupted state
				break;
			}
		}

		log.warn("Max retries reached for readInputRegistersRequest");
		return null;
	}

	public ReadMultipleRegistersResponse readMultipleRegistersRequest(short slaveId, int ref, int count,
			RTUTCPMasterConnection masterConnection) {
		if (ref < 0 || count <= 0 || slaveId < 0 || slaveId > 256) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		int attempts = 0;

		while (attempts < MODBUS_MAX_RETRIES) {
			try {
				ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(ref, count);
				request.setUnitID(slaveId);

				// Create transaction
				ModbusRTUTCPTransaction transaction = new ModbusRTUTCPTransaction(masterConnection);
				transaction.setRequest(request);
				transaction.execute(); // This will throw an exception

				// Handle response
				ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();

				if (response != null && response.getFunctionCode() == request.getFunctionCode()
						&& response.getUnitID() == request.getUnitID()) {
					return response; // Successful response
				} else {
					log.warn("Invalid response received. Attempt: " + (attempts + 1));
				}

			} catch (ModbusException e) {
				log.error("Error in readMultipleRegistersRequest attempt (ModbusException): " + (attempts + 1), e);
			} catch (Exception e) {
				log.error("Error in readMultipleRegistersRequest attempt (Exception): " + (attempts + 1), e);
			}

			attempts++;

			try {
				Thread.sleep(200 * (attempts + 1));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // Restore interrupted state
				break;
			}
		}

		log.warn("Max retries reached for readMultipleRegistersRequest");
		return null;
	}

	public boolean writeMultipleRegistersRequest(short slaveId, int registerRef,
			RTUTCPMasterConnection masterConnection, Register... registers) {
		if (slaveId < 0 || slaveId > 256) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		int attempts = 0;

		while (attempts < MODBUS_MAX_RETRIES) {
			try {
				WriteMultipleRegistersRequest request = new WriteMultipleRegistersRequest(registerRef, registers);
				request.setUnitID(slaveId);

				// Create transaction
				ModbusRTUTCPTransaction transaction = new ModbusRTUTCPTransaction(masterConnection);
				transaction.setRequest(request);
				transaction.execute(); // This will throw an exception

				// Handle response
				WriteMultipleRegistersResponse response = (WriteMultipleRegistersResponse) transaction.getResponse();

				if (response != null && response.getFunctionCode() == request.getFunctionCode()
						&& response.getUnitID() == request.getUnitID()) {
					return true; // Successful response
				} else {
					log.warn("Invalid response received. Attempt: " + (attempts + 1));
				}
			} catch (ModbusException e) {
				log.error("Error in writeMultipleRegistersRequest attempt (ModbusException): " + (attempts + 1), e);
			} catch (Exception e) {
				log.error("Error in writeMultipleRegistersRequest attempt (Exception): " + (attempts + 1), e);
			}

			attempts++;

			try {
				Thread.sleep(200 * (attempts + 1));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // Restore interrupted state
				break;
			}
		}

		log.warn("Max retries reached for writeMultipleRegistersRequest");
		return false;
	}
	
	public boolean writeSingleRegistersRequest(short slaveId, int registerRef,
			RTUTCPMasterConnection masterConnection, Register register) {
		if (slaveId < 0 || slaveId > 256) {
			throw new IllegalArgumentException("Invalid input parameters");
		}
		int attempts = 0;

		while (attempts < MODBUS_MAX_RETRIES) {
			try {
				WriteSingleRegisterRequest request = new WriteSingleRegisterRequest(registerRef, register);
				request.setUnitID(slaveId);

				// Create transaction
				ModbusRTUTCPTransaction transaction = new ModbusRTUTCPTransaction(masterConnection);
				transaction.setRequest(request);
				transaction.execute(); // This will throw an exception

				// Handle response
				WriteSingleRegisterResponse response = (WriteSingleRegisterResponse) transaction.getResponse();

				if (response != null && response.getFunctionCode() == request.getFunctionCode()
						&& response.getUnitID() == request.getUnitID()) {
					return true; // Successful response
				} else {
					log.warn("Invalid response received. Attempt: " + (attempts + 1));
				}
			} catch (ModbusException e) {
				log.error("Error in writeMultipleRegistersRequest attempt (ModbusException): " + (attempts + 1), e);
			} catch (Exception e) {
				log.error("Error in writeMultipleRegistersRequest attempt (Exception): " + (attempts + 1), e);
			}

			attempts++;

			try {
				Thread.sleep(200 * (attempts + 1));
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt(); // Restore interrupted state
				break;
			}
		}

		log.warn("Max retries reached for writeMultipleRegistersRequest");
		return false;
	}

	public RTUTCPMasterConnection getConnectionToModbusServer(String conAddress) throws ModbusConnectionException {
		if (conAddress == null || !conAddress.contains(":")) {
			throw new ApplicationException("Connection address must be in the format IP:PORT");
		}

		int idx = conAddress.indexOf(':');
		String inetAddress = conAddress.substring(0, idx);
		int port = Integer.parseInt(conAddress.substring(idx + 1));

		if (port < 1 || port > 65535) {
			throw new ApplicationException("Port number must be between 1 and 65535");
		}

		RTUTCPMasterConnection con = null;
		try {
			InetAddress addr = InetAddress.getByName(inetAddress);
			con = new RTUTCPMasterConnection(addr, port);
//			con.setTimeout(20);
			con.connect();
			log.info("Connection established to " + inetAddress + " on port " + port);
			return con;
		} catch (UnknownHostException e) {
			log.warn("Invalid IP address: " + inetAddress);
			throw new ModbusConnectionException("Invalid IP address: " + inetAddress, e);
		} catch (IOException e) {
			log.warn("Unable to connect to " + inetAddress + ":" + port);
			throw new ModbusConnectionException("Unable to connect to " + inetAddress + ":" + port, e);
		} catch (Exception e) {
			log.warn("Something went wrong to connect to " + inetAddress + ":" + port);
			e.printStackTrace();
			throw new ModbusConnectionException("Something went wrong to connect to " + inetAddress + ":" + port, e);
		}
	}

}
