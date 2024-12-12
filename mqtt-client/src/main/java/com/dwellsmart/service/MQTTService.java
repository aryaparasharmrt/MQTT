package com.dwellsmart.service;

import static com.dwellsmart.constants.MQTTConstants.CLIENT_ID;
import static com.dwellsmart.constants.MQTTConstants.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.mqtt.MQTTConnection;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;

@Component
@Slf4j
public class MQTTService {

	private final PoolManager manager ;
	
	private final MqttClientConnection connection = MQTTConnection.createMqttConnection(CLIENT_ID);
	
	public MQTTService(PoolManager manager) {
		this.manager = manager;
	}
	
	@PostConstruct
	public void init() {
		this.connect();
		this.defaultSubscribe();
	}
	
	public void defaultPublish(byte ...response) {
		connection.publish(new MqttMessage(DEFAULT_PUBLISH_TOPIC, response, QualityOfService.AT_LEAST_ONCE, false));
		log.info("Response sent to default topic: " + DEFAULT_PUBLISH_TOPIC);// TODO for logging
	}

	
	private void connect() {
		CompletableFuture<Boolean> connected = connection.connect();
		try {
			boolean sessionPresent = connected.get();
			log.info("Connected to " + (!sessionPresent ? "new" : "existing") + " session!");
		} catch (Exception ex) {
			throw new ApplicationException("Exception occurred during connect: \n" + ex);
		}

	}
	
	private void defaultSubscribe() {
		connection.subscribe(DEFAULT_SUBSCRIBE_TOPIC, QualityOfService.AT_LEAST_ONCE, message -> {
			log.info("Request Received Timestamp: " + LocalDateTime.now());
			String payload = new String(message.getPayload(), StandardCharsets.UTF_8);

			// Process the payload
			manager.processPayload(payload, this);
		});
		log.info("Subscribed to default topic: " + DEFAULT_SUBSCRIBE_TOPIC);
	}

	@PreDestroy
	public void cleanup() {
		// Disconnect
//        CompletableFuture<Void> disconnected = connection.disconnect();
//        try {
//			disconnected.get();
//		} catch (InterruptedException | ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

        connection.close();
		log.info("Connection closed");
	}

}