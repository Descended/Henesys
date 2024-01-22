package henesys.life.mob;

import henesys.life.DeathType;
import henesys.life.Life;
import henesys.life.mob.skill.MobSkill;
import henesys.world.field.Foothold;

import java.util.*;

public class Mob extends Life {

    private byte calcDamageIndex = 1;
    private Foothold curFoodhold;
    private ForcedMobStat forcedMobStat;
    private MobTemporaryStat temporaryStat;
    private double fs;
    private int summonType;
    private String mobType = "";
    private String elemAttr = "";
    private int hpTagColor;
    private int hpTagBgcolor;
    private boolean HPgaugeHide;
    private boolean boss;
    private boolean undead;
    private boolean noRegen;
    private boolean invincible;
    private boolean hideName;
    private boolean hideHP;
    private boolean noFlip;
    private boolean publicReward;
    private boolean ignoreFieldOut;
    private boolean noDoom;
    private boolean knockback;
    private boolean removeQuest;
    private int rareItemDropLevel;
    private int hpRecovery;
    private int mpRecovery;
    private int mbookID;
    private int chaseSpeed;
    private int explosiveReward;
    private int flySpeed;
    private int summonEffect;
    private int fixedDamage;
    private int removeAfter;
    private int coolDamageProb;
    private int coolDamage;
    private int firstAttack;

    private int currentAction = -1;
    private int afterAttack = -1;
    private Set<Integer> revives = new HashSet<>();
    private final List<MobSkill> skills = new ArrayList<>();
    private final List<MobSkill> attacks = new ArrayList<>();
    private HashMap<DeathType, Long> reviveRespawnDelays = new HashMap<>(); // for all type of death animations: die1, die2, dieF
    private boolean selfDestruction;
    private boolean escortMob;

    private long hp;
    private long mp;

    public long getHp() {
        return hp;
    }

    public void setHp(long hp) {
        this.hp = hp;
    }

    public long getMp() {
        return mp;
    }

    public void setMp(long mp) {
        this.mp = mp;
    }

    public long getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(long maxHp) {
        this.maxHp = maxHp;
    }

    public long getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(long maxMp) {
        this.maxMp = maxMp;
    }

    private long maxHp;
    private long maxMp;
    public Mob(int templateId) {
        super(templateId);
        forcedMobStat = new ForcedMobStat();
        temporaryStat = new MobTemporaryStat(this);
        calcDamageIndex = 1;
    }

    public byte getCalcDamageIndex() {
        return calcDamageIndex;
    }

    public void setCalcDamageIndex(byte calcDamageIndex) {
        this.calcDamageIndex = calcDamageIndex;
    }

    public Foothold getCurFoodhold() {
        return curFoodhold;
    }

    public void setCurFoodhold(Foothold curFoodhold) {
        this.curFoodhold = curFoodhold;
    }

    public ForcedMobStat getForcedMobStat() {
        return forcedMobStat;
    }

    public void setForcedMobStat(ForcedMobStat forcedMobStat) {
        this.forcedMobStat = forcedMobStat;
    }

    public MobTemporaryStat getTemporaryStat() {
        return temporaryStat;
    }

    public void setTemporaryStat(MobTemporaryStat temporaryStat) {
        this.temporaryStat = temporaryStat;
    }

    public int getSummonType() {
        return summonType;
    }

    public void setSummonType(int summonType) {
        this.summonType = summonType;
    }

    public String getMobType() {
        return mobType;
    }

    public void setMobType(String mobType) {
        this.mobType = mobType;
    }

    public double getFs() {
        return fs;
    }

    public String getElemAttr() {
        return elemAttr;
    }

    public void setElemAttr(String elemAttr) {
        this.elemAttr = elemAttr;
    }

    public void setFs(double fs) {
        this.fs = fs;
    }

    public int getHpTagColor() {
        return hpTagColor;
    }

    public void setHpTagColor(int hpTagColor) {
        this.hpTagColor = hpTagColor;
    }

    public int getHpTagBgcolor() {
        return hpTagBgcolor;
    }

    public void setHpTagBgcolor(int hpTagBgcolor) {
        this.hpTagBgcolor = hpTagBgcolor;
    }

    public boolean isHPgaugeHide() {
        return HPgaugeHide;
    }

    public void setHPgaugeHide(boolean HPgaugeHide) {
        this.HPgaugeHide = HPgaugeHide;
    }

    public boolean isBoss() {
        return boss;
    }

    public void setBoss(boolean boss) {
        this.boss = boss;
    }

    public boolean isUndead() {
        return undead;
    }

    public void setUndead(boolean undead) {
        this.undead = undead;
    }

    public boolean isNoRegen() {
        return noRegen;
    }

    public void setNoRegen(boolean noRegen) {
        this.noRegen = noRegen;
    }

    public boolean isInvincible() {
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public boolean isHideName() {
        return hideName;
    }

    public void setHideName(boolean hideName) {
        this.hideName = hideName;
    }

    public boolean isHideHP() {
        return hideHP;
    }

    public void setHideHP(boolean hideHP) {
        this.hideHP = hideHP;
    }

    public boolean isNoFlip() {
        return noFlip;
    }

    public void setNoFlip(boolean noFlip) {
        this.noFlip = noFlip;
    }

    public boolean isPublicReward() {
        return publicReward;
    }

    public void setPublicReward(boolean publicReward) {
        this.publicReward = publicReward;
    }

    public boolean isIgnoreFieldOut() {
        return ignoreFieldOut;
    }

    public void setIgnoreFieldOut(boolean ignoreFieldOut) {
        this.ignoreFieldOut = ignoreFieldOut;
    }

    public boolean isNoDoom() {
        return noDoom;
    }

    public void setNoDoom(boolean noDoom) {
        this.noDoom = noDoom;
    }

    public boolean isKnockback() {
        return knockback;
    }

    public void setKnockback(boolean knockback) {
        this.knockback = knockback;
    }

    public boolean isRemoveQuest() {
        return removeQuest;
    }

    public void setRemoveQuest(boolean removeQuest) {
        this.removeQuest = removeQuest;
    }

    public int getRareItemDropLevel() {
        return rareItemDropLevel;
    }

    public void setRareItemDropLevel(int rareItemDropLevel) {
        this.rareItemDropLevel = rareItemDropLevel;
    }

    public int getHpRecovery() {
        return hpRecovery;
    }

    public void setHpRecovery(int hpRecovery) {
        this.hpRecovery = hpRecovery;
    }

    public int getMpRecovery() {
        return mpRecovery;
    }

    public void setMpRecovery(int mpRecovery) {
        this.mpRecovery = mpRecovery;
    }

    public int getMbookID() {
        return mbookID;
    }

    public void setMbookID(int mbookID) {
        this.mbookID = mbookID;
    }

    public int getChaseSpeed() {
        return chaseSpeed;
    }

    public void setChaseSpeed(int chaseSpeed) {
        this.chaseSpeed = chaseSpeed;
    }

    public int getExplosiveReward() {
        return explosiveReward;
    }

    public void setExplosiveReward(int explosiveReward) {
        this.explosiveReward = explosiveReward;
    }

    public int getFlySpeed() {
        return flySpeed;
    }

    public void setFlySpeed(int flySpeed) {
        this.flySpeed = flySpeed;
    }

    public int getSummonEffect() {
        return summonEffect;
    }

    public void setSummonEffect(int summonEffect) {
        this.summonEffect = summonEffect;
    }

    public int getFixedDamage() {
        return fixedDamage;
    }

    public void setFixedDamage(int fixedDamage) {
        this.fixedDamage = fixedDamage;
    }

    public int getRemoveAfter() {
        return removeAfter;
    }

    public void setRemoveAfter(int removeAfter) {
        this.removeAfter = removeAfter;
    }

    public int getCoolDamageProb() {
        return coolDamageProb;
    }

    public void setCoolDamageProb(int coolDamageProb) {
        this.coolDamageProb = coolDamageProb;
    }

    public int getCoolDamage() {
        return coolDamage;
    }

    public void setCoolDamage(int coolDamage) {
        this.coolDamage = coolDamage;
    }

    public Set<Integer> getRevives() {
        return revives;
    }

    public void setRevives(Set<Integer> revives) {
        this.revives = revives;
    }

    public void addRevive(int revive) {
        revives.add(revive);
    }
    public HashMap<DeathType, Long> getReviveRespawnDelays() {
        return reviveRespawnDelays;
    }

    public void setReviveRespawnDelays(HashMap<DeathType, Long> reviveRespawnDelays) {
        this.reviveRespawnDelays = reviveRespawnDelays;
    }

    public List<MobSkill> getSkills() {
        return skills;
    }

    public void addSkill(MobSkill skill) {
        getSkills().add(skill);
    }

    public List<MobSkill> getAttacks() {
        return attacks;
    }

    public void addAttack(MobSkill mobSkill) {
        getAttacks().add(mobSkill);
    }

    public boolean isSelfDestruction() {
        return selfDestruction;
    }

    public void setSelfDestruction(boolean selfDestruction) {
        this.selfDestruction = selfDestruction;
    }

    public boolean isEscortMob() {
        return escortMob;
    }

    public void setEscortMob(boolean escortMob) {
        this.escortMob = escortMob;
    }

    public int getFirstAttack() {
        return firstAttack;
    }

    public void setFirstAttack(int firstAttack) {
        this.firstAttack = firstAttack;
    }
}
