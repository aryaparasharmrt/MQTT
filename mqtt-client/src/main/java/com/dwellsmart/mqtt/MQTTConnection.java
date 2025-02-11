package com.dwellsmart.mqtt;

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

	public static MqttClientConnection createMqttConnection(MQTTProperties properties, MQTTService mqttService) {
		
		log.debug("Create MQTT Connection method called...");

		MqttClientConnectionEvents callbacks = new MqttClientConnectionEvents() {
			@Override
			public void onConnectionInterrupted(int errorCode) {
				if (errorCode != 0) {
					log.warn("Connection interrupted: " + errorCode + ": " + CRT.awsErrorString(errorCode)+" :: Clinet ID: "+properties.getClientId());
				}
			}

			@Override
			public void onConnectionResumed(boolean sessionPresent) {
				log.info("Connection resumed: with " + (sessionPresent ? "existing session" : "clean session")+ " :: Clinet ID: "+properties.getClientId());
				if (!sessionPresent) {
					mqttService.resubscribeToTopics();
				}
			}

			@Override
			public void onConnectionClosed(OnConnectionClosedReturn data) {
				log.warn("Sesison closed :: Clinet ID: "+properties.getClientId());
			}

			@Override
			public void onConnectionSuccess(OnConnectionSuccessReturn data) {
				boolean sessionPresent = data.getSessionPresent();
				log.info("Connected to " + (!sessionPresent ? "new" : "existing") + " session! :: Clinet ID: "+properties.getClientId());
			}

			@Override
			public void onConnectionFailure(OnConnectionFailureReturn data) {
				log.error("Session Failure : " + data.getErrorCode() + " :: Clinet ID: "+properties.getClientId());
			}
		};

		 AwsIotMqttConnectionBuilder builder = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(properties.getCertPath(), properties.getKeyPath());
				    builder.withConnectionEventCallbacks(callbacks)
				    	   .withCertificateAuthorityFromPath(null, properties.getCaPath())
				           .withClientId(properties.getClientId())
				           .withEndpoint(properties.getEndpoint())
				           .withPort(8883)
				           .withKeepAliveSecs(120)
				           .withCleanSession(true) 
				           .withReconnectTimeoutSecs(1,600) 
				           .withProtocolOperationTimeoutMs(60000);

	    MqttClientConnection connection = builder.build();
	    builder.close();
	    
	    
	    return connection;
	}
	
	

}
