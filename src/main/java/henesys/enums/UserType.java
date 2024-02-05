package henesys.enums;

import java.util.Arrays;

public enum UserType {
    Player(0, 0),
    Tester(1, 0x100),
    Intern(2, 0),
    GameMaster(3, 0x40),
    Admin(5, 0x80);

    private final int lvl;
    private final byte subGrade;

    UserType(int lvl, int subGrade) {
        this.lvl = lvl;
        this.subGrade = (byte) subGrade;
    }

    public static UserType getTypeByLvl(int lvl){
        return Arrays.stream(values()).filter(userType -> userType.getLvl() == lvl).findFirst().orElse(Player);
    }

    public int getLvl() {
        return lvl;
    }
}
