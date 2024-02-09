package henesys.life.movement.parsers;

import henesys.client.character.Char;
import henesys.connection.InPacket;
import henesys.connection.OutPacket;
import henesys.life.Life;

public class TeleportMovement extends BaseMovement {
    public TeleportMovement(InPacket inPacket, byte attr) {
        super();
        this.attr = attr;
        position = inPacket.decodePosition();
        fh = inPacket.decodeShort();

        super.decode(inPacket);
    }

    @Override
    public void encode(OutPacket outPacket) {
        outPacket.encodePosition(getPosition());
        outPacket.encodeShort(getFh());

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
        life.setFh(getFh());
        life.setMoveAction(getMoveAction());
    }
}
