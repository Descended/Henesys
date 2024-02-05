package henesys.life.mob;

import henesys.connection.OutPacket;

public class BurnedInfo {
    private int chrId;
    private int skillId;
    private int dmg;
    private int interval;
    private int end;
    private int dotCount;

    public void encode(OutPacket outPacket) {
        outPacket.encodeInt(chrId);
        outPacket.encodeInt(skillId);
        outPacket.encodeInt(dmg);
        outPacket.encodeInt(interval);
        outPacket.encodeInt(end);
        outPacket.encodeInt(dotCount);
    }
}
