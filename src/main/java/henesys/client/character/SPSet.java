package henesys.client.character;

/**
 * Created on 2/18/2017.
 */
public class SPSet {
    private int id;
    private byte jobLevel;
    private byte sp;

    public SPSet() {
    }

    public SPSet(byte jobLevel, byte sp) {
        this.jobLevel = jobLevel;
        this.sp = sp;
    }

    public void setJobLevel(byte jobLevel) {
        this.jobLevel = jobLevel;
    }

    public void setSp(byte sp) {
        this.sp = sp;
    }

    public byte getJobLevel() {
        return jobLevel;
    }

    public byte getSp() {
        return sp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
