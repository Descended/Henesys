package henesys.enums;

import java.util.Arrays;


/**
 * @author Sjonnie
 * Created on 8/30/2018.
 */
public enum WeaponType {
    // WT_ Enum, but renamed some to make more sense
    None(0),
    Scepter(25),
    OneHandedSword(30),
    OneHandedAxe(31),
    OneHandedMace(32),
    Dagger(33),
    Katara(34),
    Secondary(35),
    Cane(36),
    Wand(37),
    Staff(38),
    Barehand(39),
    TwoHandedSword(40),
    TwoHandedAxe(41),
    TwoHandedMace(42),
    Spear(43),
    Polearm(44),
    Bow(45),
    Crossbow(46),
    Claw(47),
    Knuckle(48),
    Gun(49),
    ;

    private final int val;

    WeaponType(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public static WeaponType getByVal(int val) {
        return Arrays.stream(values()).filter(wt -> wt.getVal() == val).findAny().orElse(null);
    }
}
