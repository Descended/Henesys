package henesys.client.anticheat;

import henesys.client.character.Char;
import henesys.client.character.CharacterStat;
import henesys.life.mob.Mob;
import henesys.skills.CharacterTemporaryStat;

/**
 * @author Desc
 * (<a href="https://github.com/Descended">Desc</a>)
 * */
public class CalcDamage {

    Rand32 rndGenForCharacter;
    Rand32 rndForCheckDamageMiss;
    Rand32 rndGenForMob;

    public CalcDamage(int s1, int s2, int s3) {
        this.rndGenForCharacter = new Rand32(s1, s2, s3);
        this.rndForCheckDamageMiss = new Rand32(s1, s2, s3);
        this.rndGenForMob = new Rand32(s1, s2, s3);
    }

    public double calcMobBaseDamage(int p1, int nRand) {
        return getRandom(nRand, p1, 0.85 * p1);
    }

    public double adjustBaseDefense(double damage, int nAdd, int nAttackLevel, int nTargetLevel, int nPsdDr) {
        var v5 = nAdd * 0.25;
        var v6 = v5 + 0.5;
        var nFixedCanceling = v5 + 0.5;
        var v7 = Math.sqrt(v5);
        var v8 = nPsdDr * v7 / 100 + v7;
        if (nTargetLevel < nAttackLevel)
        {
            var v9 = Math.abs(nAttackLevel - nTargetLevel);
            double v10 = 4 * v9;

            if (4 * v9 >= v6) v10 = v6;

            double v11 = 2 * v9;
            nFixedCanceling = v6 - v10;

            if (v11 >= v8) v11 = v8;

            v8 -= v11;
        }

        var result = damage - nFixedCanceling;
        var v13 = damage * (100 - v8) / 100.0;

        if (v13 <= result) result = v13;
        if (result <= 1.0) result = 1.0;

        return result;
    }

    // int __cdecl `anonymous namespace'::calc_evar(int nEVA, int nMobACC, int nTargetLevel, int nAttackLevel, int nEr)
    int calcEvar(int eva, int mobAccuracy, int targetLevel, int attackLevel, int er) {
        int sqrtEVA = (int) Math.sqrt(eva);
        int sqrtMobACC = (int) Math.sqrt(mobAccuracy);
        int nFormulaRes = sqrtEVA - sqrtMobACC + er * (sqrtEVA - sqrtMobACC) / 100;
        int result = Math.max(nFormulaRes, 0);
        if (attackLevel > targetLevel) {
            double nDiff = 5 * (attackLevel - targetLevel);
            if (nDiff >= result)
                nDiff = result;
            result -= (int) nDiff;
        }
        return result;
    }
    public static double getRandom(int rand, double f0, double f1) {
        double realF1 = f1;
        double realF0 = f0;
        if (f0 > f1) {
            realF1 = f0;
            realF0 = f1;
        }
        double result;
        if (realF1 != realF0) {
            result =  realF0 + (double) (Integer.toUnsignedLong(rand) % 10000000) * (realF1 - realF0) / 9999999.0;
        } else {
            result = realF0;
        }
        return result;
    }
    public static int calcBaseDamage(int p1, int p2, int p3, int ad, double k) {
        return (int) ((double) (p3 + p2 + 4 * p1) / 100.0 * ((double) ad * k) + 0.5);
    }


}

