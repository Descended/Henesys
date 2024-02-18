package henesys.client.character.skills.info;

import com.fathzer.soft.javaluator.AbstractEvaluator;
import com.fathzer.soft.javaluator.DoubleEvaluator;
import henesys.client.character.Char;
import henesys.enums.BaseStat;
import henesys.enums.SkillStat;
import henesys.enums.WeaponType;
import henesys.util.Rect;
import henesys.util.Util;
import henesys.util.container.Tuple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * Created on 12/20/2017.
 */
public class SkillInfo {
    private static final Logger log = LogManager.getLogger(SkillInfo.class);
    private AbstractEvaluator<?> evaluator;

    private int type;
    private int skillId;
    private int rootId;
    private int maxLevel;
    private int currentLevel;
    private int masterLevel;
    private int fixLevel;
    private int hyper;
    private int hyperstat;
    private int vehicleId;
    private int reqTierPoint;
    private boolean invisible;
    private boolean massSpell;
    private boolean notCooltimeReset;
    private boolean notIncBuffDuration;
    private boolean psd;
    private boolean ignoreCounter;
    private String elemAttr = "";

    private List<Rect> rects = new ArrayList<>();
    private Set<Integer> psdSkills = new HashSet<>();
    private Set<Integer> addAttackSkills = new HashSet<>();
    private Map<SkillStat, String> skillStatInfo = new HashMap<>();
    private Map<Integer, Integer> reqSkills = new HashMap<>();
    private Map<Integer, Integer> extraSkillInfo = new HashMap<>();
    private List<String> unknownSkillStats = new ArrayList<>();

    private Map<WeaponType, Map<SkillStat, Integer>> psdWT = new HashMap<>(); // <WeaponType, <SkillStat, Value>>


    private AbstractEvaluator<?> getEvaluator() {
        if (evaluator==null) {
            evaluator = new DoubleEvaluator();
        }
        return evaluator;
    }

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

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public Map<SkillStat, String> getSkillStatInfo() {
        return skillStatInfo;
    }

    public void setSkillStatInfo(Map<SkillStat, String> skillStatInfo) {
        this.skillStatInfo = skillStatInfo;
    }

    public void addSkillStatInfo(SkillStat sc, String value) {
        getSkillStatInfo().put(sc, value);
    }

    public int getValue(SkillStat skillStat, int slv) {
        int result = 0;
        String value = getSkillStatInfo().get(skillStat);
        if(value == null || slv == 0) {
            return 0;
        }
        // Sometimes newlines get taken, just remove those
        value = value.replace("\n", "").replace("\r", "");
        value = value.replace("\\n", "").replace("\\r", ""); // unluko
        String original = value;
        if(Util.isNumber(value)) {
            result = Integer.parseInt(value);
        } else {
            try {
                value = value.replace("u", "ceil");
                value = value.replace("d", "floor");
                String toReplace = value.contains("y") ? "y"
                        : value.contains("X") ? "X"
                        : "x";
                value = value.replace(toReplace, slv + "");
                if (!value.isEmpty()) {
                    result = ((Double) getEvaluator().evaluate(value)).intValue();
                }
            } catch (Exception e) {
                log.error(String.format("Error when parsing: skill %d, level %d, skill stat %s, tried to eval %s.",
                        getSkillId(), slv, skillStat, original), e);
            }
        }
        return result;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public int getMasterLevel() {
        return masterLevel;
    }

    public void setMasterLevel(int masterLevel) {
        this.masterLevel = masterLevel;
    }

    public int getFixLevel() {
        return fixLevel;
    }

    public void setFixLevel(int fixLevel) {
        this.fixLevel = fixLevel;
    }

    public void addRect(Rect rect) {
        getRects().add(rect);
    }

    public List<Rect> getRects() {
        return rects;
    }

    public Rect getLastRect() {
        return rects != null && rects.size() > 0 ? rects.get(rects.size() - 1) : null;
    }

    public Rect getFirstRect() {
        return rects != null && rects.size() > 0 ? rects.get(0) : null;
    }

    public boolean isMassSpell() {
        return massSpell;
    }

    public void setMassSpell(boolean massSpell) {
        this.massSpell = massSpell;
    }

    public boolean hasCooltime() {
        return getValue(SkillStat.cooltime, 1) > 0;
    }

    public Map<BaseStat, Integer> getPsdWTStatValues(Char chr, int slv) {
        Map<BaseStat, Integer> stats = new HashMap<>();
        if (slv > 0) {
            getSkillStatsByWT(chr.getEquippedInventory().getEquippedWeaponType()).forEach((ss, value) ->
                    stats.put(ss.getBaseStat(), value + stats.getOrDefault(ss.getBaseStat(), 0))
            );
        }
        return stats;
    }

    public Map<BaseStat, Integer> getBaseStatValues(Char chr, int slv) {
        Map<BaseStat, Integer> stats = new HashMap<>();
        for (SkillStat ss : getSkillStatInfo().keySet()) {
            Tuple<BaseStat, Integer> bs = getBaseStatValue(ss, slv, chr);
            stats.put(bs.getLeft(), bs.getRight());
        }
        return stats;
    }

    private Tuple<BaseStat, Integer> getBaseStatValue(SkillStat ss, int slv, Char chr) {
        BaseStat bs = ss.getBaseStat();
        int value = getValue(ss, slv);
        return new Tuple<>(bs, value);
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setRects(List<Rect> rects) {
        this.rects = rects;
    }

    public void addPsdSkill(int skillID) {
        getPsdSkills().add(skillID);
    }

    public Set<Integer> getPsdSkills() {
        return psdSkills;
    }

    public void setPsdSkills(Set<Integer> psdSkills) {
        this.psdSkills = psdSkills;
    }

    public String getElemAttr() {
        return elemAttr;
    }

    public void setElemAttr(String elemAttr) {
        this.elemAttr = elemAttr;
    }

    public void setHyper(int hyper) {
        this.hyper = hyper;
    }

    public int getHyper() {
        return hyper;
    }

    public void setHyperStat(int hyperstat) {
        this.hyperstat = hyperstat;
    }

    public int getHyperStat() {
        return hyperstat;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setReqTierPoint(int reqTierPoint) {
        this.reqTierPoint = reqTierPoint;
    }

    public int getReqTierPoint() {
        return reqTierPoint;
    }

    public void addReqSkill(int skillID, int slv) {
        getReqSkills().put(skillID, slv);
    }

    public Map<Integer, Integer> getReqSkills() {
        return reqSkills;
    }

    public void setNotCooltimeReset(boolean notCooltimeReset) {
        this.notCooltimeReset = notCooltimeReset;
    }

    public boolean isNotCooltimeReset() {
        return notCooltimeReset;
    }

    public void setNotIncBuffDuration(boolean notIncBuffDuration) {
        this.notIncBuffDuration = notIncBuffDuration;
    }

    public boolean isNotIncBuffDuration() {
        return notIncBuffDuration;
    }

    public void setPsd(boolean psd) {
        this.psd = psd;
    }

    public boolean isPsd() {
        return psd;
    }

    public Set<Integer> getAddAttackSkills() {
        return addAttackSkills;
    }

    public void setAddAttackSkills(Set<Integer> addAttackSkills) {
        this.addAttackSkills = addAttackSkills;
    }

    public void addAddAttackSkills(int skillId) {
        getAddAttackSkills().add(skillId);
    }

    public Map<Integer, Integer> getExtraSkillInfo() {
        return extraSkillInfo;
    }

    public void setExtraSkillInfo(Map<Integer, Integer> extraSkillInfo) {
        this.extraSkillInfo = extraSkillInfo;
    }

    public void addExtraSkillInfo(int skillid, int delay) {
        getExtraSkillInfo().put(skillid, delay);
    }

    public List<String> getUnknownSkillStats() {
        return unknownSkillStats;
    }

    public void addUnknownSkillStat(String value) {
        getUnknownSkillStats().add(value);
    }

    public Map<SkillStat, Integer> getSkillStatsByWT(WeaponType weaponType) {
        return getPsdWT().getOrDefault(weaponType, Collections.EMPTY_MAP);
    }
    public Map<WeaponType, Map<SkillStat, Integer>> getPsdWT() {
        return psdWT;
    }
    public void addPsdWT(WeaponType wt, Map<SkillStat, Integer> skillStats) {
        getPsdWT().put(wt, skillStats);
    }

}
