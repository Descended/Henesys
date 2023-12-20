package henesys.enums;

import java.util.Arrays;

import static henesys.enums.EquipBaseStat.*;

/**
 * Created on 1/9/2018.
 */
public enum ScrollStat {
    success,
    incSTR,
    incDEX,
    incINT,
    incLUK,
    incPAD,
    incMAD,
    incPDD,
    incMDD,
    incACC,
    incEVA,
    incMHP,
    incMMP,
    incSpeed,
    incJump,
    incIUC,
    incPERIOD,
    incReqLevel,
    reqRUC,
    randOption,
    randStat,
    tuc,
    speed,
    forceUpgrade,
    cursed,
    maxSuperiorEqp,
    noNegative,
    incRandVol,
    reqEquipLevelMax,
    createType,
    optionType, recover, reset, perfectReset, reduceCooltime,
    boss,
    ignoreTargetDEF,
    incSTRr,
    incDEXr,
    incINTr,
    incLUKr,
    incCriticaldamageMin,
    incCriticaldamageMax,
    cCr,
    incDAMr,
    incPDDr,
    incMDDr,
    incEVAr,
    incACCr,
    incMHPr,
    incMMPr,
    incTerR,
    incAsrR,
    incMesoProp,
    incRewardProp,
    setItemCategory,
    ;

    public static ScrollStat getScrollStatByString(String name) {
        return Arrays.stream(values()).filter(ss -> ss.toString().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static EquipBaseStat[] getRandStats() {
        return new EquipBaseStat[]{iStr, iDex, iInt, iLuk, iMaxHP, iMaxMP, iPAD, iMAD, iPDD, iMDD, iACC, iEVA};
    }

    public EquipBaseStat getEquipStat() {
        switch (this) {
            case incSTR:
                return iStr;
            case incDEX:
                return iDex;
            case incINT:
                return iInt;
            case incLUK:
                return iLuk;
            case incPAD:
                return iPAD;
            case incMAD:
                return iMAD;
            case incPDD:
                return iPDD;
            case incMDD:
                return iMDD;
            case incACC:
                return iACC;
            case incEVA:
                return iEVA;
            case incMHP:
                return iMaxHP;
            case incMMP:
                return iMaxMP;
            case incSpeed:
            case speed:
                return iSpeed;
            case incJump:
                return iJump;
            case incReqLevel:
                return iReduceReq;
            default:
                return null;
        }
    }

    public BaseStat getBaseStat() {
        switch (this) {
            case incSTR:
                return BaseStat.STR;
            case incDEX:
                return BaseStat.DEX;
            case incINT:
                return BaseStat.INT;
            case incLUK:
                return BaseStat.LUK;
            case incPAD:
                return BaseStat.PAD;
            case incMAD:
                return BaseStat.MAD;
            case incPDD:
                return BaseStat.PDD;
            case incMDD:
                return BaseStat.MDD;
            case incACC:
                return BaseStat.ACC;
            case incEVA:
                return BaseStat.EVA;
            case incMHP:
                return BaseStat.MHP;
            case incMMP:
                return BaseStat.MMP;
            case incSpeed:
            case speed:
                return BaseStat.Speed;
            case incJump:
                return BaseStat.Jump;
            case incSTRr:
                return BaseStat.STRr;
            case incDEXr:
                return BaseStat.DEXr;
            case incINTr:
                return BaseStat.INTr;
            case incLUKr:
                return BaseStat.LUKr;
            case incMHPr:
                return BaseStat.MHPr;
            case incMMPr:
                return BaseStat.MMPr;
            default:
                return null;
        }
    }
}
