package henesys.world;

import henesys.ServerConstants;
import henesys.client.Account;
import henesys.client.Client;
import henesys.client.character.Char;
import henesys.util.Util;
import henesys.util.container.Tuple;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created on 11/2/2017.
 */
public class Channel {
    //CHANNELITEM struct
    private final int port;
    private final String name;
    private final int worldId;
    private final int channelId;
    private boolean adultChannel;
    private Map<Integer, Tuple<Byte, Client>> transfers;
    private Map<Integer, Char> chars = new HashMap<>();
    public final int MAX_SIZE = 1000;
    private Channel(String name, World world, int channelId, boolean adultChannel) {
        this.name = name;
        this.worldId = world.getWorldId();
        this.channelId = channelId;
        this.adultChannel = adultChannel;
        this.port = ServerConstants.LOGIN_PORT + 100 + channelId;
        this.transfers = new HashMap<>();
    }

    public Channel(World world, int channelId) {
        this(world.getName() + "-" + channelId, world, channelId, false);
    }

    public Channel(String worldName, int worldId, int channelId) {
        this.name = worldName + "-" + channelId;
        this.worldId = worldId;
        this.channelId = channelId;
        this.adultChannel = false;
        this.port = ServerConstants.LOGIN_PORT + (100 * worldId) + channelId;
        this.transfers = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public int getGaugePx() {
        return Math.max(1, (chars.size() * 64) / MAX_SIZE);
    }

    public int getWorldId() {
        return worldId;
    }

    public int getChannelId() {
        return channelId;
    }

    public boolean isAdultChannel() {
        return adultChannel;
    }

    public void setAdultChannel(boolean adultChannel) {
        this.adultChannel = adultChannel;
    }

    public int getPort() {
        return port;
    }

    public void addClientInTransfer(byte channelId, int characterId, Client client) {
        getTransfers().put(characterId, new Tuple<>(channelId, client));
    }

    public Map<Integer, Tuple<Byte, Client>> getTransfers() {
        return transfers;
    }

    public void setTransfers(Map<Integer, Tuple<Byte, Client>> transfers) {
        this.transfers = transfers;
    }

    public void removeClientFromTransfer(int characterId) {
        getTransfers().remove(characterId);
    }
}
