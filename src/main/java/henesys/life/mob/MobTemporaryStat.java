package henesys.life.mob;

import henesys.connection.OutPacket;
import henesys.skills.Option;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

public class MobTemporaryStat {
    private List<BurnedInfo> burnedInfoList = new ArrayList<>();
    private Comparator<MobStat> mobStatComper = (o1, o2) -> {
        int res = 0;
        if (o1.getPos() < o2.getPos()) {
            res = -1;
        } else if (o1.getPos() > o2.getPos()) {
            res = 1;
        } else {
            if (o1.getVal() < o2.getVal()) {
                res = -1;
            } else if (o1.getVal() > o2.getVal()) {
                res = 1;
            }
        }
        return res;
    };
    private final TreeMap<MobStat, Option> currentStatVals = new TreeMap<>(mobStatComper);
    private final TreeMap<MobStat, Option> newStatVals = new TreeMap<>(mobStatComper);
    private final TreeMap<MobStat, Option> removedStatVals = new TreeMap<>(mobStatComper);
    private Map<MobStat, ScheduledFuture> schedules = new HashMap<>();
    private Mob mob;

    public MobTemporaryStat(Mob mob) {
        this.mob = mob;
    }

    public MobTemporaryStat deepCopy() {
        MobTemporaryStat copy = new MobTemporaryStat(getMob());
        copy.mobStatComper = getMobStatComper();
        for (MobStat ms : getCurrentStatVals().keySet()) {
            copy.addStatOptions(ms, getCurrentStatVals().get(ms).deepCopy());
        }
        return copy;
    }

    public Option getNewOptionsByMobStat(MobStat mobStat) {
        return getNewStatVals().getOrDefault(mobStat, null);
    }

    public Option getCurrentOptionsByMobStat(MobStat mobStat) {
        return getCurrentStatVals().getOrDefault(mobStat, null);
    }

    public Option getRemovedOptionsByMobStat(MobStat mobStat) {
        return getRemovedStatVals().getOrDefault(mobStat, null);
    }

    public void encode(OutPacket outPacket) {
        int[] mask = getNewMask();
        for (int i = mask.length - 1; i >= 0; i--) {
            outPacket.encodeInt(mask[i]);
        }

        for (Map.Entry<MobStat, Option> entry : getNewStatVals().entrySet()) {
            MobStat mobStat = entry.getKey();
            Option option = entry.getValue();
            switch (mobStat) {
                case Burned:
                    outPacket.encodeInt(option.nOption);
                    if (option.nOption > 0) {
                        for (BurnedInfo bi : getBurnedInfoList()) {
                            bi.encode(outPacket);
                        }
                    }
                    break;
                case Disable:
                    outPacket.encodeByte(false); // bInvincible
                    outPacket.encodeByte(false); // bDisable
                    break;
                case PCounter:
                case MCounter:
                    outPacket.encodeShort(option.nOption);
                    outPacket.encodeInt(option.rOption);
                    outPacket.encodeShort(option.tOption / 500);
                    outPacket.encodeInt(option.nOption);
                default:
                    outPacket.encodeShort(option.nOption);
                    outPacket.encodeInt(option.rOption);
                    outPacket.encodeShort(option.tOption / 500);
            }
        }
        getNewStatVals().clear();
    }

    private int[] getMaskByCollection(Map<MobStat, Option> map) {
        int[] maskArr = new int[4];
        // Build the mask array by pos -
        map.keySet().forEach(stat -> maskArr[stat.getPos()] |= stat.getVal());
        return maskArr;
    }

    public int[] getNewMask() {
        return getMaskByCollection(getNewStatVals());
    }

    public int[] getCurrentMask() {
        return getMaskByCollection(getCurrentStatVals());
    }

    public int[] getRemovedMask() {
        return getMaskByCollection(getRemovedStatVals());
    }

    public boolean hasNewMobStat(MobStat mobStat) {
        return getNewStatVals().containsKey(mobStat);
    }

    public boolean hasCurrentMobStat(MobStat mobStat) {
        return getCurrentStatVals().containsKey(mobStat);
    }

    public boolean hasCurrentMobStatBySkillId(int skillId) {
        return getCurrentStatVals().entrySet().stream().anyMatch(map -> map.getValue().rOption == skillId);
    }

    public boolean hasRemovedMobStat(MobStat mobStat) {
        return getRemovedStatVals().containsKey(mobStat);
    }

    public Map<MobStat, Option> getCurrentStatVals() {
        return currentStatVals;
    }

    public TreeMap<MobStat, Option> getNewStatVals() {
        return newStatVals;
    }

    public TreeMap<MobStat, Option> getRemovedStatVals() {
        return removedStatVals;
    }

    public void removeMobStat(MobStat mobStat, boolean fromSchedule) {
        synchronized (currentStatVals) {
            getRemovedStatVals().put(mobStat, getCurrentStatVals().get(mobStat));
            getCurrentStatVals().remove(mobStat);
//            getMob().getField().broadcastPacket(MobPool.statReset(getMob(), (byte) 1, false)); TODO
            getSchedules().remove(mobStat);
            if (!fromSchedule && getSchedules().containsKey(mobStat)) {
                getSchedules().get(mobStat).cancel(true);
                getSchedules().remove(mobStat);
            } else {
                getSchedules().remove(mobStat);
            }
        }
    }


    /**
     * Adds a new MobStat to this MobTemporaryStat. Will immediately broadcast the reaction to all
     * clients.
     * Only works for user skills, not mob skills. For the latter, use {@link
     * #addMobSkillOptionsAndBroadCast(MobStat, Option)}.
     *
     * @param mobStat The MobStat to add.
     * @param option  The Option that contains the values of the stat.
     */
    public void addStatOptionsAndBroadcast(MobStat mobStat, Option option) {
        addStatOptions(mobStat, option);
//        mob.getField().broadcastPacket(MobPool.statSet(getMob(), (short) 0)); TODO
    }

    /**
     * Adds a new MobStat to this MobTemporary stat. Will immediately broadcast the reaction to all
     * clients.
     * Only works for mob skills, not user skills. For the latter, use {@link
     * #addStatOptionsAndBroadcast(MobStat, Option)}.
     *
     * @param mobStat The MobStat to add.
     * @param o       The option that contains the values of the stat.
     */
    public void addMobSkillOptionsAndBroadCast(MobStat mobStat, Option o) {
        o.rOption |= o.slv << 16; // mob skills are encoded differently: not an int, but short (skill ID), then short (slv).
        addStatOptionsAndBroadcast(mobStat, o);
    }

    public void addStatOptions(MobStat mobStat, Option option) {
        option.tTerm *= 1000;
        option.tOption *= 1000;
        int tAct = option.tOption > 0 ? option.tOption : option.tTerm;
        getNewStatVals().put(mobStat, option);
        getCurrentStatVals().put(mobStat, option);
        if (tAct > 0) {
            if (getSchedules().containsKey(mobStat)) {
                getSchedules().get(mobStat).cancel(true);
            }
//            ScheduledFuture sf = EventManager.addEvent(() -> removeMobStat(mobStat, true), tAct); TODO
//            getSchedules().put(mobStat, sf);
        }
    }

    public Comparator getMobStatComper() {
        return mobStatComper;
    }

    public boolean hasNewMovementAffectingStat() {
        return getNewStatVals().keySet().stream().anyMatch(MobStat::isMovementAffectingStat);
    }

    public boolean hasCurrentMovementAffectingStat() {
        return getCurrentStatVals().keySet().stream().anyMatch(MobStat::isMovementAffectingStat);
    }

    public boolean hasRemovedMovementAffectingStat() {
        return getRemovedStatVals().keySet().stream().anyMatch(MobStat::isMovementAffectingStat);
    }

    public Map<MobStat, ScheduledFuture> getSchedules() {
        if (schedules == null) {
            schedules = new HashMap<>();
        }
        return schedules;
    }

    public Mob getMob() {
        return mob;
    }

    public void setMob(Mob mob) {
        this.mob = mob;
    }

    public void clear() {
        getSchedules().clear();
        getCurrentStatVals().forEach((ms, o) -> removeMobStat(ms, false));
    }

    public void removeBuffs() {
        removeMobStat(MobStat.PowerUp, false);
        removeMobStat(MobStat.MagicUp, false);
        removeMobStat(MobStat.PGuardUp, false);
        removeMobStat(MobStat.MGuardUp, false);
        removeMobStat(MobStat.PImmune, false);
        removeMobStat(MobStat.MImmune, false);
        removeMobStat(MobStat.PCounter, false);
        removeMobStat(MobStat.MCounter, false);
        if (hasCurrentMobStat(MobStat.ACC) && getCurrentOptionsByMobStat(MobStat.ACC).nOption > 0) {
            removeMobStat(MobStat.ACC, false);
        }
        if (hasCurrentMobStat(MobStat.EVA) && getCurrentOptionsByMobStat(MobStat.EVA).nOption > 0) {
            removeMobStat(MobStat.EVA, false);
        }
//        getMob().getField().broadcastPacket(MobPool.statReset(getMob(), (byte) 0, false)); TODO
    }

    public void removeEverything() {
        Set<MobStat> mobStats = new HashSet<>(getCurrentStatVals().keySet());
        mobStats.forEach(ms -> removeMobStat(ms, false));
    }

    public List<BurnedInfo> getBurnedInfoList() {
        return burnedInfoList;
    }

    public void setBurnedInfoList(List<BurnedInfo> burnedInfoList) {
        this.burnedInfoList = burnedInfoList;
    }
}
