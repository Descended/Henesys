package henesys.life.movement.parsers;

import henesys.connection.InPacket;
import henesys.connection.OutPacket;
import henesys.life.movement.Movement;
import henesys.life.movement.MovementPathAttr;
import henesys.util.Position;

public abstract class BaseMovement implements Movement {
    protected byte command;
    protected byte moveAction;
    protected byte forcedStop;
    protected byte stat;

    protected short fh;
    protected short footStart;
    protected short elapse;

    protected Position position;
    protected Position vPosition;
    protected Position offset;
    protected byte attr = MovementPathAttr.NONE;

    @Override
    public byte getCommand() {
        return command;
    }

    @Override
    public byte getMoveAction() {
        return moveAction;
    }

    @Override
    public byte getForcedStop() {
        return forcedStop;
    }

    @Override
    public byte getStat() {
        return stat;
    }

    @Override
    public short getFh() {
        return fh;
    }

    @Override
    public short getFootStart() {
        return footStart;
    }

    @Override
    public Position getPosition() {
        return position;
    }

    @Override
    public Position getVPosition() {
        return vPosition;
    }

    @Override
    public Position getOffset() {
        return offset;
    }

    @Override
    public short getDuration() {
        return elapse;
    }

    public byte getAttr() {
        return attr;
    }

    public void setCommand(byte command) {
        this.command = command;
    }

    public void setMoveAction(byte moveAction) {
        this.moveAction = moveAction;
    }

    public void setForcedStop(byte forcedStop) {
        this.forcedStop = forcedStop;
    }

    public void setStat(byte stat) {
        this.stat = stat;
    }

    public void setFh(short fh) {
        this.fh = fh;
    }

    public void setFootStart(short footStart) {
        this.footStart = footStart;
    }

    public short getElapse() {
        return elapse;
    }

    public void setElapse(short elapse) {
        this.elapse = elapse;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getvPosition() {
        return vPosition;
    }

    public void setvPosition(Position vPosition) {
        this.vPosition = vPosition;
    }

    public void setOffset(Position offset) {
        this.offset = offset;
    }

    public void setAttr(byte attr) {
        this.attr = attr;
    }

    public void decode(InPacket inPacket) {
        moveAction = inPacket.decodeByte();
        elapse = inPacket.decodeShort();
    }

    public void encode(OutPacket outPacket) {
        outPacket.encodeByte(getMoveAction());
        outPacket.encodeShort(getElapse());
    }
}
