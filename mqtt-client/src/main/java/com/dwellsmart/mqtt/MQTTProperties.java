package com.dwellsmart.mqtt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class MQTTProperties {

	@Value("${mqtt.endpoint}")
	private String endpoint;

	@Value("${mqtt.cert.path}")
	private String certPath;

	@Value("${mqtt.key.path}")
	private String keyPath;

	@Value("${mqtt.ca.path}")
	private String caPath;

	@Value("${mqtt.subscribe.topic}")
	private String subscribeTopic;

	@Value("${mqtt.publish.topic}")
	private String publishTopic;

	@Value("${mqtt.client.id}")
	private String clientId;

}
