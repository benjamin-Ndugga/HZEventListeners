package org.dge.ug;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MapEvent;
import com.hazelcast.core.MultiMap;
import com.hazelcast.map.listener.EntryEvictedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.MapClearedListener;
import com.hazelcast.map.listener.MapEvictedListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.gde.ug.pojo.UserInput;

/**
 *
 * @author Benjamin E Ndugga
 */
public class SessionMapListener implements EntryRemovedListener<String, Integer>, EntryEvictedListener<String, Integer>, MapEvictedListener, MapClearedListener {

    private static final String CACHE_CUSTOMER_MULTI_MAP_NAME = "customer-details";
    private static final Logger LOGGER = Logger.getLogger(SessionMapListener.class.getName());

    public SessionMapListener() {
        
        LOGGER.log(Level.INFO, "REGISTERED-MAP-LISTENER | {0}", SessionMapListener.class.getName());
    }

    @Override
    public void entryRemoved(EntryEvent<String, Integer> event) {
        LOGGER.log(Level.INFO, "ENTRY-REMOVED >>> {0}", event.getKey());
        processEvent(event.getKey());
    }

    @Override
    public void entryEvicted(EntryEvent<String, Integer> event) {

        LOGGER.log(Level.INFO, "ENTRY-EVICTED >>> {0}", event.getKey());
        processEvent(event.getKey());
    }

    @Override
    public void mapEvicted(MapEvent event) {
        
        LOGGER.log(Level.INFO, "MAP-EVICTED-COUNT >>> {0}", event.getNumberOfEntriesAffected());
        clearAll();
    }

    @Override
    public void mapCleared(MapEvent event) {

        LOGGER.log(Level.INFO, "MAP-CLEARED-COUNT >>> {0}", event.getNumberOfEntriesAffected());
        clearAll();
    }

    private void clearAll() {
        HazelcastInstance client = connectToHzInstance();
        MultiMap<String, UserInput> multiMap = client.getMultiMap(CACHE_CUSTOMER_MULTI_MAP_NAME);
        multiMap.clear();
        client.shutdown();
    }

    private void processEvent(String key) {
        if (key == null) {

            LOGGER.log(Level.INFO, "NO-VALUE-FOUND | {0}", key);

        } else {
            
            LOGGER.log(Level.INFO, "CLEARING-MULTI-MAP KEY >>> | {0}", key);

            HazelcastInstance client = connectToHzInstance();
            MultiMap<String, UserInput> multiMap = client.getMultiMap(CACHE_CUSTOMER_MULTI_MAP_NAME);
            multiMap.remove(key);
            client.shutdown();
        }
    }

    private HazelcastInstance connectToHzInstance() {

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
