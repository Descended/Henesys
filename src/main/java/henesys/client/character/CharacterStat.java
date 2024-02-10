package henesys.client.character;

import henesys.connection.OutPacket;
import henesys.constants.JobConstants;
import henesys.enums.Stat;

public class CharacterStat {

    private int id;
    private int characterId;
    private String name;
    private byte gender;
    private byte skin;
    private int face;
    private int hair;
    private byte level;
    private short job;
    private short str;
    private short dex;
    private short intt;
    private short luk;
    private int hp;
    private int maxHp;
    private int mp;
    private int maxMp;
    private short ap;
    private short sp;
    private int exp;
    private short fame;

    private int tempExp;
    private int fieldId;
    private int fieldPortal;
    private int playTime;
    private short subJob;

    private int money;

    private ExtendSP extendSP;


    public CharacterStat() {
        extendSP = new ExtendSP(7);
    }

    public CharacterStat(String name, byte gender, byte skin, int face, int hair) {
        this.name = name;
        this.gender = gender;
        this.skin = skin;
        this.face = face;
        this.hair = hair;
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(getCharacterId());
        outPacket.encodeString(getName(), 13);

        outPacket.encodeByte(getGender());
        outPacket.encodeByte(getSkin());
        outPacket.encodeInt(getFace());
        outPacket.encodeInt(getHair());

        outPacket.encodeLong(0);
        outPacket.encodeLong(0);
        outPacket.encodeLong(0);

        outPacket.encodeByte(getLevel());
        outPacket.encodeShort(getJob());
        outPacket.encodeShort(getStr());
        outPacket.encodeShort(getDex());
        outPacket.encodeShort(getIntt());
        outPacket.encodeShort(getLuk());
        outPacket.encodeInt(getHp());
        outPacket.encodeInt(getMaxHp());
        outPacket.encodeInt(getMp());
        outPacket.encodeInt(getMaxMp());

        outPacket.encodeShort(getAp());
        if (JobConstants.isExtendSpJob(getJob())) {
            getExtendSP().encode(outPacket);
        } else {
            outPacket.encodeShort(getSp());
        }

        outPacket.encodeInt(getExp());
        outPacket.encodeShort(getFame());

        outPacket.encodeInt(getTempExp());
        outPacket.encodeInt(getFieldId());
        outPacket.encodeByte(getFieldPortal());
        outPacket.encodeInt(getPlayTime());
        outPacket.encodeShort(getSubJob());

    }

    /**
     * Adds a Stat to this Char.
     *
     * @param charStat which Stat to add
     * @param amount   the amount of Stat to add
     */
    public void addStat(Stat charStat, int amount) {
        setStat(charStat, getStat(charStat) + amount);
    }

    /**
     * Gets a raw Stat from this Char, unaffected by things such as equips and skills.
     *
     * @param charStat The requested Stat
     * @return the requested stat's value
     */
    public int getStat(Stat charStat) {
        switch (charStat) {
            case str:
                return getStr();
            case dex:
                return getDex();
            case inte:
                return getIntt();
            case luk:
                return getLuk();
            case hp:
                return getHp();
            case mhp:
                return getMaxHp();
            case mp:
                return getMp();
            case mmp:
                return getMaxMp();
            case ap:
                return getAp();
            case level:
                return getLevel();
            case skin:
                return getSkin();
            case face:
                return getFace();
            case hair:
                return getHair();
            case pop:
                return getFame();
            case subJob:
                return getSubJob();
        }
        return -1;
    }
    public void setStat(Stat charStat, int amount) {
        switch (charStat) {
            case str:
                setStr((short) amount);
                break;
            case dex:
                setDex((short) amount);
                break;
            case inte:
                setInt((short) amount);
                break;
            case luk:
                setLuk((short) amount);
                break;
            case hp:
                setHp(amount);
                break;
            case mhp:
                setMaxHp(amount);
                break;
            case mp:
                setMp(amount);
                break;
            case mmp:
                setMaxMp(amount);
                break;
            case ap:
                setAp((short) amount);
                break;
            case level:
                setLevel((byte) amount);
                break;
            case skin:
                setSkin((byte) amount);
                break;
            case face:
                setFace(amount);
                break;
            case hair:
                setHair(amount);
                break;
            case pop:
                setFame((short) amount);
                break;
        }
    }

    public ExtendSP getExtendSP() {
        return extendSP;
    }

    public void setExtendSP(ExtendSP extendSP) {
        this.extendSP = extendSP;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public byte getSkin() {
        return skin;
    }

    public void setSkin(byte skin) {
        this.skin = skin;
    }

    public int getFace() {
        return face;
    }

    public void setFace(int face) {
        this.face = face;
    }

    public int getHair() {
        return hair;
    }

    public void setHair(int hair) {
        this.hair = hair;
    }

    public byte getLevel() {
        return level;
    }

    public void setLevel(byte level) {
        this.level = level;
    }

    public short getJob() {
        return job;
    }

    public void setJob(short job) {
        this.job = job;
    }

    public short getStr() {
        return str;
    }

    public void setStr(short str) {
        this.str = str;
    }

    public short getDex() {
        return dex;
    }

    public void setDex(short dex) {
        this.dex = dex;
    }

    public short getIntt() {
        return intt;
    }

    public void setInt(short intt) {
        this.intt = intt;
    }

    public short getLuk() {
        return luk;
    }

    public void setLuk(short luk) {
        this.luk = luk;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getMp() {
        return mp;
    }

    public void setMp(int mp) {
        this.mp = mp;
    }

    public int getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(int maxMp) {
        this.maxMp = maxMp;
    }

    public short getAp() {
        return ap;
    }

    public void setAp(short ap) {
        this.ap = ap;
    }

    public short getSp() {
        return sp;
    }

    public void setSp(short sp) {
        this.sp = sp;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public short getFame() {
        return fame;
    }

    public void setFame(short fame) {
        this.fame = fame;
    }

    public int getTempExp() {
        return tempExp;
    }

    public void setTempExp(int tempExp) {
        this.tempExp = tempExp;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public int getFieldPortal() {
        return fieldPortal;
    }

    public void setFieldPortal(int fieldPortal) {
        this.fieldPortal = fieldPortal;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public short getSubJob() {
        return subJob;
    }

    public void setSubJob(short subJob) {
        this.subJob = subJob;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIntt(short intt) {
        this.intt = intt;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
