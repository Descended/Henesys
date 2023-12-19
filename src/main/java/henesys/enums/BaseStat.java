package henesys.enums;

import net.spirit.ms.client.character.skills.Option;
import net.spirit.ms.client.character.skills.SkillStat;
import net.spirit.ms.client.character.skills.info.SkillInfo;
import net.spirit.ms.client.character.skills.temp.CharacterTemporaryStat;
import net.spirit.ms.loaders.SkillData;

import java.util.HashMap;
import java.util.Map;

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
        SkillInfo si;
        // TODO: Left at "Albatross" in CTS
        switch (ctsArg) {
            case PyramidBonusDamageBuff:
            case IndiePAD:
                stats.put(PAD, o.nValue);
                break;
            case EPAD:
            case PAD:
                stats.put(PAD, o.nOption);
                break;
            case IndieMAD:
                stats.put(MAD, o.nValue);
                break;
            case MAD:
            case EMAD:
                stats.put(MAD, o.nOption);
                break;
            case IndiePDD:
                stats.put(PDD, o.nValue);
                break;
            case PDD:
            case EPDD:
                stats.put(PDD, o.nOption);
                break;
            case IndieMDD:
                stats.put(MDD, o.nValue);
                break;
            case MDD:
            case EMDD:
                stats.put(MDD, o.nOption);
                break;
            case IndiePADR:
                stats.put(PADr, o.nValue);
                break;
            case IndieMADR:
                stats.put(MADr, o.nValue);
                break;
            case IndiePDDR:
                stats.put(PDDr, o.nValue);
                break;
            case IndieMDDR:
                stats.put(MDDr, o.nValue);
                break;
            case IndieMHP:
                stats.put(MHP, o.nValue);
                break;
            case IndieMHPR:
                stats.put(MHPr, o.nValue);
                break;
            case MaxHP:
            case IncMaxHP:
                stats.put(MHPr, o.nOption);
                break;
            case IndieMMP:
            case MaxMP:
            case IncMaxMP:
                stats.put(MMPr, o.nOption);
                break;
            case IndieMMPR:
                stats.put(MMPr, o.nValue);
                break;
            case IndieACC:
                stats.put(ACC, o.nValue);
                break;
            case ACC:
                stats.put(ACC, o.nOption);
                break;
            case ACCR:
                stats.put(ACCr, o.nOption);
                break;
            case IndieEVA:
                stats.put(EVA, o.nValue);
                break;
            case EVA:
            case ItemEvade:
                stats.put(EVA, o.nOption);
                break;
            case IndieEVAR:
                stats.put(EVAr, o.nValue);
                break;
            case EVAR:
            case RWMovingEvar:
                stats.put(EVAr, o.nOption);
                break;
            case Speed:
                stats.put(Speed, o.nOption);
                break;
            case IndieSpeed:
                stats.put(Speed, o.nValue);
                break;
            case IndieJump:
                stats.put(Jump, o.nValue);
                break;
            case Jump:
                stats.put(Jump, o.nOption);
                break;
            case IndieAllStat:
                stats.put(STR, o.nValue);
                stats.put(DEX, o.nValue);
                stats.put(INT, o.nValue);
                stats.put(LUK, o.nValue);
                break;
            case IndieDodgeCriticalTime:
            case IndieCr:
                stats.put(Cr, o.nValue);
                break;
            case EnrageCr:
                stats.put(Cr, o.nOption);
                break;
            case EnrageCrDamMin:
                stats.put(CriticaldamageMin, o.nOption);
                break;
            case IndieCrMax:
            case IndieCrMaxR:
                stats.put(CriticaldamageMax, o.nValue);
                break;
            case IncCriticalDamMin:
                stats.put(CriticaldamageMin, o.nOption);
                break;
            case IncCriticalDamMax:
                stats.put(CriticaldamageMax, o.nOption);
                break;
            case IndieEXP:
            case IndieRelaxEXP:
                stats.put(EXPr, o.nValue);
                break;
            case HolySymbol:
            case ExpBuffRate:
            case CarnivalExp:
            case PlusExpRate:
                stats.put(EXPr, o.nOption);
                break;
            case IndieBooster:
                stats.put(booster, o.nValue);
                break;
            case Booster:
            case PartyBooster:
            case HayatoBooster:
                stats.put(booster, o.nOption);
                break;
            case STR:
            case ZeroAuraStr:
                stats.put(STR, o.nOption);
                break;
            case IndieSTR:
                stats.put(STR, o.nValue);
                break;
            case IndieDEX:
                stats.put(DEX, o.nValue);
                break;
            case IndieINT:
                stats.put(INT, o.nValue);
                break;
            case IndieLUK:
                stats.put(LUK, o.nValue);
                break;
            case IndieStatR:
                stats.put(STRr, o.nValue);
                stats.put(DEXr, o.nValue);
                stats.put(INTr, o.nValue);
                stats.put(LUKr, o.nValue);
                break;
            case IndieDamR:
                stats.put(DAMr, o.nValue);
                break;
            case DamR:
            case BeastFormDamageUp:
                stats.put(DAMr, o.nOption);
                break;
            case IndieAsrR:
                stats.put(AsrR, o.nValue);
                break;
            case AsrR:
            case AsrRByItem:
            case IncAsrR:
                stats.put(AsrR, o.nOption);
                break;
            case IndieTerR:
                stats.put(TerR, o.nValue);
                break;
            case TerR:
            case IncTerR:
                stats.put(TerR, o.nOption);
                break;
            case IndieBDR:
                stats.put(BossDamage, o.nValue);
                break;
            case BdR:
                stats.put(BossDamage, o.nOption);
                break;
            case IndieStance:
                stats.put(stance, o.nValue);
                break;
            case IndieIgnoreMobpdpR:
                stats.put(ignoreTargetDEF, o.nValue);
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
            case CriticalBuff:
            case ItemCritical:
                stats.put(Cr, o.nOption);
                break;
            case AdvancedBless:
                si = SkillData.getSkillInfoById(o.rOption);
                stats.put(PAD, si.getValue(SkillStat.x, o.nOption));
                stats.put(MAD, si.getValue(SkillStat.y, o.nOption));
                stats.put(PDD, si.getValue(SkillStat.z, o.nOption));
                stats.put(MDD, si.getValue(SkillStat.u, o.nOption));
                stats.put(ACC, si.getValue(SkillStat.v, o.nOption));
                stats.put(mpconReduce, si.getValue(SkillStat.mpConReduce, o.nOption));
                stats.put(BossDamage, o.xOption);
                break;
            case IllusionStep:
                stats.put(EVAr, o.nOption);
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
            case BeastFormMaxHP:
                stats.put(MHP, o.nOption);
                break;
            case EMMP:
                stats.put(MMP, o.nOption);
                break;
            case Bless:
                si = SkillData.getSkillInfoById(o.rOption);
                stats.put(PAD, si.getValue(SkillStat.x, o.nOption));
                stats.put(MAD, si.getValue(SkillStat.y, o.nOption));
                stats.put(PDD, si.getValue(SkillStat.z, o.nOption));
                stats.put(MDD, si.getValue(SkillStat.u, o.nOption));
                stats.put(ACC, si.getValue(SkillStat.v, o.nOption));
                stats.put(EVA, si.getValue(SkillStat.w, o.nOption));
                break;
            case BlessOfDarkness:
                // TODO
                break;
            case IndieScriptBuff:
                stats.put(buffTimeR, o.nValue);
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
