package henesys.jobs;

import henesys.client.character.CharacterStat;

import java.lang.reflect.InvocationTargetException;

import henesys.jobs.cygnus.*;
import henesys.jobs.explorer.*;
import henesys.jobs.gamemaster.*;
import henesys.jobs.legends.*;
import henesys.jobs.resistance.*;

/**
 * Created on 12/14/2017.
 */
public class JobManager {
    private static final Class[] jobClasses = new Class[] {
            Beginner.class,
            Warrior.class,
            Magician.class,
            Archer.class,
            Thief.class,
            Pirate.class,

            Noblesse.class,
            DawnWarrior.class,
            BlazeWizard.class,
            WindArcher.class,
            NightWalker.class,
            ThunderBreaker.class,

            Legend.class,
            Aran.class,
            Evan.class,

            Citizen.class,
            BattleMage.class,
            WildHunter.class,
            Mechanic.class,

            GM.class,
            SuperGM.class

    };

    private short id;

    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    public void setDefaultCharStatValues(CharacterStat characterStat) {
        characterStat.setLevel((byte) 1);
        characterStat.setStr((short) 12);
        characterStat.setDex((short) 5);
        characterStat.setInt((short) 4);
        characterStat.setLuk((short) 4);
        characterStat.setMaxHp(50);
        characterStat.setHp(50);
        characterStat.setMaxMp(5);
        characterStat.setMp(5);
    }

}
