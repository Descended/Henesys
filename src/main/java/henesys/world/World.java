package henesys.world;

import java.util.*;

/**
 * Created on 11/2/2017.
 */
public class World {
    //WORLDITEM struct

    private final int worldId;
    private final int worldState;
    private final int worldEventEXP_WSE;
    private final int worldEventDrop_WSE;
    private final int boomUpEventNotice;
    private final String name;
    private String worldEventDescription;
    private final List<Channel> channels;

    private final int expRate;

    private final int mesoRate;

    private final int dropRate;

    public World(int worldId, String name, int worldState, String worldEventDescription, int worldEventEXP_WSE,
                 int worldEventDrop_WSE, int boomUpEventNotice, int amountOfChannels, int expRate, int mesoRate, int dropRate) {
        this.worldId = worldId;
        this.name = name;
        this.worldState = worldState;
        this.worldEventDescription = worldEventDescription;
        this.worldEventEXP_WSE = worldEventEXP_WSE;
        this.worldEventDrop_WSE = worldEventDrop_WSE;
        this.boomUpEventNotice = boomUpEventNotice;
        List<Channel> channelList = new ArrayList<>();
        for (int i = 1; i <= amountOfChannels; i++) {
            channelList.add(new Channel(name, worldId, i));
        }
        this.channels = channelList;
        this.expRate = expRate;
        this.mesoRate = mesoRate;
        this.dropRate = dropRate;
    }

    public int getWorldId() {
        return worldId;
    }

    public String getName() {
        return name;
    }

    public String getWorldEventDescription() {
        return worldEventDescription;
    }

    public void setWorldEventDescription(String worldEventDescription) {
        this.worldEventDescription = worldEventDescription;
    }

    public int getExpRate() {
        return expRate;
    }

    public int getMesoRate() {
        return mesoRate;
    }

    public int getDropRate() {
        return dropRate;
    }

    public List<Channel> getChannels() {
        return channels;
    }

}
