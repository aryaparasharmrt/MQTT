package com.dwellsmart.modbus.meter;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.dwellsmart.dto.MeterData;
import com.dwellsmart.pojo.MeterAddressMap;

import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.RTUTCPMasterConnection;

public class SunStarDS extends Meter {

	public SunStarDS(short meterId, MeterAddressMap addressMap, RTUTCPMasterConnection connection) {
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
				meterReading.setEbLoad(new BigInteger(1, outputStreamEBLoad.toByteArray()).doubleValue());

				ByteArrayOutputStream outputStreamDGLoad = new ByteArrayOutputStream();
				outputStreamDGLoad.write(res.getRegister(8).toBytes());
				meterReading.setDgLoad(new BigInteger(1, outputStreamDGLoad.toByteArray()).doubleValue());

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
				Thread.sleep(100);

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

				meterReading.setReadingDateTime(LocalDate.now().toString());
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setLoad(Double ebLoad, Double dgLoad) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean resetDefaultPassword() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean setUpProtectedMode() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean factoryReset() {
		// TODO Auto-generated method stub
		return false;
	}

}