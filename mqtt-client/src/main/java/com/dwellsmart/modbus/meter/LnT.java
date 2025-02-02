package com.dwellsmart.modbus.meter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;

import com.dwellsmart.dto.MeterData;
import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.pojo.MeterAddressMap;

import lombok.extern.slf4j.Slf4j;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.net.RTUTCPMasterConnection;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;

@Slf4j
public class LnT extends AbstractMeter {

	public LnT(short meterId, MeterAddressMap addressMap, RTUTCPMasterConnection connection) {
		super(meterId, addressMap, connection);
	}

	@Override
	public MeterData readMeter() {

		MeterData meterReading = null;

		// Prepare the request
		ReadInputRegistersResponse res = modbusService.readInputRegistersRequest(meterId,
				addressMap.getKwhRegisterAddress(), addressMap.getKwhRegisterAddressesCount(), connection);
		try {
			if (null != res) {
				meterReading = new MeterData();

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				outputStream.write(res.getRegister(0).toBytes());
				outputStream.write(res.getRegister(1).toBytes());
				byte c[] = outputStream.toByteArray();
				BigInteger bi = new BigInteger(1, c);
				BigDecimal gridReading = new BigDecimal(bi);
				meterReading.setGridReading(gridReading.divide(new BigDecimal(10)).doubleValue());

				ByteArrayOutputStream outputStreamDG = new ByteArrayOutputStream();
				outputStreamDG.write(res.getRegister(28).toBytes());
				outputStreamDG.write(res.getRegister(29).toBytes());
				byte d[] = outputStreamDG.toByteArray();

				BigInteger dg_bi = new BigInteger(1, d);
				BigDecimal dg_bd = new BigDecimal(dg_bi);

				meterReading.setDgReading(dg_bd.divide(new BigDecimal(10)).doubleValue());

				// Prepare other request for other parameters
				Thread.sleep(500);

				try { // This handling for reading additional parameter will work in future
					res = modbusService.readInputRegistersRequest(meterId, addressMap.getOtherRegisterAddress(),
							addressMap.getOtherRegistersCount(), connection);
					if (null != res) {

						double vPhaseR = readSingleRegister(res, 0).divide(new BigDecimal(100)).doubleValue();
						double cPhaseR = readSingleRegister(res, 1).divide(new BigDecimal(100)).doubleValue();

						meterReading.setVPhaseR(vPhaseR);
						meterReading.setCPhaseR(cPhaseR);
						meterReading.setKWPhaseR(readTwoRegisters(res, 3).divide(new BigDecimal(10000)).doubleValue());
						meterReading.setKvaPhaseR(readTwoRegisters(res, 5).divide(new BigDecimal(10000)).doubleValue());
						meterReading.setFrequency(readSingleRegister(res, 7).divide(new BigDecimal(10)).doubleValue());
						meterReading.setPFactorR(readSingleRegister(res, 8).divide(new BigDecimal(100)).doubleValue());
						meterReading.setPowerKW(meterReading.getKWPhaseR());
						meterReading.setPowerKva(meterReading.getKvaPhaseR());
						meterReading.setPowerFactor(meterReading.getPFactorR());
						meterReading.setEnergySource("EB");
					}
				} catch (Exception e) {
					e.printStackTrace();
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

	@Override
	public boolean connect() {
		Register reg1 = new SimpleRegister(addressMap.getConnectRegisterValue());
		Register reg2 = new SimpleRegister(4361);
		Register reg3 = new SimpleRegister(4622);
		Register reg4 = new SimpleRegister(10496);
		return modbusService.writeMultipleRegistersRequest(meterId, addressMap.getConnectRegisterAddress(), connection,
				reg1, reg2, reg3, reg4);
	}

	@Override
	public boolean disconnect() {
		Register reg1 = new SimpleRegister(addressMap.getDisconnectRegisterValue());
		Register reg2 = new SimpleRegister(4361);
		Register reg3 = new SimpleRegister(4622);
		Register reg4 = new SimpleRegister(10496);
		return modbusService.writeMultipleRegistersRequest(meterId, addressMap.getConnectRegisterAddress(), connection,
				reg1, reg2, reg3, reg4);
	}

	@Override
	public boolean setUnitId(Short unitId) {
		log.warn("This method is not implemented yet.");
		return false;
	}

	@Override
	public boolean setLoad(Double ebLoad, Double dgLoad) {

		if ((ebLoad > 0 && ebLoad < 13) && (dgLoad > 0 && dgLoad < 13)) {
			Register reg1 = new SimpleRegister(0x5052);
			Register reg2 = new SimpleRegister(0x4558);
			Register reg3 = new SimpleRegister(0x4E45);

//               Enable Password protected Fields
			int passwordRegistersAddress = 13;
			boolean isSuccess = modbusService.writeMultipleRegistersRequest(meterId, passwordRegistersAddress,
					connection, reg1, reg2, reg3);

			if (isSuccess) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Register ebLoadReg = new SimpleRegister(ebLoad.intValue() * 10);
				Register dgLoadReg = new SimpleRegister(dgLoad.intValue() * 10);
				return modbusService.writeMultipleRegistersRequest(meterId, addressMap.getLoadRegisterAddress(),
						connection, ebLoadReg, dgLoadReg);
			}
			return isSuccess;
		} else {
			throw new ApplicationException("Invalid Load Limits, Max value 12, Min value 1");
		}
	}

	public BigDecimal readSingleRegister(ReadInputRegistersResponse res, int position) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(res.getRegister(position).toBytes());
		byte c[] = outputStream.toByteArray();
		BigInteger bi = new BigInteger(1, c);
		BigDecimal bd = new BigDecimal(bi);
		return bd;

	}

	public BigDecimal readTwoRegisters(ReadInputRegistersResponse res, int position) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		outputStream.write(res.getRegister(position).toBytes());
		position++;
		outputStream.write(res.getRegister(position).toBytes());

		byte c[] = outputStream.toByteArray();
		BigInteger bi = new BigInteger(1, c);
		BigDecimal bd = new BigDecimal(bi);
		return bd;

	}

}
