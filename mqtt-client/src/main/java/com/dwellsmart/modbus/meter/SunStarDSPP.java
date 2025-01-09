package com.dwellsmart.modbus.meter;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.dwellsmart.dto.MeterData;
import com.dwellsmart.pojo.MeterAddressMap;

import lombok.extern.slf4j.Slf4j;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.RTUTCPMasterConnection;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;


/**
 * This class represents a Sunstar Meter which is manufacturer for DwellSMART pvt. ltd. 
 * the version of this meter is <b>9.5</b> and have password protected functionality.
 * 
 * 
 * @author Anshul Goyal
 */
@Slf4j
public class SunStarDSPP extends AbstractMeter {

	public SunStarDSPP(short meterId, MeterAddressMap addressMap, RTUTCPMasterConnection connection) {
		super(meterId, addressMap, connection);
	}

	@Override
	public MeterData readMeter() {

		MeterData meterReading = null;

		// Prepare the request
		ReadMultipleRegistersResponse res = modbusService.readMultipleRegistersRequest(meterId,
				addressMap.getKwhRegisterAddress(), addressMap.getKwhRegisterAddressesCount(), connection);
		try {
			if (null != res) {
				meterReading = new MeterData();

				ByteArrayOutputStream outputStreamEBLoad = new ByteArrayOutputStream();
				outputStreamEBLoad.write(res.getRegister(6).toBytes());
				meterReading.setEbLoad(new BigInteger(1, outputStreamEBLoad.toByteArray()).doubleValue() / 100);

				ByteArrayOutputStream outputStreamDGLoad = new ByteArrayOutputStream();
				outputStreamDGLoad.write(res.getRegister(8).toBytes());
				meterReading.setDgLoad(new BigInteger(1, outputStreamDGLoad.toByteArray()).doubleValue() / 100);

				ByteArrayOutputStream outputStreamMode = new ByteArrayOutputStream();
				outputStreamMode.write(res.getRegister(12).toBytes()[0]);
				meterReading.setOpMode(
						(new BigInteger(1, outputStreamMode.toByteArray()).intValue() == 0) ? "KWH" : "KVAH");

				ByteArrayOutputStream outputStreamEBRelays = new ByteArrayOutputStream();
				outputStreamEBRelays.write(res.getRegister(12).toBytes()[1]);

				ByteArrayOutputStream outputStreamRelayStatus = new ByteArrayOutputStream();
				outputStreamRelayStatus.write(res.getRegister(18).toBytes());
				int status = new BigInteger(1, outputStreamRelayStatus.toByteArray()).intValue();
				switch (status) {
				case 41120: {
					meterReading.setRelayStatus("OFF");
					break;
				}
				case 40960: {
					meterReading.setRelayStatus("ON");
					break;
				}
				default: {
					meterReading.setRelayStatus("");
				}

				}

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				outputStream.write(res.getRegister(94).toBytes());
				outputStream.write(res.getRegister(95).toBytes());
				byte[] c = outputStream.toByteArray();
				ByteArrayOutputStream outputStreamDG = new ByteArrayOutputStream();
				outputStreamDG.write(res.getRegister(96).toBytes());
				outputStreamDG.write(res.getRegister(97).toBytes());
				byte[] d = outputStreamDG.toByteArray();

				BigInteger bi = new BigInteger(1, c);
				BigDecimal bd = new BigDecimal(bi);

				BigInteger dg_bi = new BigInteger(1, d);
				BigDecimal dg_bd = new BigDecimal(dg_bi);
				meterReading.setGridReading(bd.divide(new BigDecimal(10)).doubleValue());
				meterReading.setDgReading(dg_bd.divide(new BigDecimal(10)).doubleValue());

				// Prepare other request for other parameters
				Thread.sleep(800);

				res = modbusService.readMultipleRegistersRequest(meterId, addressMap.getOtherRegisterAddress(),
						addressMap.getOtherRegistersCount(), connection);
				if (null != res) {

					List<BigDecimal> parametersArray = new ArrayList<>();
					int counter = 0;
					for (int i = 0; i < 26; i++) {
						if (i == 19) {
							ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
							ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
							outputStream1.write(res.getRegister(counter).toBytes());
							counter++;
							outputStream2.write(res.getRegister(counter).toBytes());
							counter++;
							byte c1[] = outputStream1.toByteArray();
							byte c2[] = outputStream2.toByteArray();

							BigInteger bi1 = new BigInteger(1, c1);
							BigDecimal bd1 = new BigDecimal(bi1);
							BigInteger bi2 = new BigInteger(1, c2);
							BigDecimal bd2 = new BigDecimal(bi2);
							parametersArray.add(bd1);
							parametersArray.add(bd2);
						} else if (i == 21) {

							ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
							counter++;
							byte signBit = res.getRegister(counter).toBytes()[0];
							int sign = signBit;
							outputStream2.write(res.getRegister(counter).toBytes());
							counter++;
							parametersArray.add(new BigDecimal(sign));
							byte c2[] = outputStream2.toByteArray();
							byte cx = c2[1];
							int low = cx & 0xf;
							parametersArray.add(new BigDecimal(low));

						} else if (i == 24) {

							ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
							ByteArrayOutputStream outputStreamV = new ByteArrayOutputStream();
							counter++;
							outputStreamV.write(res.getRegister(counter).toBytes()[0]);
							outputStream1.write(res.getRegister(counter).toBytes()[1]);
							counter++;
							outputStream1.write(res.getRegister(counter).toBytes());

							byte c1[] = outputStream1.toByteArray();
							BigInteger bi1 = new BigInteger(1, c1);
							int serial = bi1.intValue();
							parametersArray
									.add(new BigDecimal(new BigInteger(1, outputStreamV.toByteArray()).intValue())
											.divide(BigDecimal.TEN));
							parametersArray.add(new BigDecimal(serial));

						} else {

							ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
							outputStream2.write(res.getRegister(counter).toBytes());
							counter++;
							outputStream2.write(res.getRegister(counter).toBytes());
							counter++;
							byte c2[] = outputStream2.toByteArray();
							BigInteger bi2 = new BigInteger(1, c2);
							BigDecimal bd2 = new BigDecimal(bi2);
							parametersArray.add(bd2);
						}
					}
					meterReading.setVPhaseB(parametersArray.get(0).divide(new BigDecimal(10)).doubleValue());
					meterReading.setVPhaseY(parametersArray.get(1).divide(new BigDecimal(10)).doubleValue());
					meterReading.setVPhaseR(parametersArray.get(2).divide(new BigDecimal(10)).doubleValue());
					meterReading.setCPhaseR(parametersArray.get(3).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setCPhaseY(parametersArray.get(4).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setCPhaseB(parametersArray.get(5).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setPFactorR(parametersArray.get(6).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setPFactorY(parametersArray.get(7).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setPFactorB(parametersArray.get(8).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setFrequency(parametersArray.get(9).divide(new BigDecimal(10)).doubleValue());
					meterReading.setKWPhaseR(parametersArray.get(10).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setKWPhaseY(parametersArray.get(11).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setKWPhaseB(parametersArray.get(12).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setKvaPhaseR(parametersArray.get(13).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setKvaPhaseY(parametersArray.get(14).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setKvaPhaseB(parametersArray.get(15).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setPowerFactor(parametersArray.get(16).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setPowerKW(parametersArray.get(17).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setPowerKva(parametersArray.get(18).divide(new BigDecimal(1000)).doubleValue());
					meterReading.setFirmwareVersion(parametersArray.get(26).toString());
					meterReading.setSerialNo(parametersArray.get(27).toString());
					meterReading.setEnergySource(parametersArray.get(23).toString());

				}

				meterReading.setReadingDateTime(LocalDateTime.now().toString());
				meterReading.setStatus(true);
				return meterReading;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return meterReading;
	}

	@Override
	public boolean setUnitId(Short unitId) {
		
         String hexString = Integer.toHexString(unitId);
         
         //Step 1: Append "AA" to the hexadecimal string
         hexString += "AA";

         Register changeMeterIdRegister = new SimpleRegister(Integer.parseInt(hexString,16));
         
         return modbusService.writeMultipleRegistersRequest(meterId,addressMap.getChangeMeterIdAddress() ,connection,changeMeterIdRegister);
         
		
	}

	@Override
	public boolean setLoad(Double ebLoad, Double dgLoad) {

		MeterData meter = this.readMeter();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String serialNo = meter.getSerialNo();
		boolean isProtectionModeEnabled = this.enableProtectionMode(serialNo);
		if (isProtectionModeEnabled) {

			Register ebLoadReg = new SimpleRegister(ebLoad.intValue() * 100);
			Register dgLoadReg = new SimpleRegister(dgLoad.intValue() * 100);

			return super.setLoad(ebLoadReg, dgLoadReg);
		}
		return isProtectionModeEnabled;

	}
	
	@Override
	public boolean connect() {

		MeterData meter = this.readMeter();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String serialNo = meter.getSerialNo();
		String relayStatus = meter.getRelayStatus(); //For confirmation of meter on or off
		if (relayStatus != null && relayStatus.equals("ON")) {
			log.info("Relay status is already ON for this meter serial number: " + serialNo);
			return true;
		}
		
		boolean isProtectionModeEnabled = this.enableProtectionMode(serialNo);
		if (isProtectionModeEnabled) {
			return super.connect();
		}
		return isProtectionModeEnabled;

	}
	
	@Override
	public boolean disconnect() {

		MeterData meter = this.readMeter();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String serialNo = meter.getSerialNo();
		String relayStatus = meter.getRelayStatus(); //For confirmation of meter on or off
		if (relayStatus != null && relayStatus.equals("OFF")) {
			log.info("Relay status is already OFF for this meter serial number: " + serialNo);
			return true;
		}

		boolean isProtectionModeEnabled = this.enableProtectionMode(serialNo);
		if (isProtectionModeEnabled) {
			return super.disconnect();
		}
		return isProtectionModeEnabled;

	}


	private boolean enableProtectionMode(String serialNo) {
		if (null != serialNo && !serialNo.equalsIgnoreCase("")) {
			Register[] serialPasswordRegister = SunStarDSPP.serialToPasswordRegisters(Integer.parseInt(serialNo));

			return modbusService.writeMultipleRegistersRequest(meterId, addressMap.getValidatorRegisterAddress(),
					connection,serialPasswordRegister);

		}
		return false;

	}
	
	private static Register[] serialToPasswordRegisters(int serial) {
        String numberString = String.valueOf(serial); // Replace this with your 8-digit number as a string
        while (numberString.length() < 8) {
            numberString = "0" + numberString;
        }

        // Extract the 6 least significant digits
        String lsbDigits = numberString.substring(2);

        Register[] registers = new Register[lsbDigits.length() / 2];
        int registerIndex = 0;
        // Convert each digit to a character and print its ASCII value
        for (int i = 0; i < lsbDigits.length(); i += 2) {
            char firstDigit = lsbDigits.charAt(i);
            char secondDigit = (i + 1 < lsbDigits.length()) ? lsbDigits.charAt(i + 1) : '0';

            char firstChar = (char) (firstDigit + 'A' - '0');
            char secondChar = (char) (secondDigit + 'A' - '0');

            String asciiHex = Integer.toHexString((int) firstChar) + Integer.toHexString((int) secondChar);
            int asciiDecimal = Integer.parseInt(asciiHex, 16);

//            System.out.println("Pair " + (i / 2 + 1) + " being " + firstChar + secondChar
//                    + ", hex number " + asciiHex + " becomes " + asciiDecimal);
            Register register = new SimpleRegister(asciiDecimal);

            // Add the Register object to the array
            registers[registerIndex] = register;
            registerIndex++;
        }
        return registers;

    }

}
