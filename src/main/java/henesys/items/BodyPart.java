package henesys.items;

import java.util.Arrays;

/**
 * Created on 18/12/2023
 * @author Desc
 */
public enum BodyPart {
    BPBase(0),
    Hair(0),
    Hat(1),
    FaceAccessory(2),
    EyeAccessory(3),
    Earrings(4),
    Top(5),
    Overall(5), // Top and overall share the same body part
    Bottom(6),
    Shoes(7),
    Gloves(8),
    Cape(9),
    Shield(10), // Includes things such as katara, 2ndary
    Weapon(11),
    Ring1(12),
    Ring2(13),
    PetWear1(14),
    Ring3(15),
    Ring4(16),
    Pendant(17),
    TamingMob(18),
    Saddle(19),
    MobEquip(20),
    PetRingLabel(21),
    PetAbilItem(22),
    PetAbilMeso(23),
    PetAbilHpConsume(24),  // @KOOKIIE: PetAbil_HPConsume in v95
    PetAbilMpConsume(25),
     PetAbilSweepForDrop(26),
    PetAbilLongRange(27),
    PetAbilPickupOthers(28),
    PetRingQuote(29),
    PetWear2(30),
    PetRingLabel2(31),
    PetRingQuote2(32),
    PetAbilItem2(33),
    PetAbilMeso2(34),
    PetAbilSweepForDrop2(35),
    PetAbilLongRange2(36),
    PetAbilPickupOthers2(37),
    PetWear3(38),
    PetRingLabel3(39),
    PetRingQuote3(40),
    PetAbilItem3(41),
    PetAbilMeso3(42),
    PetAbilSweepForDrop3(43),
    PetAbilLongRange3(44),
    PetAbilPickupOthers3(45),
    PetAbilIgnoreItems1(46),
    PetAbilIgnoreItems2(47),
    PetAbilIgnoreItems3(48),
    Medal(49),
    Belt(50),
    Shoulder(51),
    // These are not in v95
//    PocketItem(52),
//    Android(53),
//    MechanicalHeart(54),
//    MonsterBook(55),  // Crusader Codex
//    Badge(56),
//    Emblem(61),
//    ExtendedPendant(65),
    Sticker(100),
    BPEnd(100),
    CBPBase(101), // CASH
//    PetConsumeHPItem(200),
//    PetConsumeMPItem(201),
    CBPEnd(1000),
    EvanBase(1000),
    EvanHat(1000),
    EvanPendant(1001),
    EvanWing(1002),
    EvanShoes(1003),
    EvanEnd(1004),
    MechBase(1100),
    MachineEngine(1100),
    MachineArm(1101),
    MachineLeg(1102),
    MachineFrame(1103),
    MachineTransistor(1104),
    MechEnd(1105),
    SlotIndexNotDefined(15440);

    private final int val;

    BodyPart(int val) {
        this.val = val;
    }

    public static BodyPart getByVal(int bodyPartVal) {
        return Arrays.stream(values()).filter(bp -> bp.getVal() == bodyPartVal).findAny().orElse(null);
    }

    public int getVal() {
        return val;
    }
}
