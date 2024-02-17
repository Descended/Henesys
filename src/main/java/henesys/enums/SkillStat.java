package henesys.enums;

public enum SkillStat {
    acc,
    emdd,
    criticaldamageMin,
    hp,
    bulletCount,
    subTime,
    itemConsume,
    mhpR,
    padX,
    pad,
    moneyCon,
    action,
    criticaldamageMax,
    jump,
    emhp,
    epad,
    mp,
    dotInterval,
    mdd,
    er,
    rb,
    selfDestruction,
    mmpR,
    hpCon,
    madX,
    mobCount,
    morph,
    damage,
    ignoreMobpdpR,
    itemConNo,
    epdd,
    dot,
    range,
    itemCon,
    speed,
    mastery,
    mad,
    eva,
    dotTime,
    pddR,
    prop,
    bulletConsume,
    subProp,
    attackCount,
    emmp,
    terR,
    mpCon,
    damR,
    cooltime,
    cr,
    pdd,
    mesoR,
    t,
    u,
    v,
    mddR,
    w,
    x,
    y,
    z,
    expR,
    time,
    asrR
    ;

    public static SkillStat getSkillStatByString(String s) {
        for(SkillStat skillStat : SkillStat.values()) {
            if(skillStat.toString().equals(s)) {
                return skillStat;
            }
        }
        return null;
    }

    public BaseStat getBaseStat() {
        return switch (this) {
            case pdd, epdd -> BaseStat.PDD;
            case pddR -> BaseStat.PDDr;
            case emdd, mdd -> BaseStat.MDD;
            case mddR -> BaseStat.MDDr;
            case emhp -> BaseStat.MHP;
            case mhpR -> BaseStat.MHPr;
            case emmp -> BaseStat.MMP;
            case mmpR -> BaseStat.MMPr;
            case speed -> BaseStat.Speed;
            case jump -> BaseStat.Jump;
            case asrR -> BaseStat.AsrR;
            case pad, padX, epad -> BaseStat.PAD;
            case mad, madX -> BaseStat.MAD;
            case terR -> BaseStat.TerR;
            case eva -> BaseStat.EVA;
            case mastery -> BaseStat.mastery;
            case ignoreMobpdpR -> BaseStat.ignoreTargetDEF;
            case criticaldamageMin -> BaseStat.CriticaldamageMin;
            case criticaldamageMax -> BaseStat.CriticaldamageMax;
            case cr, expR, mesoR -> BaseStat.MesoProp;
            case hp -> BaseStat.RecoveryHP;
            case mp -> BaseStat.RecoveryMP;
            default -> null;
        };
    }
}
