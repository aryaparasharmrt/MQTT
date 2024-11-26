package com.dwellsmart.mqtt;

import static com.dwellsmart.constants.MQTTConstants.*;

import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

public class MQTTConnection {

	public static MqttClientConnection createMqttConnection(String clientId) {
		AwsIotMqttConnectionBuilder builder = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(CERT_PATH, KEY_PATH);
		builder.withCertificateAuthorityFromPath(null, CA_PATH);
		builder.withClientId(clientId).withEndpoint(ENDPOINT).withPort(8883).withCleanSession(true)
				.withProtocolOperationTimeoutMs(60000);
		MqttClientConnection connection = builder.build();
		builder.close();
		return connection;
	}

}
