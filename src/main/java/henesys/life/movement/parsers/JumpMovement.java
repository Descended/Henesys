package henesys.life.movement.parsers;


import henesys.client.character.Char;
import henesys.connection.InPacket;
import henesys.connection.OutPacket;
import henesys.life.Life;

public class JumpMovement extends BaseMovement {
    public JumpMovement(InPacket inPacket, byte attr) {
        super();
        this.attr = attr;
        vPosition = inPacket.decodePosition();

        super.decode(inPacket);
    }

    @Override
    public void encode(OutPacket outPacket) {
        outPacket.encodePosition(getVPosition());

        super.encode(outPacket);
    }

    @Override
    public void applyTo(Char chr) {
        //TODO: chr.setPosition(getVPosition());
        chr.setMoveAction(getMoveAction());
    }

    @Override
    public void applyTo(Life life) {
        life.setvPosition(getVPosition());
        life.setMoveAction(getMoveAction());
    }
}
