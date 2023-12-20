package henesys.skills;

import org.apache.logging.log4j.LogManager;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public enum CharacterTemporaryStat implements Comparator<CharacterTemporaryStat> {
    PAD(0),
    PDD(1),
    MAD(2),
    MDD(3),
    ACC(4),
    EVA(5),
    Craft(6),
    Speed(7),
    Jump(8),
    MagicGuard(9),
    DarkSight(10),
    Booster(11),
    PowerGuard(12),
    MaxHP(13),
    MaxMP(14),
    Invincible(15),
    SoulArrow(16),
    Stun(17),
    Poison(18),
    Seal(19),
    Darkness(20),
    ComboCounter(21),
    WeaponCharge(22),
    DragonBlood(23),
    HolySymbol(24),
    MesoUp(25),
    ShadowPartner(26),
    PickPocket(27),
    MesoGuard(28),
    Thaw(29),
    Weakness(30),
    Curse(31),
    Slow(32),
    Morph(33),
    Regen(34),
    BasicStatUp(35),
    Stance(36),
    SharpEyes(37),
    ManaReflection(38),
    Attract(39),
    SpiritJavelin(40),
    Infinity(41),
    Holyshield(42),
    HamString(43),
    Blind(44),
    Concentration(45),
    BanMap(46),
    MaxLevelBuff(47),
    MesoUpByItem(48),
    Ghost(49),
    Barrier(50),
    ReverseInput(51),
    ItemUpByItem(52),
    RespectPImmune(53),
    RespectMImmune(54),
    DefenseAtt(55),
    DefenseState(56),
    IncEffectHPPotion(57),
    IncEffectMPPotion(58),
    DojangBerserk(59),
    DojangInvincible(60),
    Spark(61),
    DojangShield(62),
    SoulMasterFinal(63),
    WindBreakerFinal(64),
    ElementalReset(65),
    WindWalk(66),
    EventRate(67),
    ComboAbilityBuff(68),
    ComboDrain(69),
    ComboBarrier(70),
    BodyPressure(71),
    SmartKnockback(72),
    RepeatEffect(73),
    ExpBuffRate(74),
    StopPortion(75),
    StopMotion(76),
    Fear(77),
    EvanSlow(78),
    MagicShield(79),
    MagicResistance(80),
    SoulStone(81),
    Flying(82),
    Frozen(83),
    AssistCharge(84),
    Enrage(85),
    SuddenDeath(86),
    NotDamaged(87),
    FinalCut(88),
    ThornsEffect(89),
    SwallowAttackDamage(90),
    MorewildDamageUp(91),
    Mine(92),
    EMHP(93),
    EMMP(94),
    EPAD(95),
    EPDD(96),
    EMDD(97),
    Guard(98),
    SafetyDamage(99),
    SafetyAbsorb(100),
    Cyclone(101),
    SwallowCritical(102),
    SwallowMaxMP(103),
    SwallowDefence(104),
    SwallowEvasion(105),
    Conversion(106),
    Revive(107),
    Sneak(108),
    Mechanic(109),
    Aura(110),
    DarkAura(111),
    BlueAura(112),
    YellowAura(113),
    SuperBody(114),
    MorewildMaxHP(115),
    Dice(116),
    BlessingArmor(117),
    DamR(118),
    TeleportMasteryOn(119),
    CombatOrders(120),
    Beholder(121),
    EnergyCharged(122),
    Dash_Speed(123),
    Dash_Jump(124),
    RideVehicle(125),
    PartyBooster(126),
    GuidedBullet(127),
    Undead(128),
    SummonBomb(129);;

    private int bitPos;
    private int val;
    private int pos;
    public static final int length = 17;
    private static final org.apache.logging.log4j.Logger log = LogManager.getRootLogger();

    private static final List<CharacterTemporaryStat> ORDER = Arrays.asList(
            PAD, PDD, MAD, MDD, ACC, EVA, Craft, Speed, Jump, EMHP, EMMP, EPAD, EPDD, EMDD, MagicGuard, DarkSight,
            Booster, PowerGuard, Guard, SafetyDamage, SafetyAbsorb, MaxHP, MaxMP, Invincible, SoulArrow, Stun,
            Poison, Seal, Darkness, ComboCounter, WeaponCharge, DragonBlood, HolySymbol, MesoUp, ShadowPartner,
            PickPocket, MesoGuard, Thaw, Weakness, Curse, Slow, Morph, Ghost, Regen, BasicStatUp, Stance, SharpEyes,
            ManaReflection, Attract, SpiritJavelin, Infinity, Holyshield, HamString, Blind, Concentration, BanMap,
            MaxLevelBuff, Barrier, DojangShield, ReverseInput, MesoUpByItem, ItemUpByItem, RespectPImmune,
            RespectMImmune, DefenseAtt, DefenseState, DojangBerserk, DojangInvincible, Spark, SoulMasterFinal,
            WindBreakerFinal, ElementalReset, WindWalk, EventRate, ComboAbilityBuff, ComboDrain, ComboBarrier,
            BodyPressure, SmartKnockback, RepeatEffect, ExpBuffRate, IncEffectHPPotion, IncEffectMPPotion, StopPortion,
            StopMotion, Fear, EvanSlow, MagicShield, MagicResistance, SoulStone, Flying, Frozen, AssistCharge, Enrage,
            SuddenDeath, NotDamaged, FinalCut, ThornsEffect, SwallowAttackDamage, MorewildDamageUp, Mine, Cyclone,
            SwallowCritical, SwallowMaxMP, SwallowDefence, SwallowEvasion, Conversion, Revive, Sneak, Mechanic,
            Aura, DarkAura, BlueAura, YellowAura, SuperBody, MorewildMaxHP, Dice, BlessingArmor, DamR,
            TeleportMasteryOn, CombatOrders, Beholder, SummonBomb
    );

    private static final List<CharacterTemporaryStat> REMOTE_ORDER = Arrays.asList(
            Speed, ComboCounter, WeaponCharge, Stun, Darkness, Seal, Weakness, Curse, Poison, ShadowPartner, Morph,
            Ghost, Attract, SpiritJavelin, BanMap, Barrier, DojangShield, ReverseInput, RespectPImmune, RespectMImmune,
            DefenseAtt, DefenseState, RepeatEffect, StopPortion, StopMotion, Fear, MagicShield, Frozen, SuddenDeath,
            FinalCut, Cyclone, Mechanic, DarkAura, BlueAura, YellowAura
    );


    CharacterTemporaryStat(int val, int pos) {
        this.val = val;
        this.pos = pos;
    }

    CharacterTemporaryStat(int bitPos) {
        this.bitPos = bitPos;
        this.val = 1 << (31 - bitPos % 32);
        this.pos = bitPos / 32;
    }

    public static CharacterTemporaryStat getByBitPos(int parseInt) {
        return
                Arrays.asList(values()).stream()
                        .filter(characterTemporaryStat -> characterTemporaryStat.getBitPos() == parseInt)
                        .collect(Collectors.toList()).get(0);
    }

    public boolean isEncodeInt() {
        switch (this) {
            case ShadowPartner:
                return true;
            default:
                return false;
        }
    }

    public boolean isIndie() {
        return toString().toLowerCase().contains("indie");
    }

    public boolean isMovingEffectingStat() {
        switch (this) {
            case Speed:
            case Jump:
            case Stun:
            case Weakness:
            case Slow:
            case Morph:
            case Ghost:
            case BasicStatUp:
            case Attract:
            case EnergyCharged:
            case Flying:
            case Frozen:
            case Mechanic:
            case RideVehicle:
                return true;
            default:
                return false;
        }
    }

    public int getVal() {
        return val;
    }

    public int getPos() {
        return pos;
    }

    public int getOrder() {
        return ORDER.indexOf(this);
    }

    public int getRemoteOrder() {
        return REMOTE_ORDER.indexOf(this);
    }

    public boolean isRemoteEncode4() {
        switch (this) {
            case RespectPImmune:
            case RespectMImmune:
            case DefenseAtt:
            case DefenseState:
            case MagicShield:
                return true;
            default:
                return false;
        }
    }

    public boolean isRemoteEncode1() {
        switch (this) {
            case Speed:
            case Cyclone:
            case ComboCounter:
                return true;
            default:
                return false;
        }
    }

    public boolean isNotEncodeReason() {
        switch (this) {
            case Speed:
            case Ghost:
            case RespectPImmune:
            case RespectMImmune:
            case DefenseAtt:
            case DefenseState:
            case MagicShield:
            case Cyclone:
            case ComboCounter:
                return true;
            default:
                return false;
        }
    }

    @Override
    public int compare(CharacterTemporaryStat o1, CharacterTemporaryStat o2) {
        if (o1.getPos() < o2.getPos()) {
            return -1;
        } else if (o1.getPos() > o2.getPos()) {
            return 1;
        }
        // hacky way to circumvent java not having unsigned ints
        int o1Val = o1.getVal();
        if (o1Val == 0x8000_0000) {
            o1Val = Integer.MAX_VALUE;
        }
        int o2Val = o2.getVal();
        if (o2Val == 0x8000_0000) {
            o2Val = Integer.MAX_VALUE;
        }

        if (o1Val > o2Val) {
            // bigger value = earlier in the int => smaller
            return -1;
        } else if (o1Val < o2Val) {
            return 1;
        }
        return 0;
    }

    public int getBitPos() {
        return bitPos;
    }
}
