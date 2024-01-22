package henesys.life.mob;


import henesys.util.Rect;

public class MobBuffInfo {
    private Rect addRect;
    private long addRectTime;
    private int fixedDamR;
    private long selfDot;
    private long selfDotTime;

    public MobBuffInfo() {
        // Empty
    }

    public int getFixedDamR() {
        return fixedDamR;
    }

    public long getAddRectTime() {
        return addRectTime;
    }

    public long getSelfDot() {
        return selfDot;
    }

    public long getSelfDotTime() {
        return selfDotTime;
    }

    public Rect getAddRect() {
        return addRect;
    }

    public void setAddRect(Rect addRect) {
        this.addRect = addRect;
    }

    public void setAddRectTime(long addRectTime) {
        this.addRectTime = addRectTime;
    }

    public void setFixedDamR(int fixedDamR) {
        this.fixedDamR = fixedDamR;
    }

    public void setSelfDot(long selfDot) {
        this.selfDot = selfDot;
    }

    public void setSelfDotTime(long selfDotTime) {
        this.selfDotTime = selfDotTime;
    }

    @Override
    public String toString() {
        return "MobBuffInfo{" +
                "addRect=" + addRect +
                ", addRectTime=" + addRectTime +
                ", fixedDamR=" + fixedDamR +
                ", selfDot=" + selfDot +
                ", selfDotTime=" + selfDotTime +
                '}';
    }
}
