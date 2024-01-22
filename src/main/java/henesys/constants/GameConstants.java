package henesys.constants;

import henesys.util.Util;

public class GameConstants {

    public static final int RANDOM_EQUIP_UNIQUE_CHANCE = 1; // out of a 100
    public static final int RANDOM_EQUIP_EPIC_CHANCE = 3; // out of a 100
    public static final int RANDOM_EQUIP_RARE_CHANCE = 8; // out of a 100

    public static final int DEFAULT_FIELD_MOB_CAPACITY = 100;
    public static final double DEFAULT_FIELD_MOB_RATE_BY_MOBGEN_COUNT = 1.5;
    public static final int TRUNK_SLOT_COUNT = 4;
    public static final boolean OVERRIDE_MAX_CONSUMABLES = true;
    public static boolean isValidName(String name) {
        return name.length() >= 4 && name.length() <= 13 && Util.isDigitLetterString(name);
    }
}
