package com.dwellsmart.service;

import static com.dwellsmart.constants.MQTTConstants.CLIENT_ID;
import static com.dwellsmart.constants.MQTTConstants.PUBLISH_TOPIC;
import static com.dwellsmart.constants.MQTTConstants.SUBSCRIBE_TOPIC;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dwellsmart.constants.ErrorCode;
import com.dwellsmart.dto.MeterOperationPayload;
import com.dwellsmart.dto.ResponseError;
import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.mqtt.MQTTConnection;
import com.dwellsmart.util.PayloadUtils;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttException;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;

@Component
public class MQTTService {

	@Autowired
	private PayloadUtils payloadUtils;

	@Autowired
	private MeterOperationService meterOperationService;

	private final MqttClientConnection connection = MQTTConnection.createMqttConnection(CLIENT_ID);

	@PostConstruct
	public void init() {

		try {
			connection.connect().get();
			System.out.println("Connected to AWS IoT Core!"); // TODO for logging

			// Subscribe
			connection.subscribe(SUBSCRIBE_TOPIC, QualityOfService.AT_LEAST_ONCE, message -> {

				String jsonPayload = new String(message.getPayload(), StandardCharsets.UTF_8);
				byte[] response = null;

				try {
					MeterOperationPayload operationPayload = payloadUtils.validateRequest(jsonPayload);

					//Main Functionality:
					meterOperationService.processOperation(operationPayload);

					response = payloadUtils.convertToResponseAsBytes(operationPayload);

				} catch (ApplicationException e) {
					e.printStackTrace();  // TODO for logging errorsS
					ResponseError responseError = ResponseError.builder().errorCode(e.getCode())
							.errorMessage(e.getMessage()).build();
					response = payloadUtils.convertToResponseAsBytes(responseError);
				} catch (Exception e) {
					e.printStackTrace();
					ResponseError responseError = ResponseError.builder()
							.errorCode(ErrorCode.GENERIC_EXCEPTION.getErrorCode())
							.errorMessage(ErrorCode.GENERIC_EXCEPTION.getErrorMessage()).build();
					response = payloadUtils.convertToResponseAsBytes(responseError);
				}

				connection.publish(new MqttMessage(PUBLISH_TOPIC, response, QualityOfService.AT_LEAST_ONCE, false));
				System.out.println("Response sent to topic: " + PUBLISH_TOPIC);// TODO for logging

			}).get();

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@PreDestroy
	public void cleanup() {

		connection.disconnect();
	}

}