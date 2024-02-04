package henesys.world;

import henesys.ServerConstants;
import henesys.client.Account;
import henesys.client.Client;
import henesys.client.character.Char;
import henesys.loaders.FieldData;
import henesys.util.Util;
import henesys.util.container.Tuple;
import henesys.world.field.Field;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    private List<Field> fields;

    private Map<Integer, Tuple<Byte, Client>> transfers;
    private Map<Integer, Char> chars = new ConcurrentHashMap<>();
    public final int MAX_SIZE = 1000;
    public Channel(String worldName, int worldId, int channelId) {
        this.name = worldName + "-" + channelId;
        this.worldId = worldId;
        this.channelId = channelId;
        this.adultChannel = false;
        this.port = ServerConstants.LOGIN_PORT + (100 * worldId ) + channelId;
        this.fields = new CopyOnWriteArrayList<>();
        this.transfers = new ConcurrentHashMap<>();
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
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

    private Field createAndReturnNewField(int id) {
        Field newField = FieldData.getFieldCopyById(id);
        if (newField != null) {
            newField.setChannelField(true);
            newField.setChannel(getChannelId());
            getFields().add(newField);
        }
        return newField;
    }

    /**
     * Gets a {@link Field} corresponding to a given ID. If it doesn't exist, creates one.
     *
     * @param id The map ID of the field.
     * @return The (possibly newly created) Field.
     */
    public Field getField(int id) {
        for (Field field : getFields()) {
            if (field.getId() == id) {
                return field;
            }
        }
        return createAndReturnNewField(id);
    }

    public void addChar(Char chr) {
        getChars().put(chr.getId(), chr);
    }
    public void removeChar(Char chr) {
        getChars().remove(chr.getId());
    }

    public Map<Integer, Char> getChars() {
        return chars;
    }
}
