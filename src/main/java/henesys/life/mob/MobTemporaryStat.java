package henesys.life.mob;

import henesys.connection.OutPacket;
import henesys.skills.Option;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

public class MobTemporaryStat {
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
        synchronized (currentStatVals) {
            // DecodeBuffer(12) + MobStat::DecodeTemporary
            int[] mask = getNewMask();
            for (int i = 0; i < mask.length; i++) {
                outPacket.encodeInt(mask[i]);
            }

            for (Map.Entry<MobStat, Option> entry : getNewStatVals().entrySet()) {
                MobStat mobStat = entry.getKey();
                Option option = entry.getValue();
                switch (mobStat) {
                    case PAD:
                    case PDR:
                    case MAD:
                    case MDR:
                    case ACC:
                    case EVA:
                    case Speed:
                    case Stun:
                    case Freeze:
                    case Poison:
                    case Seal:
                    case Darkness:
                    case PowerUp:
                    case MagicUp:
                    case PGuardUp:
                    case MGuardUp:
                    case PImmune:
                    case MImmune:
                    case Web:
                    case HardSkin:
                    case Ambush:
                    case Venom:
                    case Blind:
                    case SealSkill:
                    case Dazzle:
                    case PCounter:
                    case MCounter:
                    case RiseByToss:
                    case BodyPressure:
                    case Weakness:
                    case Showdown:
                    case MagicCrash:
                    case DamagedElemAttr:
                        outPacket.encodeInt(getNewOptionsByMobStat(mobStat).nOption);
                        outPacket.encodeInt(getNewOptionsByMobStat(mobStat).rOption);
                        outPacket.encodeShort(getNewOptionsByMobStat(mobStat).tOption / 500);
                }
            }
            if (hasNewMobStat(MobStat.PDR)) {
                outPacket.encodeInt(getNewOptionsByMobStat(MobStat.PDR).cOption);
            }
            if (hasNewMobStat(MobStat.MDR)) {
                outPacket.encodeInt(getNewOptionsByMobStat(MobStat.MDR).cOption);
            }
            if (hasNewMobStat(MobStat.PCounter)) {
                outPacket.encodeInt(getNewOptionsByMobStat(MobStat.PCounter).wOption);
            }
            if (hasNewMobStat(MobStat.MCounter)) {
                outPacket.encodeInt(getNewOptionsByMobStat(MobStat.MCounter).wOption);
            }
            if (hasNewMobStat(MobStat.PCounter)) {
                outPacket.encodeInt(getNewOptionsByMobStat(MobStat.PCounter).mOption); // nCounterProb
                outPacket.encodeByte(getNewOptionsByMobStat(MobStat.PCounter).bOption); // bCounterDelay
                outPacket.encodeInt(getNewOptionsByMobStat(MobStat.PCounter).nReason); // nAggroRank
            } else if (hasNewMobStat(MobStat.MCounter)) {
                outPacket.encodeInt(getNewOptionsByMobStat(MobStat.MCounter).mOption); // nCounterProb
                outPacket.encodeByte(getNewOptionsByMobStat(MobStat.MCounter).bOption); // bCounterDelay
                outPacket.encodeInt(getNewOptionsByMobStat(MobStat.MCounter).nReason); // nAggroRank
            }
            if (hasNewMobStat(MobStat.Speed)) {
                outPacket.encodeByte(getNewOptionsByMobStat(MobStat.Speed).mOption);
            }
            if (hasNewMobStat(MobStat.Freeze)) {
                outPacket.encodeInt(getNewOptionsByMobStat(MobStat.Freeze).cOption);
            }
            getNewStatVals().clear();
        }
    }

    private int[] getMaskByCollection(Map<MobStat, Option> map) {
        OutPacket outPacket = new OutPacket();
        int[] res = new int[3];
        for (MobStat mobStat : map.keySet()) {
            res[mobStat.getPos()] |= mobStat.getVal();
        }
        for (int re : res) {
            outPacket.encodeInt(re);
        }
        outPacket.release();
        return res;
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
}
