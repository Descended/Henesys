package henesys.enums;


import henesys.skills.CharacterTemporaryStat;
import henesys.skills.Option;

import java.util.HashMap;
import java.util.Map;

import static henesys.skills.CharacterTemporaryStat.*;

/**
 * Created on 5/4/2018.
 */
public enum BaseStat {
    UNK,
    STR,
    STRr,
    DEX,
    DEXr,
    INT,
    INTr,
    LUK,
    LUKr,
    PAD,
    PADr,
    MAD,
    MADr,
    PDD,
    PDDr,
    MDD,
    MDDr,
    MHP,
    MHPr,
    MMP,
    MMPr,
    Cr, // Crit rate
    CriticaldamageMin, // Min crit damage
    CriticaldamageMax, // Max crit damage
    DAMr, // Final damage (total damage)
    BossDamage, // Boss damage
    ignoreTargetDEF, // Ignore enemy defense
    AsrR, // Abnormal status resistance
    TerR, // All Elemental Resistance
    ACC,
    ACCr,
    EVA,
    EVAr,
    Jump,
    Speed,
    EXPr,
    RewardProp, // Drop RAte
    MesoProp, // Meso Rate
    booster,
    stance,
    mastery,
    damageOver, // max damage
    AllStat,
    AllStatr,
    RecoveryHP,
    RecoveryMP,
    Allskill,
    STRlv,
    DEXlv,
    INTlv,
    LUKlv,
    buffTimeR,  // Buff Duration multiplier
    RecoveryUP, // & Increase in HP Recovery from Items and Skills
    mpconReduce,
    reduceCooltime,
    PADlv,
    MADlv,
    MHPlv,
    MMPlv,
    dmgReduce,
    magicGuard, // in %  of HP goes to MP instead.
    invincibleAfterRevive, // in seconds
    shopDiscountR, // % discount on shop items
    pqShopDiscountR, // % discount in pq Shop
    ;

    public static BaseStat getFromStat(Stat s) {
        switch (s) {
            case str:
                return STR;
            case dex:
                return DEX;
            case inte:
                return INT;
            case luk:
                return LUK;
            case mhp:
                return MHP;
            case mmp:
                return MMP;
            default:
                return UNK;
        }
    }

    public BaseStat getRateVar() {
        switch (this) {
            case STR:
                return STRr;
            case DEX:
                return DEXr;
            case INT:
                return INTr;
            case LUK:
                return LUKr;
            case PAD:
                return PADr;
            case MAD:
                return MADr;
            case PDD:
                return PDDr;
            case MDD:
                return MDDr;
            case MHP:
                return MHPr;
            case MMP:
                return MMPr;
            case ACC:
                return ACCr;
            case EVA:
                return EVAr;
            default:
                return null;
        }
    }

    public BaseStat getLevelVar() {
        switch (this) {
            case STR:
                return STRlv;
            case DEX:
                return DEXlv;
            case INT:
                return INTlv;
            case LUK:
                return LUKlv;
            case PAD:
                return PADlv;
            case MAD:
                return MADlv;
            case MHP:
                return MHPlv;
            case MMP:
                return MMPlv;
        }
        return null;
    }

    public static Map<BaseStat, Integer> getFromCTS(CharacterTemporaryStat ctsArg, Option o) {
        Map<BaseStat, Integer> stats = new HashMap<>();
//        SkillInfo si;
        // TODO: Left at "Albatross" in CTS
        switch (ctsArg) {
            case EPAD:
            case PAD:
                stats.put(PAD, o.nOption);
                break;
            case MAD:
                stats.put(MAD, o.nOption);
                break;
            case PDD:
            case EPDD:
                stats.put(PDD, o.nOption);
                break;
            case MDD:
            case EMDD:
                stats.put(MDD, o.nOption);
                break;
            case MaxHP:
                stats.put(MHPr, o.nOption);
                break;
            case MaxMP:
                stats.put(MMPr, o.nOption);
                break;
            case ACC:
                stats.put(ACC, o.nOption);
                break;
            case EVA:
                stats.put(EVA, o.nOption);
                break;
            case Speed:
                stats.put(Speed, o.nOption);
                break;
            case Jump:
                stats.put(Jump, o.nOption);
                break;
            case HolySymbol:
            case ExpBuffRate:
                stats.put(EXPr, o.nOption);
                break;
            case Booster:
            case PartyBooster:
                stats.put(booster, o.nOption);
                break;
            case DamR:
                stats.put(DAMr, o.nOption);
                break;
            case MesoUp:
            case MesoUpByItem:
                stats.put(MesoProp, o.nOption);
                break;
            case BasicStatUp:
                // TODO what exactly does this give?
                break;
            case Stance:
                stats.put(stance, o.nOption);
                break;
            case SharpEyes:
                stats.put(Cr, o.nOption);
                break;
            case Concentration:
                // TODO
                break;
            case ItemUpByItem:
                stats.put(RewardProp, o.nOption);
                break;
            case EventRate:
                // TODO
                break;
            case FinalCut:
                // TODO
                break;
            case EMHP:
                stats.put(MHP, o.nOption);
                break;
            case EMMP:
                stats.put(MMP, o.nOption);
                break;
            default:
                stats.put(UNK, o.nOption);
                break;
        }
        return stats;
    }

    public Stat toStat() {
        switch (this) {
            case STR:
                return Stat.str;
            case DEX:
                return Stat.dex;
            case INT:
                return Stat.inte;
            case LUK:
                return Stat.luk;
            case MHP:
                return Stat.mhp;
            case MMP:
                return Stat.mmp;
            default:
                return null;
        }
    }
    public boolean isNonAdditiveStat() {
        return this == ignoreTargetDEF;
    }
}
