/*
 * 
 * __________________  DwellSMART CONFIDENTIAL __________________
 * 
 * (C) DwellSMART Pvt. Ltd. [2015] - All Rights Reserved.
 * 
 * NOTICE: All information contained herein is, and remains the property of DwellSMART Pvt. Ltd. 
 * and its partners, if any. The intellectual and technical concepts contained herein are 
 * proprietary to DwellSMART Pvt. Ltd. and its suppliers and may be covered by Patents,
 * patents in process, and are protected by trade secret or copyright law. 
 * Dissemination of this information or reproduction of this material is strictly forbidden 
 * unless prior written permission is obtained from DwellSMART Pvt. Ltd.
 * October 2015
 */
package com.dwellsmart.services;

import com.dwellsmart.entities.MeterMap;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.util.List;

import java.util.concurrent.CompletableFuture;
import javax.ejb.LocalBean;

import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import javax.ejb.Stateless;

/**
 *
 * @author aryap
 */
@Stateless
@LocalBean
public class MqttCommunication1 {

    String endpoint = "a23bq50uzv6v7o-ats.iot.ap-south-1.amazonaws.com";

    String clientId = "basestation1";
    String topic = "sdk/modbus/request/1";
    String certPath = "D:\\OneDrive\\Team Shared\\mqttcerts\\TestLaptopNikhil.cert.pem";
    String keyPath = "D:\\OneDrive\\Team Shared\\\\mqttcerts\\TestLaptopNikhil.private.key";
    String caPath = "D:\\OneDrive\\Team Shared\\mqttcerts\\root-CA.crt";

    int messageId = 1221;
    String ipAddress = "103.211.57.230:1100";
//    int projectId = 8;
    int meterId = 171;
    int meterTypeId = 8;
//    List<MeterMap> meterMaps = null;

    String operation = "read";

//    public boolean publishMessage(
//            String topic, // Topic for publishing
//            String messageId, // Dynamic message ID
//            String ipAddress, // Dynamic IP Address
//            String meterId, // Meter ID
//            String meterTypeId, // Meter Type ID
//            String operation, // Operation (read, connect, disconnect, setLoad)
//            Integer ebLoad, // Optional Eb load
//            Integer dgLoad // Optional DG load
//    ) 
    public boolean publishMessage() {
        MqttClientConnection connection = null;

        try {
            // Step 1: Build the MQTT Connection
            connection = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(certPath, keyPath)
                    .withEndpoint(endpoint)
                    .withPort(8883)
                    .withClientId(clientId)
                    .withCertificateAuthorityFromPath(null, caPath)
                    .withCleanSession(true)
                    .withProtocolOperationTimeoutMs(60000)
                    .build();

            // Connect to AWS IoT
            connection.connect().get();
            System.out.println("Connected to AWS IoT Core.");

            // Step 2: Prepare JSON payload dynamically
            Gson gson = new Gson();
            JsonObject jsonPayload = new JsonObject();

//            jsonPayload.addProperty("message_Id", messageId);
//            jsonPayload.addProperty("ipaddress:port", ipAddress);
//            jsonPayload.addProperty("operation", operation);
//            jsonPayload.addProperty("meter_Id", meterId);
//            jsonPayload.addProperty("meter_type_Id", meterTypeId);

//            JsonElement meterListJson = gson.toJsonTree(meterMaps); // Convert List<MeterMap> to JSON
//            jsonPayload.add("meters", meterListJson);
            

            // Add Meter details for all operations
//            if (operation.equals("read")) {
//                JsonArray meterArray = new JsonArray();
//                JsonObject meterObject = new JsonObject();
//                meterObject.addProperty("meter_Id", meterId);
//                meterObject.addProperty("meter_type_Id", meterTypeId);
//                meterArray.add(meterObject);
//                jsonPayload.add("Meter", meterArray); // Add nested array
//            }
//else {
//                jsonPayload.addProperty("meter_Id", meterId);
//                jsonPayload.addProperty("meter_type_Id", meterTypeId);
//            }
            // Add Eb and DG load for setLoad operation
//            if (operation.equals("setLoad") && ebLoad != null && dgLoad != null) {
//                jsonPayload.addProperty("Eb load", ebLoad);
//                jsonPayload.addProperty("DG load", dgLoad);
//            }
            // Step 3: Serialize JSON
            String jsonString = "{\n" +
    "  \"operationCode\": \"read\",\n" +
    "  \"ipAddress\": \"192.168.0.100\",\n" +
    "  \"port\": 100,\n" +
    "  \"meterRequests\": [\n" +
    "    {\n" +
    "      \"meterTypeId\": \"9\",\n" +
    "      \"slaveId\": 170\n" +
    "    }\n" +
    "  ]\n" +
    "}";
//gson.toJson(jsonPayload);

            // Step 4: Publish the JSON to the specified topic
            MqttMessage message = new MqttMessage(
                    topic,
                    jsonString.getBytes(StandardCharsets.UTF_8),
                    QualityOfService.AT_LEAST_ONCE,
                    false
            );

            CompletableFuture<Integer> published = connection.publish(message);
            published.get(); // Wait for publish confirmation
            System.out.println("Message published to topic: " + topic);

            // Step 5: Disconnect
            connection.disconnect().get();
            System.out.println("Disconnected from AWS IoT Core.");
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

//    boolean result = publishMessage();
}
