package com.dwellsmart.service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Component;

import com.dwellsmart.exception.ApplicationException;
import com.dwellsmart.mqtt.MQTTConnection;
import com.dwellsmart.mqtt.MQTTProperties;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;

@Component
@Slf4j
public class MQTTService {

	private final PoolManager manager;
	
	private final MQTTProperties properties;

	private final MqttClientConnection connection;

	public MQTTService(PoolManager manager, MQTTProperties properties) {
		this.manager = manager;
		this.properties = properties;
		this.connection = MQTTConnection.createMqttConnection(properties, this);
	}

	@PostConstruct
	public void init() {
		this.connect();
		this.subscribe();
	}

	public void publish(byte... response) {
		String defaultPublishTopic = properties.getPublishTopic();
		connection.publish(new MqttMessage(defaultPublishTopic, response, QualityOfService.AT_LEAST_ONCE, false));
		log.info("Response sent to default topic: " + defaultPublishTopic);
	}

	public void publish(String topic, byte... response) {
		connection.publish(new MqttMessage(topic, response, QualityOfService.AT_LEAST_ONCE, false));
		log.info("Response sent to this topic: " + topic);
	}

	private void connect() {
		CompletableFuture<Boolean> connected = connection.connect();
		try {
			connected.get();
		} catch (Exception ex) {
			throw new ApplicationException("Exception occurred during connect: \n" + ex);
		}

	}

	private void subscribe() {
		String subscribeTopic = properties.getSubscribeTopic();
		connection.subscribe(subscribeTopic, QualityOfService.AT_LEAST_ONCE, message -> {
			String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
			
			manager.processPayload(payload, this);
		});
		log.info("Subscribed to topic: " + subscribeTopic);
	}

	public void resubscribeToTopics() {
		this.subscribe();
		log.info("Resubscribing to topics after clean session reconnect.");
	}

	@PreDestroy
	public void cleanup() {
		
		CompletableFuture<Void> disconnected = connection.disconnect();
		try {
			disconnected.get();
		} catch (InterruptedException | ExecutionException e) {
			log.error("Disconnection interrupted",e);
		}
		connection.close();
	}

}