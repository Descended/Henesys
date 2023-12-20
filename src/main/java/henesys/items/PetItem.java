package henesys.items;

import henesys.connection.OutPacket;
import henesys.util.FileTime;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created on 4/14/2018.
 */
public class PetItem extends Item {
    private final Logger log = LogManager.getLogger(PetItem.class);

    private String name;
    private byte level;
    private short tameness;
    private byte repleteness; // hungry thing
    private short petAttribute;
    private int petSkill;
    private FileTime dateDead;
    private int remainLife;
    private short attribute;
    private byte activeState;
    private int autoBuffSkill;
    private int petHue;
    private short giantRate;

    @Override
    public Type getType() {
        return Type.PET;
    }

    public void encode(OutPacket outPacket) {
        super.encode(outPacket);
        outPacket.encodeString(getName(), 13);
        outPacket.encodeByte(getLevel());
        outPacket.encodeShort(getTameness() + 1);
        outPacket.encodeByte(getRepleteness());
        outPacket.encodeFT(getDateDead()); // 0 = no date dead
        outPacket.encodeShort(getPetAttribute());
        outPacket.encodeShort(getPetSkill());
        outPacket.encodeInt(getRemainLife());
        outPacket.encodeShort(getAttribute());
        outPacket.encodeByte(getActiveState());
        outPacket.encodeInt(getAutoBuffSkill());
        outPacket.encodeInt(getPetHue());
        outPacket.encodeShort(getGiantRate());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public short getTameness() {
        return tameness;
    }

    public void setTameness(short tameness) {
        this.tameness = tameness;
    }

    public byte getRepleteness() {
        return repleteness;
    }

    public void setRepleteness(byte repleteness) {
        this.repleteness = repleteness;
    }

    public short getPetAttribute() {
        return petAttribute;
    }

    public void setPetAttribute(short petAttribute) {
        this.petAttribute = petAttribute;
    }

    public int getPetSkill() {
        return petSkill;
    }

    public void setPetSkill(int petSkill) {
        this.petSkill = petSkill;
    }

    public FileTime getDateDead() {
        return dateDead;
    }

    public void setDateDead(FileTime dateDead) {
        this.dateDead = dateDead;
    }

    public int getRemainLife() {
        return remainLife;
    }

    public void setRemainLife(int remainLife) {
        this.remainLife = remainLife;
    }

    public short getAttribute() {
        return attribute;
    }

    public void setAttribute(short attribute) {
        this.attribute = attribute;
    }

    public byte getActiveState() {
        return activeState;
    }

    public void setActiveState(byte activeState) {
        this.activeState = activeState;
    }

    public int getAutoBuffSkill() {
        return autoBuffSkill;
    }

    public void setAutoBuffSkill(int autoBuffSkill) {
        this.autoBuffSkill = autoBuffSkill;
    }

    public int getPetHue() {
        return petHue;
    }

    public void setPetHue(int petHue) {
        this.petHue = petHue;
    }

    public short getGiantRate() {
        return giantRate;
    }

    public void setGiantRate(short giantRate) {
        this.giantRate = giantRate;
    }

}
