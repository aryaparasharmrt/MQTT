//package lib_download;
//
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//
//public class EurekaClientManual {
//    public static void main(String[] args) throws InterruptedException {
//        String eurekaServerUrl = "http://localhost:8761/eureka/apps/MY-APP";
//        // JSON String for Instance Info
//        String instanceInfo = "{"
//                + "\"instance\": {"
//                + "\"hostName\": \"localhost\","
//                + "\"app\": \"MY-APP\","
//                + "\"ipAddr\": \"127.0.0.1\","
//                + "\"vipAddress\": \"MY-APP\","
//                + "\"secureVipAddress\": \"MY-APP\","
//                + "\"status\": \"UP\","
//                + "\"port\": { \"value\": 8080 },"
//                + "\"dataCenterInfo\": {"
//                + "\"@class\": \"com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo\","
//                + "\"name\": \"MyOwn\""
//                + "}"
//                + "}"
//                + "}";
//
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpPost request = new HttpPost(eurekaServerUrl);
//            request.setHeader("Content-Type", "application/json");
//            request.setEntity(new StringEntity(instanceInfo));
//
//            httpClient.execute(request);
//            System.out.println("Registered successfully with Eureka Server!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        
//      Thread.sleep(2000000);
//    }
//}
//
