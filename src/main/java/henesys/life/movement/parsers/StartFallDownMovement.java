package henesys.life.movement.parsers;

import henesys.client.character.Char;
import henesys.connection.InPacket;
import henesys.connection.OutPacket;
import henesys.life.Life;
import henesys.util.Position;

public class StartFallDownMovement extends BaseMovement {
    public StartFallDownMovement(InPacket inPacket, byte attr) {
        super();
        this.attr = attr;
        this.position = new Position(0, 0);
        vPosition = inPacket.decodePosition();
        footStart = inPacket.decodeShort();

        super.decode(inPacket);
    }

    @Override
    public void encode(OutPacket outPacket) {
        outPacket.encodePosition(getVPosition());
        outPacket.encodeShort(getFootStart());

        super.encode(outPacket);
    }

    @Override
    public void applyTo(Char chr) {
        chr.setPosition(getPosition());
        chr.setFoothold(getFh());
        chr.setMoveAction(getMoveAction());
    }

    @Override
    public void applyTo(Life life) {
        life.setPosition(getPosition());
        life.setvPosition(getVPosition());
        life.setFh(getFh());
        life.setMoveAction(getMoveAction());
    }

}
