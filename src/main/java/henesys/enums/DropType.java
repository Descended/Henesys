package henesys.enums;

public enum DropType {
    Mesos(0),
    Item(1);

    private final byte val;

    DropType(int val) {
        this.val = (byte) val;
    }

    public byte getVal() {
        return val;
    }
}
