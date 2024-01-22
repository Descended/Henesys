package henesys.life.mob;

public enum MobStat {
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
    private final int pos;
    private int bitPos;

    MobStat(int val, int pos) {
        this.val = val;
        this.pos = pos;
    }

    MobStat(int bitPos) {
        this.bitPos = bitPos;
        this.val = 1 << (31 - bitPos % 32);
        this.pos = bitPos / 32;
    }

    public int getPos() {
        return pos;
    }

    public int getVal() {
        return val;
    }

    public boolean isMovementAffectingStat() {
        switch (this) {
            case Speed:
            case Stun:
            case Freeze:
            case RiseByToss:
                return true;
            default:
                return false;
        }
    }

    public int getBitPos() {
        return bitPos;
    }
}
