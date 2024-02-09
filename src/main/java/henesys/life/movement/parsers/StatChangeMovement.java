package henesys.life.movement.parsers;


import henesys.client.character.Char;
import henesys.connection.InPacket;
import henesys.connection.OutPacket;
import henesys.life.Life;
import henesys.util.Position;

public class StatChangeMovement extends BaseMovement {
    public StatChangeMovement(InPacket inPacket, byte attr) {
        super();
        this.attr = attr;
        this.position = new Position(0, 0);

        this.stat = inPacket.decodeByte();
    }

    @Override
    public void encode(OutPacket outPacket) {
        outPacket.encodeByte(getStat());
    }

    @Override
    public void applyTo(Char chr) {

    }

    @Override
    public void applyTo(Life life) {

    }
}
