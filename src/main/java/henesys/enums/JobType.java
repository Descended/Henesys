package henesys.enums;

import henesys.constants.JobConstants;
import henesys.jobs.Job;

import java.util.Arrays;

public enum JobType {
    Resistance(0),
    Adventurer(1),
    Cygnus(2),
    Aran(3),
    Evan(4);

    private final int val;

    JobType(int val) {
        this.val = val;
    }

    public static JobType getTypeByVal(int val){
        return Arrays.stream(JobType.values()).filter(jobType -> jobType.getVal() == val).findFirst().orElse(Adventurer);
    }

    public int getVal() {
        return val;
    }

    public int getStartJobByType(){
        int job = 0;
        switch (this){
            case Resistance -> job = JobConstants.Citizen;
            case Cygnus -> job = JobConstants.Noblesse;
            case Aran -> job = JobConstants.Legend;
            case Evan -> job = JobConstants.Evan;
        }
        return job;
    }
}
