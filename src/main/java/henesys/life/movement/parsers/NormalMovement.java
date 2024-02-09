package henesys.life.movement.parsers;


import henesys.client.character.Char;
import henesys.connection.InPacket;
import henesys.connection.OutPacket;
import henesys.life.Life;
import henesys.life.movement.MovementPathAttr;

public class NormalMovement extends BaseMovement {
    public NormalMovement(InPacket inPacket, byte attr) {
        super();
        this.attr = attr;

        position = inPacket.decodePosition();
        vPosition = inPacket.decodePosition();
        fh = inPacket.decodeShort();

        if(attr == MovementPathAttr.FALL_DOWN){
            footStart = inPacket.decodeShort();
        }
        offset = inPacket.decodePosition();

        super.decode(inPacket);
    }

    @Override
    public void encode(OutPacket outPacket) {
        outPacket.encodePosition(getPosition());
        outPacket.encodePosition(getVPosition());
        outPacket.encodeShort(getFh());
        if (attr == MovementPathAttr.FALL_DOWN) {
            outPacket.encodeShort(getFootStart());
        }
        outPacket.encodePosition(getOffset());

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
