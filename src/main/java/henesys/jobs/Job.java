package henesys.jobs;

public interface Job {

    void handleSkill(int skillId);

    void handleAttack(int skillId);

    void handleBuff(int skillId);

    boolean isHandlerOfSkill(int skillId);
}
