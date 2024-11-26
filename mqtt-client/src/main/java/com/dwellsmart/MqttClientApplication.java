package com.dwellsmart;

import static com.dwellsmart.constants.MQTTConstants.CLIENT_ID;
import static com.dwellsmart.constants.MQTTConstants.PUBLISH_TOPIC;
import static com.dwellsmart.constants.MQTTConstants.SUBSCRIBE_TOPIC;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.dwellsmart.dto.MeterOperationPayload;
import com.dwellsmart.mqtt.MQTTConnection;
import com.dwellsmart.service.MeterOperationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttException;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;

@SpringBootApplication
//@EnableDiscoveryClient
public class MqttClientApplication {

	private static String responseMessage;


	public static void main(String[] args) {
		System.out.println("Application Started....");
		ConfigurableApplicationContext context = SpringApplication.run(MqttClientApplication.class, args);
		System.out.println("IOC Container Started...");
		MqttClientConnection connection = MQTTConnection.createMqttConnection(CLIENT_ID);
		try {
			connection.connect().get();
			System.out.println("Connected to AWS IoT Core!");
			ObjectMapper mapper = context.getBean(ObjectMapper.class);

			connection.subscribe(SUBSCRIBE_TOPIC, QualityOfService.AT_LEAST_ONCE, message -> {
				System.out.println(message);
				String payload = new String(message.getPayload(), StandardCharsets.UTF_8);

				System.out.println(payload);
				responseMessage = payload;
				byte[] writeValueAsBytes = null;
				int beanDefinitionCount = context.getBeanDefinitionCount();
				System.out.println(beanDefinitionCount);
				try {
					MeterOperationPayload request = mapper.readValue(payload, MeterOperationPayload.class);
					
					System.out.println("The Request is: " + request);

					MeterOperationService meterOperationService = context.getBean(MeterOperationService.class);
					MeterOperationPayload processOperation = meterOperationService.processOperation(request);
					System.out.println("The Response Is : "+processOperation);
					
				writeValueAsBytes = mapper.writeValueAsBytes(processOperation);
				
				String writeValueAsString = mapper.writeValueAsString(processOperation);
				System.out.println(writeValueAsString);
					
					
				} catch (JsonMappingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Message handler: Process incoming messages here
				System.out.println("Message received:");
				System.out.println("Topic: " + message.getTopic());

				// Respond to the received message
				try {
					String response = "Acknowledged: " + payload;
					connection.publish(
//							new MqttMessage(PUBLISH_TOPIC, response.getBytes(), QualityOfService.AT_LEAST_ONCE, false));
					new MqttMessage(PUBLISH_TOPIC, writeValueAsBytes, QualityOfService.AT_LEAST_ONCE, false));
					System.out.println("Response sent to topic: " + PUBLISH_TOPIC);
				} catch (Exception e) {
					System.err.println("Error sending response: " + e.getMessage());
				}
			}).get();

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}

		String jsonString = "{ \"meterTypeId\": 1, \"conAddress\": \"192.168.0.100:100\" }";
		// JSON to MeterRequest object conversion
		if (responseMessage == null) {
			responseMessage = jsonString;
		}
//		MeterRequest meterRequest = gson.fromJson(responseMessage, MeterRequest.class);

		// Displaying the values
//		System.out.println("Meter Type ID: " + meterRequest.getMeterTypeId());
//		System.out.println("Connection Address: " + meterRequest.getConAddress());

		System.out.println("Listening for messages...");
		synchronized (MqttClientApplication.class) {
			try {
				MqttClientApplication.class.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} // Keeps the connection active
		}

	}

}
