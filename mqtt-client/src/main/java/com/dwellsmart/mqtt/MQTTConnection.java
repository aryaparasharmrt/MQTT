package com.dwellsmart.mqtt;

import static com.dwellsmart.constants.MQTTConstants.CA_PATH;
import static com.dwellsmart.constants.MQTTConstants.CERT_PATH;
import static com.dwellsmart.constants.MQTTConstants.ENDPOINT;
import static com.dwellsmart.constants.MQTTConstants.KEY_PATH;

import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

public class MQTTConnection {

	public static MqttClientConnection createMqttConnection(String clientId) {
	    AwsIotMqttConnectionBuilder builder = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(CERT_PATH, KEY_PATH);
	    builder.withCertificateAuthorityFromPath(null, CA_PATH)
	           .withClientId(clientId)
	           .withEndpoint(ENDPOINT)
	           .withPort(8883)
	           .withKeepAliveSecs(60)
	           .withCleanSession(false) 
	           .withReconnectTimeoutSecs(1,300) 
	           .withProtocolOperationTimeoutMs(60000) ;

	    MqttClientConnection connection = builder.build();
	    builder.close();
	    return connection;
	}


}
