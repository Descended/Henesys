package henesys.life;

import java.security.InvalidParameterException;

/**
 * Created on 1/2/2018.
 */
public enum DeathType {
    STAY(0),
    ANIMATION_DEATH(1),
    NO_ANIMATION_DEATH(2),
    INSTA_DEATH(3),
    NO_ANIMATION_DEATH_2(4),
    ANIMATION_DEATH_2(5),
    ANIMATION_DEATH_3(6),
    ;
    private final byte val;

    DeathType(byte val) {
        this.val = val;
    }

    DeathType(int val) {
        this((byte) val);
    }

    public byte getVal() {
        return val;
    }

    public static DeathType getByVal(int value) {
        if (value >= 0 && value < values().length) {
            return values()[value];
        }
        return null;
    }

    public static DeathType getByNodeName(String nodeName) {
        switch (nodeName) {
            case "die1":
                return ANIMATION_DEATH;
            case "die2":
                return ANIMATION_DEATH_2;
            case "dieF":
                return ANIMATION_DEATH_3;
            default:
                throw new InvalidParameterException(String.format("Invalid DeathType for node name: %s", nodeName));
        }
    }
}
