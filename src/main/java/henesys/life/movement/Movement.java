package henesys.life.movement;


import henesys.client.character.Char;
import henesys.connection.OutPacket;
import henesys.life.Life;
import henesys.util.Position;

/**
 * Created on 1/2/2018.
 * These classes + children/parents are basically the same as Mushy, credits to @MaxCloud.
 */
public interface Movement {
    void encode(OutPacket outPacket);

    Position getPosition();

    byte getCommand();

    byte getMoveAction();

    byte getForcedStop();

    byte getStat();

    short getFh();

    short getFootStart();

    short getDuration();

    Position getVPosition();

    Position getOffset();

    void applyTo(Char chr);

    void applyTo(Life life);
}
