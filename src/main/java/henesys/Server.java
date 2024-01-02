package henesys;

import henesys.client.Client;
import henesys.connection.crypto.MapleCrypto;
import henesys.connection.netty.ChannelAcceptor;
import henesys.connection.netty.ChannelHandler;
import henesys.connection.netty.ChatAcceptor;
import henesys.connection.netty.LoginAcceptor;
import henesys.items.Item;
import henesys.loaders.DataClasses;
import henesys.util.Loader;
import henesys.util.Util;
import henesys.util.container.Tuple;
import henesys.world.Channel;
import henesys.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

public class Server extends Properties {

    private static final Server server = new Server();

    private static final Logger log = LogManager.getLogger(Server.class);

    private List<World> worldList = ServerConfig.WORLDS;

    private long uniqueItemIdCounter;

    public static Server getInstance() {
        return server;
    }

    private void init(String[] args) throws Exception {
        log.info("Starting Henesys server...");
        checkAndCreateDat();
        loadWzData();
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

    public void loadWzData() throws IllegalAccessException, InvocationTargetException {
        String datFolder = ServerConstants.DAT_DIR;
        for (Class<?> c : DataClasses.dataClasses) {
            for (Method method : c.getMethods()) {
                String name;
                Loader annotation = method.getAnnotation(Loader.class);
                if (annotation != null) {
                    name = annotation.varName();
                    File file = new File(datFolder, name + ".dat");
                    boolean exists = file.exists();
                    long start = System.currentTimeMillis();
                    method.invoke(c, file, exists);
                    long total = System.currentTimeMillis() - start;
                    if (exists) {
                        log.info(String.format("Took %dms to load from %s", total, file.getName()));
                    } else {
                        log.info(String.format("Took %dms to load using %s", total, method.getName()));
                    }
                }
            }
        }
    }
    public List<World> getWorlds() {
        return worldList;
    }

    public static void main(String[] args) throws Exception {
        Server.getInstance().init(args);
    }

    private void checkAndCreateDat() {
        File file = new File(ServerConstants.DAT_DIR + "/equips");
        boolean exists = file.exists();
        if (!exists) {
            log.info("Dat files cannot be found (at least not the equip dats). All dats will now be generated. This may take a long while.");
            Util.makeDirIfAbsent(ServerConstants.DAT_DIR);
            for (Class<?> c : DataClasses.datCreators) {
                try {
                    Method m = c.getMethod("generateDatFiles");
                    m.invoke(null);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    log.error("Failed to generate DAT files for " + c.getName(), e);
                }
            }
            log.info("Following DAT generation, please restart the server for changes to take effect.");
        }
    }

    public Tuple<Byte, Client> getChannelFromTransfer(int charId, int worldId) {
        for (Channel c : getWorldById(worldId).getChannels()) {
            if (c.getTransfers().containsKey(charId)) {
                return c.getTransfers().get(charId);
            }
        }
        return null;
    }
    public World getWorldById(int id) {
        return Util.findWithPred(getWorlds(), w -> w.getWorldId() == id);
    }

    public synchronized void assignItemIdAndInc(Item item) {
        item.setId(uniqueItemIdCounter);
        incItemIdCounter();
    }

    private synchronized void incItemIdCounter() {
        uniqueItemIdCounter++;
    }
}
