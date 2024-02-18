package henesys.skills;


import henesys.connection.OutPacket;
import henesys.util.FileTime;

/**
 * Created on 12/20/2017.
 */
public class Skill {

    private int id;
    private int charId;
    private int skillId;
    private int rootId;
    private int maxLevel;
    private int currentLevel;
    private int masterLevel;

    public int getSkillId() {
        return skillId;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }

    public int getRootId() {
        return rootId;
    }

    public void setRootId(int rootId) {
        this.rootId = rootId;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCharId() {
        return charId;
    }

    public void setCharId(int charId) {
        this.charId = charId;
    }

    public void setMasterLevel(int masterLevel) {
        this.masterLevel = masterLevel;
    }

    public int getMasterLevel() {
        return masterLevel;
    }

    @Override
    public String toString() {
        return "id = " + getSkillId() + ", cur = " + getCurrentLevel() + ", master = " + getMasterLevel();
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getSkillId());
        outPacket.encodeInt(getCurrentLevel());
        outPacket.encodeInt(getMasterLevel());
        outPacket.encodeFT(FileTime.fromType(FileTime.Type.PLAIN_ZERO));
    }
}
