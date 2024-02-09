package henesys.life.movement.parsers;

import henesys.client.character.Char;
import henesys.connection.InPacket;
import henesys.connection.OutPacket;
import henesys.life.Life;

public class FlyingBlockMovement extends BaseMovement {
    public FlyingBlockMovement(InPacket inPacket, byte attr) {
        super();
        this.attr = attr;
        position = inPacket.decodePosition();
        vPosition = inPacket.decodePosition();

        super.decode(inPacket);
    }

    @Override
    public void encode(OutPacket outPacket) {
        outPacket.encodePosition(getPosition());
        outPacket.encodePosition(getVPosition());

        super.encode(outPacket);
    }

    @Override
    public void applyTo(Char chr) {
        chr.setPosition(getPosition());
        chr.setMoveAction(getMoveAction());
    }

    @Override
    public void applyTo(Life life) {
        life.setPosition(getPosition());
        life.setvPosition(getVPosition());
        life.setMoveAction(getMoveAction());
    }
}
