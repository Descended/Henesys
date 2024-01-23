package henesys.world.field;

import henesys.client.character.Char;
import henesys.connection.OutPacket;
import henesys.connection.packet.UserPool;
import henesys.constants.FieldConstants;
import henesys.constants.GameConstants;
import henesys.enums.FieldType;
import henesys.life.Life;
import henesys.life.drop.Drop;
import henesys.life.drop.DropInfo;
import henesys.util.Position;
import henesys.util.Rect;
import henesys.util.Util;
import henesys.util.container.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created on 12/14/2017.
 */
public class Field {
    private static final Logger log = LogManager.getLogger(Field.class);
    private Rect rect;
    private int vrTop, vrLeft, vrBottom, vrRight;
    private double mobRate;
    private int id;
    private FieldType fieldType;
    private long fieldLimit;
    private int returnMap, forcedReturn, createMobInterval, timeOut, timeLimit, lvLimit, lvForceMove;
    private int consumeItemCoolTime, link;
    private boolean town, swim, fly, reactorShuffle, expeditionOnly, partyOnly, needSkillForFly;
    private Set<Portal> portals;
    private Set<Foothold> footholds;
    private Map<Integer, Life> lifes;
    private List<Char> chars;
    private Map<Life, Char> lifeToControllers;
    private String onFirstUserEnter = "", onUserEnter = "";
    private int fixedMobCapacity;
    private int objectIDCounter = 1000000;
    private boolean userFirstEnter = false;
    private String fieldScript = "";
    //    private List<OpenGate> openGateList = new ArrayList<>();
//    private List<TownPortal> townPortalList = new ArrayList<>();
    private boolean isChannelField;
    private Clock clock;
    private int channel;
    private Map<String, Object> properties;
    private boolean changeToChannelOnLeave;
    private boolean dropsDisabled;
    private ScheduledFuture<?> generateMobsEvent;

    public Field(int fieldID) {
        this.id = fieldID;
        this.rect = new Rect();
        this.portals = new HashSet<>();
        this.footholds = new HashSet<>();
        this.lifes = new ConcurrentHashMap<>();
        this.chars = new CopyOnWriteArrayList<>();
        this.lifeToControllers = new HashMap<>();
        this.fixedMobCapacity = GameConstants.DEFAULT_FIELD_MOB_CAPACITY; // default
        this.properties = new HashMap<>();
        dropsDisabled = false;
    }

    public void startFieldScript() {
//        String script = getFieldScript();
//        if (!"".equalsIgnoreCase(script)) {
//            log.debug("Starting field script {}.", script);
//            scriptManagerImpl.startScript(getId(), script, ScriptType.Field);
//        }
    }

    public void setDropsDisabled(boolean val) {
        dropsDisabled = val;
    }

    public boolean getDropsDisabled() {
        return dropsDisabled;
    }

    public Rect getRect() {
        return rect;
    }

    public void setRect(Rect rect) {
        this.rect = rect;
    }

    public int getVrTop() {
        return vrTop;
    }

    public void setVrTop(int vrTop) {
        this.vrTop = vrTop;
    }

    public int getVrLeft() {
        return vrLeft;
    }

    public void setVrLeft(int vrLeft) {
        this.vrLeft = vrLeft;
    }

    public int getVrBottom() {
        return vrBottom;
    }

    public void setVrBottom(int vrBottom) {
        this.vrBottom = vrBottom;
    }

    public int getVrRight() {
        return vrRight;
    }

    public void setVrRight(int vrRight) {
        this.vrRight = vrRight;
    }

    public int getHeight() {
        return getVrTop() - getVrBottom();
    }

    public int getWidth() {
        return getVrRight() - getVrLeft();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public long getFieldLimit() {
        return fieldLimit;
    }

    public void setFieldLimit(long fieldLimit) {
        this.fieldLimit = fieldLimit;
    }

    public Set<Portal> getPortals() {
        return portals;
    }

    public void setPortals(Set<Portal> portals) {
        this.portals = portals;
    }

    public void addPortal(Portal portal) {
        getPortals().add(portal);
    }

    public int getReturnMap() {
        return returnMap;
    }

    public void setReturnMap(int returnMap) {
        this.returnMap = returnMap;
    }

    public int getForcedReturn() {
        return forcedReturn;
    }

    public void setForcedReturn(int forcedReturn) {
        this.forcedReturn = forcedReturn;
    }

    public double getMobRate() {
        return mobRate;
    }

    public void setMobRate(double mobRate) {
        this.mobRate = mobRate;
    }

    public int getCreateMobInterval() {
        return createMobInterval;
    }

    public void setCreateMobInterval(int createMobInterval) {
        this.createMobInterval = createMobInterval;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public int getLvLimit() {
        return lvLimit;
    }

    public void setLvLimit(int lvLimit) {
        this.lvLimit = lvLimit;
    }

    public int getLvForceMove() {
        return lvForceMove;
    }

    public void setLvForceMove(int lvForceMove) {
        this.lvForceMove = lvForceMove;
    }

    public int getConsumeItemCoolTime() {
        return consumeItemCoolTime;
    }

    public void setConsumeItemCoolTime(int consumeItemCoolTime) {
        this.consumeItemCoolTime = consumeItemCoolTime;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }

    public boolean isTown() {
        return town;
    }

    public void setTown(boolean town) {
        this.town = town;
    }

    public boolean isSwim() {
        return swim;
    }

    public void setSwim(boolean swim) {
        this.swim = swim;
    }

    public boolean isFly() {
        return fly;
    }

    public void setFly(boolean fly) {
        this.fly = fly;
    }

    public boolean isReactorShuffle() {
        return reactorShuffle;
    }

    public void setReactorShuffle(boolean reactorShuffle) {
        this.reactorShuffle = reactorShuffle;
    }

    public boolean isExpeditionOnly() {
        return expeditionOnly;
    }

    public void setExpeditionOnly(boolean expeditionONly) {
        this.expeditionOnly = expeditionONly;
    }

    public boolean isPartyOnly() {
        return partyOnly;
    }

    public void setPartyOnly(boolean partyOnly) {
        this.partyOnly = partyOnly;
    }

    public boolean isNeedSkillForFly() {
        return needSkillForFly;
    }

    public void setNeedSkillForFly(boolean needSkillForFly) {
        this.needSkillForFly = needSkillForFly;
    }

    public String getOnFirstUserEnter() {
        return onFirstUserEnter;
    }

    public void setOnFirstUserEnter(String onFirstUserEnter) {
        this.onFirstUserEnter = onFirstUserEnter;
    }

    public String getOnUserEnter() {
        return onUserEnter;
    }

    public void setOnUserEnter(String onUserEnter) {
        this.onUserEnter = onUserEnter;
    }

    public Portal getPortalByName(String name) {
        return Util.findWithPred(getPortals(), portal -> portal.getName().equals(name));
    }

    public Portal getPortalByID(int id) {
        return Util.findWithPred(getPortals(), portal -> portal.getId() == id);
    }

    public Foothold findFootHoldBelow(Position pos) {
        Set<Foothold> footholds = getFootholds().stream().filter(fh -> fh.getX1() <= pos.getX() && fh.getX2() >= pos.getX()).collect(Collectors.toSet());
        Foothold res = null;
        int lastY = Integer.MAX_VALUE;
        for (Foothold fh : footholds) {
            int y = fh.getYFromX(pos.getX());
            if (res == null && y >= pos.getY()) {
                res = fh;
                lastY = y;
            } else {
                if (y < lastY && y >= pos.getY()) {
                    res = fh;
                    lastY = y;
                }
            }
        }
        return res;
    }

    public Foothold findFootholdBelowPortal(int portalId) {
        Portal port = getPortalByID(portalId);
        return findFootHoldBelow(new Position(port.getX(), port.getY() + FieldConstants.PORTAL_Y_OFFSET));  // TODO: Constants
    }

    public Tuple<Foothold, Foothold> getMinMaxNonWallFH() {
        Set<Foothold> footholds = getFootholds().stream().filter(fh -> !fh.isWall()).collect(Collectors.toSet());
        Foothold left = footholds.iterator().next(), right = footholds.iterator().next(); // retun vals

        for (Foothold fh : footholds) {
            if (fh.getX1() < left.getX1()) {
                left = fh;
            } else if (fh.getX1() > right.getX1()) {
                right = fh;
            }
        }
        return new Tuple<>(left, right);
    }

    public void broadcastPacket(OutPacket outPacket, Char exceptChr) {
        List<Char> chars = getChars().stream().filter(chr -> !chr.equals(exceptChr)).toList();
        if (chars.size() - 1 > 0) {
            outPacket.retain(chars.size() - 1);
        }
        chars.forEach(chr -> chr.getClient().write(outPacket));
        if (chars.isEmpty()) {
            outPacket.release();
        }
    }

    public Set<Foothold> getFootholds() {
        return footholds;
    }

    public void setFootholds(Set<Foothold> footholds) {
        this.footholds = footholds;
    }

    public void addFoothold(Foothold foothold) {
        getFootholds().add(foothold);
    }

    public void setFixedMobCapacity(int fixedMobCapacity) {
        this.fixedMobCapacity = fixedMobCapacity;
    }

    public int getFixedMobCapacity() {
        return fixedMobCapacity;
    }

    public Map<Integer, Life> getLifes() {
        return lifes;
    }

    public String getFieldScript() {
        return fieldScript;
    }

    public void setFieldScript(String fieldScript) {
        this.fieldScript = fieldScript;
    }

    public void setLifes(Map<Integer, Life> lifes) {
        this.lifes = lifes;
    }

    public List<Char> getChars() {
        return chars;
    }

    public void setChars(List<Char> chars) {
        this.chars = chars;
    }

    public Map<Life, Char> getLifeToControllers() {
        return lifeToControllers;
    }

    public void setLifeToControllers(Map<Life, Char> lifeToControllers) {
        this.lifeToControllers = lifeToControllers;
    }

    public int getObjectIDCounter() {
        return objectIDCounter;
    }

    public void setObjectIDCounter(int objectIDCounter) {
        this.objectIDCounter = objectIDCounter;
    }

    public boolean isUserFirstEnter() {
        return userFirstEnter;
    }

    public void setUserFirstEnter(boolean userFirstEnter) {
        this.userFirstEnter = userFirstEnter;
    }

    public boolean isChannelField() {
        return isChannelField;
    }

    public void setChannelField(boolean channelField) {
        isChannelField = channelField;
    }

    public Clock getClock() {
        return clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public boolean isChangeToChannelOnLeave() {
        return changeToChannelOnLeave;
    }

    public void setChangeToChannelOnLeave(boolean changeToChannelOnLeave) {
        this.changeToChannelOnLeave = changeToChannelOnLeave;
    }

    public boolean isDropsDisabled() {
        return dropsDisabled;
    }

    public ScheduledFuture<?> getGenerateMobsEvent() {
        return generateMobsEvent;
    }

    public void setGenerateMobsEvent(ScheduledFuture<?> generateMobsEvent) {
        this.generateMobsEvent = generateMobsEvent;
    }

    public Foothold getFootholdById(int fh) {
        return getFootholds().stream().filter(f -> f.getId() == fh).findFirst().orElse(null);
    }

    public void addLife(Life life) {
        // TODO
    }

    public void removeLife(Life life) {
        // TODO
    }

    public void drop(Drop drop, Position posFrom, Position posTo, boolean ignoreTradability) {
        // TODO
    }

    public void drop(Set<DropInfo> dropInfos, Foothold fh, Position position, int ownerID, int mesoRate, int dropRate, List<Char> playersEligibleForDrops) {
        // TODO
    }

    public void addChar(Char chr) {
        if (!getChars().contains(chr)) {
            getChars().add(chr);
        }
        broadcastPacket(UserPool.userEnterField(chr), chr);
    }

    public int getNewObjectID() {
        return objectIDCounter++;
    }

}
