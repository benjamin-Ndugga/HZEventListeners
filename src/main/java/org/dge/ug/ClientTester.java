
package org.dge.ug;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Benjamin E Ndugga
 */
public class ClientTester {

    private static final Logger LOGGER = Logger.getLogger(ClientTester.class.getName());

   
     
    
    public static void main(String[] args) {
        
        HazelcastInstance client = connectToHzInstance();
        IMap<String, Integer> map = client.getMap("map1");
        
        //map.put("key1",1,5,TimeUnit.SECONDS);
        map.put("key2",3,20,TimeUnit.SECONDS);
        
        client.shutdown();
        
    }
    
     private static  HazelcastInstance connectToHzInstance() {

        ClientConfig clientConfig = new ClientConfig();

        clientConfig.setGroupConfig(new GroupConfig("dev", "dev-pass"));
        ClientNetworkConfig networkConfig = clientConfig.getNetworkConfig();

        LOGGER.log(Level.INFO, "CONNECTING TO HZ-INSTANCE");

        networkConfig.addAddress("localhost:6000");

        networkConfig.setSmartRouting(true);
        networkConfig.setConnectionTimeout(500);
        networkConfig.setConnectionAttemptPeriod(250);
        networkConfig.setConnectionAttemptLimit(1);
        clientConfig.setNetworkConfig(networkConfig);

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        LOGGER.log(Level.INFO, "CONNECTED-TO | {0}", client.getName());

        return client;
    }
    
}
