package henesys.life.mob.skill;

import henesys.skills.CharacterTemporaryStat;

import java.util.Arrays;

/**
 * Created on 3/18/2018.
 */
public enum MobSkillID {
    Unk(-1),
    PAD(0),
    PDR(1),
    MAD(2),
    MDR(3),
    ACC(4),
    EVA(5),
    Speed(6),
    Stun(7),
    Freeze(8),
    Poison(9),
    Seal(10),
    Darkness(11),
    PowerUp(12),
    MagicUp(13),
    PGuardUp(14),
    MGuardUp(15),
    Doom(16),
    Web(17),
    PImmune(18),
    MImmune(19),
    Showdown(20),
    HardSkin(21),
    Ambush(22),
    DamagedElemAttr(23),
    Venom(24),
    Blind(25),
    SealSkill(26),
    Burned(27),
    Dazzle(28),
    PCounter(29),
    MCounter(30),
    Disable(31),
    RiseByToss(32),
    BodyPressure(33),
    Weakness(34),
    TimeBomb(35),
    MagicCrash(36),
    HealByDamage(37);
    ;

    private final int val;

    MobSkillID(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public static MobSkillID getMobSkillIDByVal(int val) {
        return Arrays.stream(values()).filter(m -> m.getVal() == val).findAny().orElse(Unk);
    }

    public static void main(String[] args) {
        for (MobSkillID msi : values()) {
            StringBuilder s = new StringBuilder();
            boolean capital = true;
            for (char c : msi.toString().toCharArray()) {
                if (!capital && Character.isLetter(c)) {
                    c = Character.toLowerCase(c);
                }
                if (c != '_') {
                    s.append(c);
                    capital = false;
                } else {
                    capital = true;
                }
            }
            System.out.printf("%s(%d),%n", s.toString(), msi.val);
        }
    }

    public CharacterTemporaryStat getAffectedCTS() {
        switch (this) {
            case Seal:
                return CharacterTemporaryStat.Seal;
            case Darkness:
                return CharacterTemporaryStat.Darkness;
            case Weakness:
                return CharacterTemporaryStat.Weakness;
            case Stun:
                return CharacterTemporaryStat.Stun;
            case Poison:
                return CharacterTemporaryStat.Poison;
        }
        return null;
    }
}
