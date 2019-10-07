package org.dge.ug;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Benjamin E Ndugga
 */
public class HazelcastInstanceServer {

    private static final Logger LOGGER = Logger.getLogger(HazelcastInstanceServer.class.getName());

    private static final int CACHE_PORT = 6000;
    private static HazelcastInstance instance;

    public static void main(String[] args) {
        startServer();
    }

    public static void startServer() {
        LOGGER.log(Level.INFO, "STARTING-UP-INSTANCE");

        Config config = new Config();

        config.setGroupConfig(new GroupConfig("dev", "dev-pass"));
        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.setPort(CACHE_PORT);
        JoinConfig join = networkConfig.getJoin();

        MulticastConfig multicastConfig = new MulticastConfig();
        multicastConfig.setEnabled(false);
        join.setMulticastConfig(multicastConfig);
        networkConfig.setJoin(join);

        TcpIpConfig tcpIpConfig = new TcpIpConfig();
        tcpIpConfig.setEnabled(true);

        tcpIpConfig.addMember("localhost");
        join.setTcpIpConfig(tcpIpConfig);
        config.setNetworkConfig(networkConfig);

        instance = Hazelcast.newHazelcastInstance(config);

        IMap<String, Integer> map = instance.getMap("map1");
        map.addEntryListener(new SessionMapListener(), true);
        
    }

    public void shutdownInstance() {
        LOGGER.log(Level.INFO, "SHUTTING-DOWN-INSTANCE");
        instance.shutdown();
    }

}
