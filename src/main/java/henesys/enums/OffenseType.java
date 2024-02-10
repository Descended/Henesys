package henesys.enums;

public enum OffenseType {
    WZ_EDIT(0),
    MOB_VAC(1),
    MOB_COUNT(2),
    ITEM_VAC(3),
    FAST_ATTACK(4),
    FAST_HP_HEALING(5),
    FAST_MP_HEALING(6),
    DAMAGE_HACK(7),
    DISTANCE_HACK(8),
    PORTAL_DISTANCE(9),
    PACKET_EDIT(10),
    MISS_GODMODE(11),
    ;

    private final int value;
    OffenseType(int value) {
        this.value = value;
    }

    public int getVal() {
        return value;
    }

}
