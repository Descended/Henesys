package henesys.life.mob.skill;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MobSkill {
    private static final Logger log = LogManager.getRootLogger();
    private int skillSN;
    private byte action;
    private int level;
    private int effectAfter;
    private int skillAfter;
    private boolean firstAttack;
    private boolean onlyFsm;
    private boolean onlyOtherSkill;
    private int afterDelay;
    private int fixDamR;
    private boolean doFirst;
    private String info;
    private boolean afterDead;
    private int afterAttack = -1;
    private int afterAttackCount;
    private int coolTime;
    private String speak;
    private int skillID;
    private int disease;

    public int getSkillSN() {
        return skillSN;
    }

    public void setSkillSN(int skillSN) {
        this.skillSN = skillSN;
    }

    public byte getAction() {
        return action;
    }

    public void setAction(byte action) {
        this.action = action;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getEffectAfter() {
        return effectAfter;
    }

    public void setEffectAfter(int effectAfter) {
        this.effectAfter = effectAfter;
    }

    public int getSkillAfter() {
        return skillAfter;
    }

    public void setSkillAfter(int skillAfter) {
        this.skillAfter = skillAfter;
    }

    public boolean isFirstAttack() {
        return firstAttack;
    }

    public void setFirstAttack(boolean firstAttack) {
        this.firstAttack = firstAttack;
    }

    public boolean isOnlyFsm() {
        return onlyFsm;
    }

    public void setOnlyFsm(boolean onlyFsm) {
        this.onlyFsm = onlyFsm;
    }

    public boolean isOnlyOtherSkill() {
        return onlyOtherSkill;
    }

    public void setOnlyOtherSkill(boolean onlyOtherSkill) {
        this.onlyOtherSkill = onlyOtherSkill;
    }

    public int getAfterDelay() {
        return afterDelay;
    }

    public void setAfterDelay(int afterDelay) {
        this.afterDelay = afterDelay;
    }

    public int getFixDamR() {
        return fixDamR;
    }

    public void setFixDamR(int fixDamR) {
        this.fixDamR = fixDamR;
    }

    public boolean isDoFirst() {
        return doFirst;
    }

    public void setDoFirst(boolean doFirst) {
        this.doFirst = doFirst;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isAfterDead() {
        return afterDead;
    }

    public void setAfterDead(boolean afterDead) {
        this.afterDead = afterDead;
    }

    public int getAfterAttack() {
        return afterAttack;
    }

    public void setAfterAttack(int afterAttack) {
        this.afterAttack = afterAttack;
    }

    public int getAfterAttackCount() {
        return afterAttackCount;
    }

    public void setAfterAttackCount(int afterAttackCount) {
        this.afterAttackCount = afterAttackCount;
    }

    public int getCoolTime() {
        return coolTime;
    }

    public void setCoolTime(int coolTime) {
        this.coolTime = coolTime;
    }

    public String getSpeak() {
        return speak;
    }

    public void setSpeak(String speak) {
        this.speak = speak;
    }

    public int getSkillID() {
        return skillID;
    }

    public void setSkillID(int skillID) {
        this.skillID = skillID;
    }

    public int getDisease() {
        return disease;
    }

    public void setDisease(int disease) {
        this.disease = disease;
    }
}
