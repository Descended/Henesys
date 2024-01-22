package henesys.life;

import henesys.client.character.Char;
import henesys.loaders.containers.ReactorInfo;
import henesys.util.Position;
import henesys.world.field.Field;
import henesys.world.field.Foothold;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Created on 4/21/2018.
 */
public class Reactor extends Life {

    private byte state;
    private String name = "";
    private int ownerID;
    private int properEventIdx;
    private int reactorTime;
    private boolean phantomForest;
    private int hitCount;

    public Reactor(int templateId) {
        super(templateId);
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public void increaseState() {
        this.state++;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getProperEventIdx() {
        return properEventIdx;
    }

    public void setProperEventIdx(int properEventIdx) {
        this.properEventIdx = properEventIdx;
    }

    public void setReactorTime(int reactorTime) {
        this.reactorTime = reactorTime;
    }

    public int getReactorTime() {
        return reactorTime;
    }

    public void setPhantomForest(boolean phantomForest) {
        this.phantomForest = phantomForest;
    }

    public boolean isPhantomForest() {
        return phantomForest;
    }

    public void init() {
        ReactorInfo ri = ReactorData.getReactorInfoByID(getTemplateId());
        setState((byte) 0);
        setName(ri.getViewName());
        setPosition(getHomePosition());
    }

    @Override
    public void broadcastSpawnPacket(Char onlyChar) {
        init();
        getField().broadcastPacket(ReactorPool.reactorEnterField(this));
    }

    @Override
    public void broadcastLeavePacket() {
        Field field = getField();
        field.broadcastPacket(ReactorPool.reactorLeaveField(this));
        if (field.isChannelField()) {
            Reactor reactor = (Reactor) deepCopy();
            reactor.init();
            EventManager.addEvent(() -> field.spawnLife(reactor, null), 5, TimeUnit.SECONDS);
        }
    }

    public Life deepCopy() {
        Reactor copy = new Reactor(getTemplateId());
        copy.setLifeType(getLifeType());
        copy.setX(getX());
        copy.setY(getY());
        copy.setMobTime(getMobTime());
        copy.setFlip(isFlip());
        copy.setLimitedName(getLimitedName());
        copy.setPosition(getPosition().deepCopy());
        copy.setHomePosition(getPosition().deepCopy());
        return copy;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public void incHitCount() {
        setHitCount(getHitCount() + 1);
    }

    public void die(boolean drops) { //idk sorry
        getField().removeLife(this);
        if (drops) {
            dropDrops();
        }
    }

    public void dropDrops() {
        int fhID = getFh();
        if (fhID == 0) {
            Position pos = getPosition();
            pos.setY(pos.getY());
            Foothold fhBelow = getField().findFootHoldBelow(pos);
            if (fhBelow != null) {
                fhID = fhBelow.getId();
            }
        }
        Set<DropInfo> dropInfoSet = ReactorData.getReactorInfoByID(getTemplateId()).getDrops();
        getField().drop(dropInfoSet, getField().getFootholdById(fhID), getPosition(), ownerID, 100,
                100, Collections.emptyList());
    }
}
