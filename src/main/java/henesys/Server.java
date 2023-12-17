package henesys;

import henesys.connection.crypto.MapleCrypto;
import henesys.connection.netty.ChatAcceptor;
import henesys.connection.netty.LoginAcceptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class Server extends Properties {

    private static final Server server = new Server();

    static Logger log = LogManager.getRootLogger();

    public static Server getInstance() {
        return server;
    }

    public static void main(String[] args) {
        log.info("Starting Henesys server...");
        MapleCrypto.initialize(ServerConstants.VERSION);
        new Thread(new LoginAcceptor()).start();
        new Thread(new ChatAcceptor()).start();

    }
}
