package henesys.jobs.resistance;

import henesys.jobs.Job;

public class Citizen extends Job {
    public static final int CRYSTAL_THROW = 30001000;
    public static final int INFLITRATE = 30001001;
    public static final int POTION_MASTERY = 30000002;

    private int[] addedSkills = new int[] {
            CRYSTAL_THROW,
            INFLITRATE,
            POTION_MASTERY
    };
}
