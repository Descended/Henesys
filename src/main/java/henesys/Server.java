package henesys;

import henesys.connection.crypto.MapleCrypto;
import henesys.connection.netty.ChannelAcceptor;
import henesys.connection.netty.ChannelHandler;
import henesys.connection.netty.ChatAcceptor;
import henesys.connection.netty.LoginAcceptor;
import henesys.world.Channel;
import henesys.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Properties;

public class Server extends Properties {

    private static final Server server = new Server();

    Logger log = LogManager.getRootLogger();

    private List<World> worldList = ServerConfig.WORLDS;

    public static Server getInstance() {
        return server;
    }

    private void init(String[] args) throws Exception {
        log.info("Starting Henesys server...");
        MapleCrypto.initialize(ServerConstants.VERSION);
        ChannelHandler.initHandlers(false);
        new Thread(new LoginAcceptor()).start();
        new Thread(new ChatAcceptor()).start();
        for (World world : getWorlds()) {
            for (Channel channel : world.getChannels()) {
                ChannelAcceptor ca = new ChannelAcceptor(channel);
                new Thread(ca).start();
            }
        }

    }

    public List<World> getWorlds() {
        return worldList;
    }

    public static void main(String[] args) throws Exception {
        Server.getInstance().init(args);
    }
}
