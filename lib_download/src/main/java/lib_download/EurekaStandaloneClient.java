//package lib_download;
//
//import com.netflix.appinfo.ApplicationInfoManager;
//import com.netflix.appinfo.EurekaInstanceConfig;
//import com.netflix.appinfo.MyDataCenterInstanceConfig;
//import com.netflix.discovery.DefaultEurekaClientConfig;
//import com.netflix.discovery.DiscoveryManager;
//
//public class EurekaStandaloneClient {
//    public static void main(String[] args) {
//        // Configure Eureka Instance
//        EurekaInstanceConfig instanceConfig = new MyDataCenterInstanceConfig();
//        ApplicationInfoManager applicationInfoManager = new ApplicationInfoManager(instanceConfig, null);
//
//        // Configure Eureka Client
//        DiscoveryManager.getInstance().initComponent(
//            instanceConfig,
//            new DefaultEurekaClientConfig()
//        );
//
//        System.out.println("Eureka Client is registered to Eureka Server!");
//    }
//}
