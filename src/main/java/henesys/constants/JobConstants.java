package henesys.constants;

public class JobConstants {

    public static boolean isExtendSpJob(short jobId) {
        return isResistance(jobId) || isThirdJob(jobId) && isAdventurerMage(jobId) || isEvanJr(jobId);
    }

    public static boolean isResistance(short jobId) {
        return jobId / 1000 == 3;
    }

    public static boolean isThirdJob(short jobId) {
        return jobId / 1000 == 2;
    }

    public static boolean isAdventurerMage(short jobId) {
        return jobId == 200
                || jobId == 210
                || jobId == 211
                || jobId == 212
                || jobId == 220
                || jobId == 221
                || jobId == 222
                || jobId == 230
                || jobId == 231
                || jobId == 232;
    }

    public static boolean isEvanJr(short jobId) {
        return jobId == 2001;
    }

}
