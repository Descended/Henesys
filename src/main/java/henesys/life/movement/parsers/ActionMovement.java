package henesys.life.movement.parsers;

import henesys.client.character.Char;
import henesys.connection.InPacket;
import henesys.connection.OutPacket;
import henesys.life.Life;
import henesys.util.Position;

public class ActionMovement extends BaseMovement {
    public ActionMovement(InPacket inPacket, byte attr) {
        super();
        this.attr = attr;
        this.position = new Position(0, 0);
        super.decode(inPacket);
    }

    @Override
    public void encode(OutPacket outPacket) {
        super.encode(outPacket);
    }

    @Override
    public void applyTo(Char chr) {
        chr.setMoveAction(moveAction);
    }

    @Override
    public void applyTo(Life life) {
        life.setMoveAction(moveAction);
    }
}
