package henesys.life.movement;

import henesys.client.character.Char;
import henesys.connection.Encodable;
import henesys.connection.InPacket;
import henesys.connection.OutPacket;
import henesys.life.Life;
import henesys.life.movement.parsers.*;
import henesys.util.Position;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static henesys.life.movement.MovementPathAttr.*;

public class MovementInfo implements Encodable {



    private Position oldPos;
    private Position oldVPos;
    private List<Movement> movements = new ArrayList<>();

    private static final Logger log = LogManager.getLogger(MovementInfo.class);
    @Override
    public void encode(OutPacket outPacket) {

    }

    public MovementInfo(InPacket inPacket) {
        decode(inPacket);
    }

    public void decode(InPacket inPacket) {
        oldPos = inPacket.decodePosition();
        oldVPos = inPacket.decodePosition();
        movements = parseMovement(inPacket);
    }


    // Taken from SpringStory (Doriyan)
    private static List<Movement> parseMovement(InPacket inPacket) {
        List<Movement> movements = new ArrayList<>();
        byte size = inPacket.decodeByte();
        for (int i = 0; i < size; i++) {
            byte attr = inPacket.decodeByte();
            //  CMovePath::Decode -
            switch (attr) {
                case NORMAL, HANG_ON_BACK, FALL_DOWN, WINGS, MOB_ATK_RUSH, MOB_ATK_RUSH_STOP ->
                        movements.add(new NormalMovement(inPacket, attr));
                case JUMP, IMPACT, START_WINGS, MOB_TOSS, DASH_SLIDE, MOB_LADDER,
                        MOB_RIGHT_ANGLE, MOB_STOP_NODE_START, MOB_BEFORE_NODE ->
                        movements.add(new JumpMovement(inPacket, attr));
                case FLASH_JUMP, ROCKET_BOOSTER, BACK_STEP_SHOT, MOB_POWER_KNOCK_BACK,
                        VERTICAL_JUMP, CUSTOM_IMPACT, COMBAT_STEP, HIT, TIME_BOMB_ATK,
                        SNOW_BALL_TOUCH, BUFF_ZONE_EFFECT -> movements.add(new ActionMovement(inPacket, attr));
                case IMMEDIATE, TELEPORT, ASSAULTER, ASSASSINATION, RUSH, SIT_DOWN ->
                        movements.add(new TeleportMovement(inPacket, attr));
                case STAT_CHANGE -> movements.add(new StatChangeMovement(inPacket, attr));
                case START_FALL_DOWN -> movements.add(new StartFallDownMovement(inPacket, attr));
                case FLYING_BLOCK -> movements.add(new FlyingBlockMovement(inPacket, attr));
                default -> log.warn("Movement not handled: " + attr);
            }
        }
        return movements;
    }

    public void applyTo(Char chr) {
        for (Movement m : getMovements()) {
            m.applyTo(chr);
        }
    }

    public void applyTo(Life life) {
        for (Movement m : getMovements()) {
            m.applyTo(life);
        }
    }

    public Position getOldPos() {
        return oldPos;
    }

    public void setOldPos(Position oldPos) {
        this.oldPos = oldPos;
    }

    public Position getOldVPos() {
        return oldVPos;
    }

    public void setOldVPos(Position oldVPos) {
        this.oldVPos = oldVPos;
    }

    public List<Movement> getMovements() {
        return movements;
    }

    public void setMovements(List<Movement> movements) {
        this.movements = movements;
    }
}
