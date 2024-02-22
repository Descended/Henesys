package henesys.constants;

public class JobConstants {

    public static final int Beginner = 0;
    public static final int Noblesse = 1000;
    public static final int Legend = 2000;
    public static final int Citizen = 3000;

    public static final int Evan = 2001;

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

    public static boolean isEvan(short jobId) {
        return jobId / 100 == 22 || jobId == 2001;
    }
    public static int getEvanJobLevel(short jobId) {
        return switch (jobId) {
            case 2200, 2210 -> 1;
            case 2211, 2212, 2213 -> 2;
            case 2214, 2215, 2216 -> 3;
            case 2217, 2218 -> 4;
            default -> 0;
        };
    }
    public static boolean isDualBlade(int job) {
        return job / 10 == 43;
    }

    public static boolean isManager(int jobId) {
        return jobId == 800;
    }

    public static boolean isBeginnerJob(short jobId) {
        return jobId % 1000 == 0 || jobId == 2001;
    }
    // get_job_level
    public static int getJobLevel(int jobId)
    {
        int v1; // esi
        int v2; // esi
        int result; // eax
        if ( !(jobId % 100 == 0) || jobId == 2001 )
            return 1;
        if (isDualBlade(jobId))
            v1 = (jobId - 430) / 2;
        else
            v1 = jobId % 10;
        v2 = v1 + 2;
        if ( v2 >= 2 && (v2 <= 4 || v2 <= 10 && isEvan((short) jobId)) )
            result = v2;
        else
            result = 0;
        return result;
    }
}
