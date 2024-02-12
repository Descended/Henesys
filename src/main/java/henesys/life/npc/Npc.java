package henesys.life.npc;

import henesys.client.character.Char;
import henesys.connection.OutPacket;
import henesys.connection.packet.NpcPool;
import henesys.life.Life;
import henesys.world.field.Field;

import java.util.HashMap;
import java.util.Map;

public class Npc extends Life {

    private boolean enabled = true;
    private final Map<Integer, String> scripts = new HashMap<>();

    private boolean move;

    private int trunkGet;
    private int trunkPut;

    public Npc(int templateId) {
        super(templateId);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Map<Integer, String> getScripts() {
        return scripts;
    }

    public boolean isMove() {
        return move;
    }

    public void setMove(boolean move) {
        this.move = move;
    }

    public int getTrunkGet() {
        return trunkGet;
    }

    public void setTrunkGet(int trunkGet) {
        this.trunkGet = trunkGet;
    }

    public int getTrunkPut() {
        return trunkPut;
    }

    public void setTrunkPut(int trunkPut) {
        this.trunkPut = trunkPut;
    }

    public void encode(OutPacket outPacket) {
        // CNpc::Init
        outPacket.encodePosition(getPosition());
        outPacket.encodeByte(!isFlip());
        outPacket.encodeShort(getFh());
        outPacket.encodeShort(getRx0()); // rgHorz.low
        outPacket.encodeShort(getRx1()); // rgHorz.high
        outPacket.encodeByte(isEnabled());
    }

    @Override
    public void broadcastSpawnPacket(Char onlyChar) {
        Field field = getField();
        for (Char chr : field.getChars()) {
            chr.getClient().write(NpcPool.npcEnterField(this));
        }
    }
}
