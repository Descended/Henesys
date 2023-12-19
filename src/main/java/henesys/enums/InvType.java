package henesys.enums;

import java.util.Arrays;

/**
 * Created on 11/23/2017.
 */
public enum InvType {
    EQUIPPED(-1),
    EQUIP(1),
    CONSUME(2),
    INSTALL(3),
    ETC(4),
    CASH(5);

    private final byte val;

    InvType(int val) {
        this((byte) val);
    }

    InvType(byte val) {
        this.val = val;
    }

    public byte getVal() {
        return val;
    }

    public static InvType getInvTypeByVal(int val) {
        return Arrays.stream(InvType.values()).filter(t -> t.getVal() == val).findFirst().orElse(null);
    }

    public static InvType getInvTypeByString(String subMap) {
        switch (subMap.toLowerCase()) {
            case "cash":
            case "pet":
                return CASH;
            case "consume":
            case "special":
            case "use":
                return CONSUME;
            case "etc":
                return ETC;
            case "install":
            case "setup":
                return INSTALL;
            case "eqp":
            case "equip":
                return EQUIP;
            default:
                return null;
        }
    }

    public boolean isStackable() {
        return this != EQUIP && this != EQUIPPED  && this != CASH ;
    }
}
