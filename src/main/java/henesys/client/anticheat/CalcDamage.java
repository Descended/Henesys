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

