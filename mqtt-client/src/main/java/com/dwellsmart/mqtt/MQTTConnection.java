package com.dwellsmart.mqtt;

import static com.dwellsmart.constants.MQTTConstants.CA_PATH;
import static com.dwellsmart.constants.MQTTConstants.CERT_PATH;
import static com.dwellsmart.constants.MQTTConstants.ENDPOINT;
import static com.dwellsmart.constants.MQTTConstants.KEY_PATH;

import com.dwellsmart.service.MQTTService;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.crt.CRT;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttClientConnectionEvents;
import software.amazon.awssdk.crt.mqtt.OnConnectionClosedReturn;
import software.amazon.awssdk.crt.mqtt.OnConnectionFailureReturn;
import software.amazon.awssdk.crt.mqtt.OnConnectionSuccessReturn;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

@Slf4j
public class MQTTConnection {

	public static MqttClientConnection createMqttConnection(String clientId, MQTTService mqttService) {

		MqttClientConnectionEvents callbacks = new MqttClientConnectionEvents() {
			@Override
			public void onConnectionInterrupted(int errorCode) {
				if (errorCode != 0) {
					log.warn("Connection interrupted: " + errorCode + ": " + CRT.awsErrorString(errorCode));
				}
			}

			@Override
			public void onConnectionResumed(boolean sessionPresent) {
				log.info("Connection resumed: with " + (sessionPresent ? "existing session" : "clean session"));
				if (!sessionPresent) {
					log.info("Resubscribing to topics after clean session reconnect...");
					mqttService.resubscribeToTopics();
				}
			}

			@Override
			public void onConnectionClosed(OnConnectionClosedReturn data) {
				log.warn("Sesison closed ");
			}

			@Override
			public void onConnectionSuccess(OnConnectionSuccessReturn data) {
				boolean sessionPresent = data.getSessionPresent();
				log.info("Conncetion success with old Session : " + (sessionPresent ? "Yes" : "NO"));
			}

			@Override
			public void onConnectionFailure(OnConnectionFailureReturn data) {
				log.error("Session Failure : " + data.getErrorCode());

			}
		};

		 AwsIotMqttConnectionBuilder builder = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(CERT_PATH, KEY_PATH);
				    builder.withConnectionEventCallbacks(callbacks)
				    	   .withCertificateAuthorityFromPath(null, CA_PATH)
				           .withClientId(clientId)
				           .withEndpoint(ENDPOINT)
				           .withPort(8883)
				           .withKeepAliveSecs(60)
				           .withCleanSession(true) 
				           .withReconnectTimeoutSecs(1,300) 
				           .withProtocolOperationTimeoutMs(60000);

	    MqttClientConnection connection = builder.build();
	    builder.close();
	    
	    
	    return connection;
	}
	
	

}
