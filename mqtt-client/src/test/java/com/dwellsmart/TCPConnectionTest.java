package com.dwellsmart;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.exception.ModbusConnectionException;

import net.wimpi.modbus.io.ModbusRTUTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.RTUTCPMasterConnection;

public class TCPConnectionTest {
	
	public static RTUTCPMasterConnection getConnectionToModbusServer(String conAddress) throws ModbusConnectionException{
		 long timeMillis = System.currentTimeMillis();
		 System.out.println("Request Started...."+timeMillis);
		
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
	        con.connect();
//	        log.info("Connection established to " + inetAddress + " on port " + port);
	        System.out.println("Connected to IP: "+ inetAddress + " On Port: "+port+" Time Taken " + (System.currentTimeMillis()- timeMillis));;
	        return con;
	    } catch (UnknownHostException e) {
//	        log.error("Invalid IP address: " + inetAddress);
	    	System.out.println("Time Taken Not Connected to IP: "+ inetAddress + ": Time Taken " + (System.currentTimeMillis()- timeMillis)+ " Exception = UnknownHostException");
	        throw new ModbusConnectionException("Invalid IP address: " + inetAddress, e);
	    } catch (IOException e) {
//	        log.error("Unable to connect to " + inetAddress + ":" + port);
	    	System.out.println("Time Taken Not Connected to IP: "+ inetAddress + ": Time Taken " + (System.currentTimeMillis() - timeMillis)+ " Exception = IOException");
	        throw new ModbusConnectionException("Unable to connect to " + inetAddress + ":" + port, e);
		} catch (Exception e) {
//			log.error("Something went wrong to connect to " + inetAddress + ":" + port);
			System.out.println("Time Taken Not Connected to IP: "+ inetAddress + ": Time Taken " + (System.currentTimeMillis() - timeMillis)+ " Exception");
			e.printStackTrace();
			throw new ModbusConnectionException("Something went wrong to connect to " + inetAddress + ":" + port, e);
		}
	}

	
	public static void main(String[] args) {
		String ipAddress = "192.168.0.100:100";  //Unkonwn host time taken less than 1 sec and connection time taken less than 1 sec
//		String ipAddress = "150.129.250.99:21101";  //Around ~22 seconds
//		String ipAddress = "213.233.223.25:234";
//		String ipAddress = "192.168.0.100:100";
		
//		RTUTCPMasterConnection connection = getConnectionToModbusServer(ipAddress);
		String inetAddress = "192.168.0.100";
		InetAddress addr;
		try {
			 long timeMillis = System.currentTimeMillis();
			System.out.println("Starting ..."+ timeMillis);
			addr = InetAddress.getByName(inetAddress);
			RTUTCPMasterConnection connection = new RTUTCPMasterConnection(addr,100);
			connection.connect();
			System.out.println("After Connection: "+ (System.currentTimeMillis() - timeMillis));
			
			ReadMultipleRegistersRequest request = new ReadMultipleRegistersRequest(0, 10);
			request.setUnitID(21);

			// Create transaction
			ModbusRTUTCPTransaction transaction = new ModbusRTUTCPTransaction(connection);
			transaction.setRequest(request);
			transaction.execute();
			
			System.out.println("After Transaction: "+ (System.currentTimeMillis() - timeMillis));

			// Handle response
			ReadMultipleRegistersResponse response = (ReadMultipleRegistersResponse) transaction.getResponse();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Connect hone ke bad yadi meter nhi milta to around ~15- 20 seconds le rha hai
		
	}

}
