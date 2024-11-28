//package lib_download;
//import org.apache.http.client.methods.HttpPut;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class EurekaHeartbeat {
//    public static void main(String[] args) {
//        String heartbeatUrl = "http://localhost:8761/eureka/apps/MY-APP/localhost";
//
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//                    HttpPut request = new HttpPut(heartbeatUrl);
//                    httpClient.execute(request);
//                    System.out.println("Heartbeat sent to Eureka Server.");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 0, 30 * 1000); // Every 30 seconds
//    }
//}
