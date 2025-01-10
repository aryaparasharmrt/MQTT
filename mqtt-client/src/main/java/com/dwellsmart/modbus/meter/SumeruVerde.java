package com.dwellsmart.modbus.meter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import com.dwellsmart.dto.MeterData;
import com.dwellsmart.pojo.MeterAddressMap;

import lombok.extern.slf4j.Slf4j;
import net.wimpi.modbus.io.ModbusRTUTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteMultipleRegistersRequest16;
import net.wimpi.modbus.msg.WriteMultipleRegistersResponse16;
import net.wimpi.modbus.net.RTUTCPMasterConnection;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;
import net.wimpi.modbus.util.ModbusUtil;

/**
 * @author Anshul Goyal This class cover both sumeru verde meters like which
 *         meter have msb first and other types. Meter Type 4, 8
 */
@Slf4j
public class SumeruVerde extends AbstractMeter {

	public static final int METER_TYPE_SUMERU_EBDG_SENSING_REG_ADDRESS = 3122;

	public SumeruVerde(short meterId, MeterAddressMap addressMap, RTUTCPMasterConnection connection) {
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

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				outputStream.write(res.getRegister(0).toBytes());
				outputStream.write(res.getRegister(1).toBytes());
				byte c[] = outputStream.toByteArray();
				ByteArrayOutputStream outputStreamDG = new ByteArrayOutputStream();
				outputStreamDG.write(res.getRegister(14).toBytes());
				outputStreamDG.write(res.getRegister(15).toBytes());
				byte d[] = outputStreamDG.toByteArray();
				BigInteger bi = new BigInteger(1, c);
				BigDecimal gridReading = new BigDecimal(bi).divide(new BigDecimal(1000));
				BigInteger dg_bi = new BigInteger(1, d);
				BigDecimal dgReading = new BigDecimal(dg_bi).divide(new BigDecimal(1000));

				meterReading.setGridReading(gridReading.doubleValue());
				meterReading.setDgReading(dgReading.doubleValue());

				// Prepare other request for other parameters
				Thread.sleep(500);

				res = modbusService.readMultipleRegistersRequest(meterId, addressMap.getOtherRegisterAddress(),
						addressMap.getOtherRegistersCount(), connection);
				if (null != res) {

					BigDecimal vPhaseR = readSingleRegister(res.getRegister(0)).divide(new BigDecimal(100));
					BigDecimal vPhaseY = readSingleRegister(res.getRegister(1)).divide(new BigDecimal(100));
					BigDecimal vPhaseB = readSingleRegister(res.getRegister(2)).divide(new BigDecimal(100));
					BigDecimal cPhaseR = readSingleRegister(res.getRegister(3)).divide(new BigDecimal(100));
					BigDecimal cPhaseY = readSingleRegister(res.getRegister(4)).divide(new BigDecimal(100));
					BigDecimal cPhaseB = readSingleRegister(res.getRegister(5)).divide(new BigDecimal(100));
					String powerKW = readSingleRegister(res.getRegister(6)).divide(new BigDecimal(100)).toString();
					String powerKVA = readSingleRegister(res.getRegister(8)).divide(new BigDecimal(100)).toString();
					String powerFactor = (addressMap.isEnableMsbFirst())
							? readSingleRegisterMSBFirst(res.getRegister(9)).divide(new BigDecimal(1000)).toString()
							: readSingleRegister(res.getRegister(9)).divide(new BigDecimal(1000)).toString();

					String frequency = readSingleRegister(res.getRegister(10)).divide(new BigDecimal(1000)).toString();

					meterReading.setVPhaseR(vPhaseR.doubleValue());
					meterReading.setVPhaseY(vPhaseY.doubleValue());
					meterReading.setVPhaseB(vPhaseB.doubleValue());
					meterReading.setCPhaseR(cPhaseR.doubleValue());
					meterReading.setCPhaseY(cPhaseY.doubleValue());
					meterReading.setCPhaseB(cPhaseB.doubleValue());
					meterReading.setPowerKW(Double.valueOf(powerKW));
					meterReading.setPowerKva(Double.valueOf(powerKVA));
					meterReading.setPowerFactor(Double.valueOf(powerFactor));
					meterReading.setFrequency(Double.valueOf(frequency));

					// Read MSB LSB of register 25 and MSB of register 26 to compute Relay Status of
					// each relay
					meterReading.setRelayStatus(((readSingleRegisterMSB(res.getRegister(25)).toString()
							+ readSingleRegisterLSB(res.getRegister(25)).toString()
							+ readSingleRegisterMSB(res.getRegister(26)).toString()).equals("000")) ? "OFF" : "ON");

					// Read LSB of register 26 to compute Energy Source
					Thread.sleep(1000);

					res = modbusService.readMultipleRegistersRequest(meterId,
							METER_TYPE_SUMERU_EBDG_SENSING_REG_ADDRESS, 1, connection);
					meterReading.setEnergySource(
							readSingleRegisterMSB(res.getRegister(0)).equals(new BigDecimal(BigInteger.ONE)) ? "DG"
									: "EB");
					Thread.sleep(1000);

					res = modbusService.readMultipleRegistersRequest(meterId, addressMap.getLoadRegisterAddress(), 2,
							connection);
					if (addressMap.isEnableMsbFirst()) {

						meterReading.setEbLoad(readSingleRegisterMSBFirst(res.getRegister(0)).divide(new BigDecimal(10))
								.doubleValue());
						meterReading.setDgLoad(readSingleRegisterMSBFirst(res.getRegister(1)).divide(new BigDecimal(10))
								.doubleValue());

					} else {
						meterReading.setEbLoad(
								readSingleRegister(res.getRegister(0)).divide(new BigDecimal(10)).doubleValue());
						meterReading.setDgLoad(
								readSingleRegister(res.getRegister(1)).divide(new BigDecimal(10)).doubleValue());
					}

				}

				meterReading.setReadingDateTime(LocalDateTime.now().toString());
				meterReading.setStatus(true);
				return meterReading;
			}
		} catch (Exception e) {
			e.printStackTrace();// We will add logic for retry mechanism
		}

		return meterReading;
	}

	public static BigDecimal readSingleRegister(Register nibble) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(nibble.toBytes());
		byte c[] = outputStream.toByteArray();
		BigInteger bi = new BigInteger(1, c);
		BigDecimal bd = new BigDecimal(bi);
		return bd;
	}

	public BigDecimal readSingleRegisterMSB(Register reg) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(reg.toBytes());
		byte c[] = outputStream.toByteArray();
		int cx = c[0];

		BigInteger bi = BigInteger.valueOf(cx);
		BigDecimal bd = new BigDecimal(bi);
		return bd;

	}

	public static BigDecimal readSingleRegisterMSBFirst(Register nibble) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(nibble.toBytes()[1]);
		outputStream.write(nibble.toBytes()[0]);
		byte c[] = outputStream.toByteArray();
		BigInteger bi = new BigInteger(1, c);
		BigDecimal bd = new BigDecimal(bi);
		return bd;
	}

	public BigDecimal readSingleRegisterLSB(Register reg) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(reg.toBytes());
		byte c[] = outputStream.toByteArray();
		int cx = c[1];

		BigInteger bi = BigInteger.valueOf(cx);
		BigDecimal bd = new BigDecimal(bi);
		return bd;

	}

	@Override
	public boolean connect() {
		if (meterId != 0) {
			return super.connect();
		}
		super.connect(); // Like if meterId == 0 that means not waiting for response only send request
		return true;
	}

	@Override
	public boolean disconnect() {
		if (meterId != 0) {
			return super.disconnect();
		}
		super.disconnect(); // Like if meterId == 0 that means not waiting for response only send request
		return true;
	}

	@Override
	public boolean setLoad(Double ebLoad, Double dgLoad) {
		int ebLoadForSumeru = ebLoad.intValue() * 10;
		int dgLoadForSumeru = dgLoad.intValue() * 10;

		if (addressMap.isEnableMsbFirst()) {
			byte hi = ModbusUtil.lowByte(ebLoadForSumeru);
			byte lo = ModbusUtil.hiByte(ebLoadForSumeru);
			Register ebLoadReg = new SimpleRegister(hi, lo);

			boolean ebLoadRes = modbusService.writeSingleRegistersRequest(meterId, addressMap.getLoadRegisterAddress(),
					connection, ebLoadReg);

			if (ebLoadRes) {

				byte hi_dg = ModbusUtil.lowByte(dgLoadForSumeru);
				byte lo_dg = ModbusUtil.hiByte(dgLoadForSumeru);
				Register dgLoadReg = new SimpleRegister(hi_dg, lo_dg);
				return modbusService.writeSingleRegistersRequest(meterId, addressMap.getLoadRegisterAddress() + 1,
						connection, dgLoadReg);
			}

			return ebLoadRes;
		}
		try { // Will improve the logic like retry mechanism and more...
			Register ebLoadReg = new SimpleRegister(ebLoadForSumeru);
			Register dgLoadReg = new SimpleRegister(dgLoadForSumeru);
			WriteMultipleRegistersRequest16 req = new WriteMultipleRegistersRequest16(
					addressMap.getLoadRegisterAddress(), new Register[] { ebLoadReg, dgLoadReg });
			req.setUnitID(meterId);
			ModbusRTUTCPTransaction trans = new ModbusRTUTCPTransaction(connection);
			trans.setRequest(req);
			trans.execute();
			WriteMultipleRegistersResponse16 res = (WriteMultipleRegistersResponse16) trans.getResponse();
			if (res.getFunctionCode() == req.getFunctionCode() && res.getUnitID() == req.getUnitID()) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean setUnitId(Short unitId) {
		log.warn("Not yet implemented");
		return false;
	}

}
