package com.dwellsmart.modbus.meter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.dwellsmart.dto.MeterData;
import com.dwellsmart.pojo.MeterAddressMap;

import lombok.extern.slf4j.Slf4j;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.net.RTUTCPMasterConnection;

@Slf4j
public class Enertrak extends AbstractMeter {

	public Enertrak(short meterId, MeterAddressMap addressMap, RTUTCPMasterConnection connection) {
		super(meterId, addressMap, connection);
	}

	@Override
	public MeterData readMeter() {

		MeterData meterReading = null;

		ReadMultipleRegistersResponse res = modbusService.readMultipleRegistersRequest(meterId,
				addressMap.getKwhRegisterAddress(), addressMap.getKwhRegisterAddressesCount(), connection);
		try {
			if (null != res) {
				meterReading = new MeterData();

				ByteArrayOutputStream outputStreamGrid = new ByteArrayOutputStream();
				outputStreamGrid.write(res.getRegister(0).toBytes());
				outputStreamGrid.write(res.getRegister(1).toBytes());
				byte gridBytes[] = outputStreamGrid.toByteArray();
				BigInteger grid_bi = new BigInteger(1, gridBytes);
				BigDecimal grid_bd = new BigDecimal(grid_bi);
				meterReading.setGridReading(grid_bd.divide(new BigDecimal(10)).doubleValue());

				ByteArrayOutputStream outputStreamDG = new ByteArrayOutputStream();
				outputStreamDG.write(res.getRegister(2).toBytes());
				outputStreamDG.write(res.getRegister(3).toBytes());
				byte dgBytes[] = outputStreamDG.toByteArray();
				BigInteger dg_bi = new BigInteger(1, dgBytes);
				BigDecimal dg_bd = new BigDecimal(dg_bi);
				meterReading.setDgReading(dg_bd.divide(new BigDecimal(10)).doubleValue());

				// Prepare other request for other parameters

				try { // Will work in future

					readEnertrakMeterProfile(meterReading);

					Thread.sleep(600);
					int additionalParametersCount = addressMap.getOtherRegistersCount();

					res = modbusService.readMultipleRegistersRequest(meterId, addressMap.getOtherRegisterAddress(),
							additionalParametersCount, connection);
					List<BigDecimal> parametersArray = new ArrayList<>();
					int counter = 0;
					try {
						if (null != res) {
							for (int i = 0; i < additionalParametersCount / 2; i++) {
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
									ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
									ByteArrayOutputStream outputStream2 = new ByteArrayOutputStream();
									outputStream1.write(res.getRegister(counter).toBytes());
									counter++;
									outputStream2.write(res.getRegister(counter).toBytes());
									counter++;
									byte c1[] = outputStream1.toByteArray();
									BigInteger bi1 = new BigInteger(1, c1);
									BigDecimal bd1 = new BigDecimal(bi1);
									parametersArray.add(bd1);
									byte c2[] = outputStream2.toByteArray();
									byte cx = c2[1];
//								int high = (cx & 0xf0) >> 4;
									int low = cx & 0xf;
//		                        System.out.format("%x%n", high);
//		                        System.out.format("%x%n", low);
//								BigInteger bi2 = new BigInteger(1, c2);
//								BigDecimal bd2 = new BigDecimal(bi2);
									parametersArray.add(new BigDecimal(low));
								} else {
									ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
									outputStream.write(res.getRegister(counter).toBytes());
									counter++;
									outputStream.write(res.getRegister(counter).toBytes());
									counter++;
									byte c[] = outputStream.toByteArray();
									BigInteger bi = new BigInteger(1, c);
									BigDecimal bd = new BigDecimal(bi);
									parametersArray.add(bd);
								}
							}
						}
//		                System.out.println("Grid Reading is " + bd.divide(new BigDecimal(10)) + "  DG Reading is " + dg_bd.divide(new BigDecimal(10)));
						if (!parametersArray.isEmpty()) {
							meterReading.setVPhaseR(parametersArray.get(0).divide(new BigDecimal(10)).doubleValue());
							meterReading.setVPhaseY(parametersArray.get(1).divide(new BigDecimal(10)).doubleValue());
							meterReading.setVPhaseB(parametersArray.get(2).divide(new BigDecimal(10)).doubleValue());
							meterReading.setCPhaseR(parametersArray.get(3).divide(new BigDecimal(1000)).doubleValue());
							meterReading.setCPhaseY(parametersArray.get(4).divide(new BigDecimal(1000)).doubleValue());
							meterReading.setCPhaseB(parametersArray.get(5).divide(new BigDecimal(1000)).doubleValue());
							meterReading.setPFactorR(parametersArray.get(6).divide(new BigDecimal(1000)).doubleValue());
							meterReading.setPFactorY(parametersArray.get(7).divide(new BigDecimal(1000)).doubleValue());
							meterReading.setPFactorB(parametersArray.get(8).divide(new BigDecimal(1000)).doubleValue());
							meterReading.setFrequency(parametersArray.get(9).divide(new BigDecimal(10)).doubleValue());
							meterReading
									.setKWPhaseR(parametersArray.get(10).divide(new BigDecimal(1000)).doubleValue());
							meterReading
									.setKWPhaseY(parametersArray.get(11).divide(new BigDecimal(1000)).doubleValue());
							meterReading
									.setKWPhaseB(parametersArray.get(12).divide(new BigDecimal(1000)).doubleValue());
							meterReading
									.setKvaPhaseR(parametersArray.get(13).divide(new BigDecimal(1000)).doubleValue());
							meterReading
									.setKvaPhaseY(parametersArray.get(14).divide(new BigDecimal(1000)).doubleValue());
							meterReading
									.setKvaPhaseB(parametersArray.get(15).divide(new BigDecimal(1000)).doubleValue());
							meterReading
									.setPowerFactor(parametersArray.get(16).divide(new BigDecimal(1000)).doubleValue());
							meterReading.setPowerKW(parametersArray.get(17).divide(new BigDecimal(1000)).doubleValue());
							meterReading
									.setPowerKva(parametersArray.get(18).divide(new BigDecimal(1000)).doubleValue());
//		                meterReading.setvAvg(parametersArray.get(19).divide(new BigDecimal(10)).toString());
//		                meterReading.setiTot(parametersArray.get(20).divide(new BigDecimal(100)).toString());
							// Read parameter 23 to compute Energy Source
							meterReading.setEnergySource(
									computeEnergySourceforEnertrakMeters(parametersArray.get(23).intValue()));
						} else {
//						meterReading = null;
						}

					} catch (IndexOutOfBoundsException | IOException | NullPointerException ex) {
						ex.printStackTrace(); // this is warnings
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				meterReading.setStatus(true);
				return meterReading;
			}

		} catch (Exception e) {
			e.printStackTrace();// We will add logic for retry mechanism
		}

		return meterReading;
	}

	@Override
	public boolean setUnitId(Short unitId) {
		log.warn("This method is not implemented yet.");
		return false;
	}

	public void readEnertrakMeterProfile(MeterData meterReading) {

		try {
			ReadMultipleRegistersResponse res = modbusService.readMultipleRegistersRequest(meterId,
					(addressMap.getKwhRegisterAddress() - 44), 92, connection);

			if (null != res) {
				ByteArrayOutputStream outputStreamEBLoad = new ByteArrayOutputStream();
				outputStreamEBLoad.write(res.getRegister(0).toBytes());
				meterReading.setEbLoad(new BigInteger(1, outputStreamEBLoad.toByteArray()).doubleValue());

				ByteArrayOutputStream outputStreamDGLoad = new ByteArrayOutputStream();
				outputStreamDGLoad.write(res.getRegister(2).toBytes());
				meterReading.setDgLoad(new BigInteger(1, outputStreamDGLoad.toByteArray()).doubleValue());

				ByteArrayOutputStream outputStreamMode = new ByteArrayOutputStream();
				outputStreamMode.write(res.getRegister(6).toBytes()[0]);
				meterReading.setOpMode(
						(new BigInteger(1, outputStreamMode.toByteArray()).intValue() == 0) ? "KWH" : "KVAH");

				ByteArrayOutputStream outputStreamEBRelays = new ByteArrayOutputStream();
				outputStreamEBRelays.write(res.getRegister(6).toBytes()[1]);

				switch (new BigInteger(1, outputStreamEBRelays.toByteArray()).intValue()) {
				case 0: {
					meterReading.setEbRelayB(false);
					meterReading.setEbRelayY(false);
					meterReading.setEbRelayR(false);
					break;
				}
				case 1: {
					meterReading.setEbRelayB(true);
					meterReading.setEbRelayY(false);
					meterReading.setEbRelayR(false);
					break;
				}
				case 2: {
					meterReading.setEbRelayB(false);
					meterReading.setEbRelayY(true);
					meterReading.setEbRelayR(false);
					break;
				}
				case 4: {
					meterReading.setEbRelayB(false);
					meterReading.setEbRelayY(false);
					meterReading.setEbRelayR(true);
					break;
				}
				case 7: {
					meterReading.setEbRelayB(true);
					meterReading.setEbRelayY(true);
					meterReading.setEbRelayR(true);
					break;
				}

				}
				ByteArrayOutputStream outputStreamDGRelays = new ByteArrayOutputStream();
				outputStreamDGRelays.write(res.getRegister(7).toBytes()[0]);

				switch (new BigInteger(1, outputStreamEBRelays.toByteArray()).intValue()) {
				case 0: {
					meterReading.setDgRelayB(false);
					meterReading.setDgRelayY(false);
					meterReading.setDgRelayR(false);
					break;
				}
				case 1: {
					meterReading.setDgRelayB(true);
					meterReading.setDgRelayY(false);
					meterReading.setDgRelayR(false);
					break;
				}
				case 2: {
					meterReading.setDgRelayB(false);
					meterReading.setDgRelayY(true);
					meterReading.setDgRelayR(false);
					break;
				}
				case 4: {
					meterReading.setDgRelayB(false);
					meterReading.setDgRelayY(false);
					meterReading.setDgRelayR(true);
					break;
				}
				case 7: {
					meterReading.setDgRelayB(true);
					meterReading.setDgRelayY(true);
					meterReading.setDgRelayR(true);
					break;
				}

				}

				ByteArrayOutputStream outputStreamRelayStatus = new ByteArrayOutputStream();
				outputStreamRelayStatus.write(res.getRegister(12).toBytes());
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
					meterReading.setRelayStatus("--");
				}

				}
				Thread.sleep(800);
				meterReading.setFirmwareVersion(String.valueOf(readEnertrakMeterFwVersion()));
				Thread.sleep(800);
				meterReading.setSerialNo(String.valueOf(readEnertrakMeterSerial()));
				int firmwareVersion = Integer.parseInt(meterReading.getFirmwareVersion());

				if (firmwareVersion == 55 || firmwareVersion == 95) {
					meterReading.setEbLoad(meterReading.getEbLoad() / 100);
					meterReading.setDgLoad(meterReading.getDgLoad() / 100);
				}
			} else {
//				meterReading = null;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public int readEnertrakMeterFwVersion() {
		int version = 0;
		int address = addressMap.getOtherRegisterAddress() + 24; // the reference offset from otherRegisters
		int count = 4; // the number of DI's to read 4
		ReadMultipleRegistersResponse res = modbusService.readMultipleRegistersRequest(meterId, address, count,
				connection);

		if (null != res) {
			ByteArrayOutputStream outputStreamV = new ByteArrayOutputStream();
			outputStreamV.write(res.getRegister(1).toBytes()[0]);
			version = new BigInteger(1, outputStreamV.toByteArray()).intValue();
		}
		return version;
	}

	private String computeEnergySourceforEnertrakMeters(int energySourceCode) {
		String energySource = "";
		switch (energySourceCode) {
		case 1: {
			energySource = "Solar";
			break;
		}
		case 2: {
			energySource = "DG";
			break;
		}
		case 4: {
			energySource = "EB";
			break;
		}

		}
		return energySource;

	}

	public int readEnertrakMeterSerial() throws Exception {
		int serial = 0;
		int address = addressMap.getOtherRegisterAddress() + 24; // the reference offset from otherRegisters
		int count = 4; // the number of DI's to read 4

		ReadMultipleRegistersResponse res = modbusService.readMultipleRegistersRequest(meterId, address, count,
				connection);

		if (null != res) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			outputStream.write(res.getRegister(1).toBytes()[1]);
			outputStream.write(res.getRegister(2).toBytes());

			byte c[] = outputStream.toByteArray();
			BigInteger bi = new BigInteger(1, c);
			serial = bi.intValue();
		}
		return serial;
	}

}
