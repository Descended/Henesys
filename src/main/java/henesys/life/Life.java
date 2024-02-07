package henesys.life;

import henesys.client.character.Char;
import henesys.life.mob.Mob;
import henesys.life.npc.Npc;
import henesys.loaders.MobData;
import henesys.loaders.NpcData;
import henesys.util.Position;
import henesys.world.field.Field;

public class Life {
    private Position position;
    private int objectId = -1;
    protected int cy, fh, templateId, mobTime, rx0, rx1, type, x, y;
    protected boolean flip;
    private String lifeType = "";
    private boolean hide;
    private String limitedName = "";
    private boolean hold;
    private boolean noFoothold;
    private int regenStart;
    private int mobAliveReq;
    private boolean dummy;
    private boolean mobTimeOnDie;
    private boolean notRespawnable;
    private byte moveAction;
    private Field field;
    private Position homePosition;
    private Position vPosition;

    public Life(int templateId) {
        this.templateId = templateId;
        this.position = new Position(0, 0);
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public int getCy() {
        return cy;
    }

    public void setCy(int cy) {
        this.cy = cy;
    }

    public int getFh() {
        return fh;
    }

    public void setFh(int fh) {
        this.fh = fh;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public int getMobTime() {
        return mobTime;
    }

    public void setMobTime(int mobTime) {
        this.mobTime = mobTime;
    }

    public int getRx0() {
        return rx0;
    }

    public void setRx0(int rx0) {
        this.rx0 = rx0;
    }

    public int getRx1() {
        return rx1;
    }

    public void setRx1(int rx1) {
        this.rx1 = rx1;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isFlip() {
        return flip;
    }

    public void setFlip(boolean flip) {
        this.flip = flip;
    }

    public String getLifeType() {
        return lifeType;
    }

    public void setLifeType(String lifeType) {
        this.lifeType = lifeType;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public String getLimitedName() {
        return limitedName;
    }

    public void setLimitedName(String limitedName) {
        this.limitedName = limitedName;
    }

    public boolean isHold() {
        return hold;
    }

    public void setHold(boolean hold) {
        this.hold = hold;
    }

    public boolean isNoFoothold() {
        return noFoothold;
    }

    public void setNoFoothold(boolean noFoothold) {
        this.noFoothold = noFoothold;
    }

    public int getRegenStart() {
        return regenStart;
    }

    public void setRegenStart(int regenStart) {
        this.regenStart = regenStart;
    }

    public int getMobAliveReq() {
        return mobAliveReq;
    }

    public void setMobAliveReq(int mobAliveReq) {
        this.mobAliveReq = mobAliveReq;
    }

    public boolean isDummy() {
        return dummy;
    }

    public void setDummy(boolean dummy) {
        this.dummy = dummy;
    }
    public boolean isMobTimeOnDie() {
        return mobTimeOnDie;
    }

    public void setMobTimeOnDie(boolean mobTimeOnDie) {
        this.mobTimeOnDie = mobTimeOnDie;
    }

    public boolean isNotRespawnable() {
        return notRespawnable;
    }

    public void setNotRespawnable(boolean notRespawnable) {
        this.notRespawnable = notRespawnable;
    }

    public byte getMoveAction() {
        return moveAction;
    }

    public void setMoveAction(byte moveAction) {
        this.moveAction = moveAction;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public Position getHomePosition() {
        return homePosition;
    }

    public void setHomePosition(Position homePosition) {
        this.homePosition = homePosition;
    }

    public Position getvPosition() {
        return vPosition;
    }

    public void setvPosition(Position vPosition) {
        this.vPosition = vPosition;
    }

    public void broadcastSpawnPacket() {
    }

    public void broadcastSpawnPacket(Char onlyChar) {

    }

    public void broadcastLeavePacket() {


    }

    public Mob createMobFromLife() {
        Mob mob = null;
        if ("m".equalsIgnoreCase(getLifeType())) {
            mob = MobData.getMobById(getTemplateId());
            mob.setObjectId(getObjectId());
            mob.setTemplateId(getTemplateId());
            mob.setLifeType(getLifeType());
            mob.setX(getX());
            mob.setY(getY());
            mob.setPosition(new Position(getX(), getY()));
            mob.setMobTime(getMobTime());
            mob.setFlip(isFlip());
            mob.setHide(isHide());
            mob.setFh(getFh());
            mob.setCy(getCy());
            mob.setRx0(getRx0());
            mob.setRx1(getRx1());
            mob.setLimitedName(getLimitedName());
            mob.setHold(isHold());
            mob.setNoFoothold(isNoFoothold());
            mob.setDummy(isDummy());
            mob.setMobTimeOnDie(isMobTimeOnDie());
            mob.setRegenStart(getRegenStart());
            mob.setMobAliveReq(getMobAliveReq());
            mob.setMoveAction(getMoveAction());
        }
        return mob;
    }
    public Npc createNpcFromLife() {
        Npc npc = null;
        if ("n".equalsIgnoreCase(getLifeType())) {
            npc = NpcData.getNpcDeepCopyById(getTemplateId());
            npc.setObjectId(getObjectId());
            npc.setLifeType(getLifeType());
            npc.setX(getX());
            npc.setY(getY());
            npc.setPosition(new Position(getX(), getY()));
            npc.setMobTime(getMobTime());
            npc.setFlip(isFlip());
            npc.setHide(isHide());
            npc.setFh(getFh());
            npc.setCy(getCy());
            npc.setRx0(getRx0());
            npc.setRx1(getRx1());
            npc.setLimitedName(getLimitedName());
            npc.setHold(isHold());
            npc.setNoFoothold(isNoFoothold());
            npc.setDummy(isDummy());
            npc.setMobTimeOnDie(isMobTimeOnDie());
            npc.setRegenStart(getRegenStart());
            npc.setMobAliveReq(getMobAliveReq());
        }
        return npc;
    }

}
