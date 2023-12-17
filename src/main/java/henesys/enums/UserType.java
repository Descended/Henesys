package henesys.enums;

public enum UserType {
    Player(0),
    Tester(1 << 5),
    Intern(1 << 3),
    GameMaster(1 << 4),
    Admin(1 << 4);

    private final int val;

    UserType(int val) {
        this.val = val;
    }

    public static UserType getByVal(int val) {
        for (UserType type : values()) {
            if (type.getVal() == val) {
                return type;
            }
        }
        return null;
    }
    public int getVal() {
        return val;
    }
}
