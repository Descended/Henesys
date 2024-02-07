package henesys.loaders;

import henesys.ServerConstants;
import henesys.life.DeathType;
import henesys.life.mob.*;
import henesys.life.mob.skill.MobSkill;
import henesys.skills.Option;
import henesys.util.Rect;
import henesys.util.Util;
import henesys.util.XMLApi;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

import static henesys.life.mob.skill.MobSkillID.*;

/**
 * Created on 12/30/2017.
 */
public class MobData {
    private static final boolean LOG_UNKS = false;
    private static final org.apache.logging.log4j.Logger log = LogManager.getRootLogger();

    private static Map<Integer, Mob> mobs = new HashMap<>();

    public static Map<Integer, Mob> getMobs() {
        return mobs;
    }

    public static void addMob(Mob mob) {
        getMobs().put(mob.getTemplateId(), mob);
    }

    public static void generateDatFiles() {
        log.info("Started generating mob data.");
        long start = System.currentTimeMillis();
        loadMobsFromWz();
//        QuestData.linkMobData();
        saveToFile(ServerConstants.DAT_DIR + "/mobs");
        log.info(String.format("Completed generating mob data in %dms.", System.currentTimeMillis() - start));
    }
    public static Mob getMobById(int id) {
        Mob mob = getMobs().get(id);
        if (mob == null) {
            mob = loadMobFromFile(id);
        }
        return mob;
    }

    public static Mob getMobDeepCopyById(int id) {
        Mob from = getMobById(id);
        Mob copy = null;
        if (from != null) {
            copy = from.deepCopy();
        }
        return copy;
    }
    private static void saveToFile(String dir) {
        Util.makeDirIfAbsent(dir);
        for (Mob mob : getMobs().values()) {
            File file = new File(dir + "/" + mob.getTemplateId() + ".dat");
            try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file))) {
                ForcedMobStat fms = mob.getForcedMobStat();
                MobTemporaryStat mts = mob.getTemporaryStat();
                dataOutputStream.writeInt(mob.getTemplateId());
                dataOutputStream.writeInt(fms.getLevel());
                dataOutputStream.writeInt(mob.getFirstAttack());
                dataOutputStream.writeLong(fms.getMaxHP());
                dataOutputStream.writeLong(fms.getMaxMP());
                dataOutputStream.writeInt(fms.getPad());
                dataOutputStream.writeInt(fms.getPdr());
                dataOutputStream.writeInt(fms.getMad());
                dataOutputStream.writeInt(fms.getMdr());
                dataOutputStream.writeInt(fms.getAcc());
                dataOutputStream.writeInt(fms.getEva());
                dataOutputStream.writeInt(fms.getPushed());
                dataOutputStream.writeLong(fms.getExp());
                dataOutputStream.writeInt(mob.getSummonType());
                dataOutputStream.writeUTF(mob.getMobType());
                dataOutputStream.writeInt(fms.getSpeed());
                dataOutputStream.writeDouble(mob.getFs());
                dataOutputStream.writeUTF(mob.getElemAttr());
                dataOutputStream.writeInt(mob.getHpTagColor());
                dataOutputStream.writeInt(mob.getHpTagBgcolor());

                dataOutputStream.writeInt(mob.getReviveRespawnDelays().size());
                for(Map.Entry<DeathType, Long> entry : mob.getReviveRespawnDelays().entrySet()) {
                    dataOutputStream.writeInt(entry.getKey().getVal());
                    dataOutputStream.writeLong(entry.getValue());
                }

                dataOutputStream.writeBoolean(mob.isHPgaugeHide());
                dataOutputStream.writeBoolean(mob.isBoss());
                dataOutputStream.writeBoolean(mob.isUndead());
                dataOutputStream.writeBoolean(mob.isNoRegen());
                dataOutputStream.writeBoolean(mob.isInvincible());
                dataOutputStream.writeBoolean(mob.isHideName());
                dataOutputStream.writeBoolean(mob.isHideHP());
                dataOutputStream.writeBoolean(mob.isNoFlip());
                dataOutputStream.writeBoolean(mob.isPublicReward());
                dataOutputStream.writeBoolean(mob.isIgnoreFieldOut());
                dataOutputStream.writeBoolean(mob.isNoDoom());
                dataOutputStream.writeBoolean(mob.isKnockback());
                dataOutputStream.writeBoolean(mob.isRemoveQuest());
                dataOutputStream.writeInt(mob.getRareItemDropLevel());
                dataOutputStream.writeInt(mob.getHpRecovery());
                dataOutputStream.writeInt(mob.getMpRecovery());
                dataOutputStream.writeInt(mob.getMbookID());
                dataOutputStream.writeInt(mob.getChaseSpeed());
                dataOutputStream.writeInt(mob.getExplosiveReward());
                dataOutputStream.writeInt(mob.getFlySpeed());
                dataOutputStream.writeInt(mob.getSummonEffect());
                dataOutputStream.writeInt(mob.getFixedDamage());
                dataOutputStream.writeInt(mob.getRemoveAfter());
                dataOutputStream.writeInt(mob.getCoolDamage());
                dataOutputStream.writeInt(mob.getCoolDamageProb());
                dataOutputStream.writeBoolean(mob.isEscortMob());
                dataOutputStream.writeShort(mob.getRevives().size());
                for (int i : mob.getRevives()) {
                    dataOutputStream.writeInt(i);
                }
//                dataOutputStream.writeShort(mob.getSkills().size());
//                dataOutputStream.writeShort(mob.getAttacks().size());
//                List<MobSkill> all = mob.getSkills();
//                all.addAll(mob.getAttacks());
//                for (MobSkill ms : all) {
//                    dataOutputStream.writeInt(ms.getSkillSN());
//                    dataOutputStream.writeInt(ms.getSkillID());
//                    dataOutputStream.writeByte(ms.getAction());
//                    dataOutputStream.writeInt(ms.getLevel());
//                    dataOutputStream.writeInt(ms.getEffectAfter());
//                    dataOutputStream.writeInt(ms.getSkillAfter());
//                    dataOutputStream.writeBoolean(ms.isOnlyFsm());
//                    dataOutputStream.writeBoolean(ms.isOnlyOtherSkill());
//                    dataOutputStream.writeBoolean(ms.isDoFirst());
//                    dataOutputStream.writeBoolean(ms.isAfterDead());
//                    dataOutputStream.writeInt(ms.getAfterAttack());
//                    dataOutputStream.writeInt(ms.getAfterAttackCount());
//                    dataOutputStream.writeInt(ms.getAfterDelay());
//                    dataOutputStream.writeInt(ms.getFixDamR());
//                    dataOutputStream.writeInt(ms.getCoolTime());
//                    dataOutputStream.writeUTF(ms.getInfo());
//                    dataOutputStream.writeUTF(ms.getSpeak());
//                }
//                dataOutputStream.writeInt(mts.getNewOptionsByMobStat(MobStat.PImmune) != null ? mts.getNewOptionsByMobStat(MobStat.PImmune).nOption : 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Mob loadMobFromFile(int id) {
        File file = new File(ServerConstants.DAT_DIR + "/mobs/" + id + ".dat");
        if (!file.exists()) {
            return null;
        }
        Mob mob = null;
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file))) {
            mob = new Mob(dataInputStream.readInt());
            ForcedMobStat fms = mob.getForcedMobStat();
            fms.setLevel(dataInputStream.readInt());
            mob.setFirstAttack(dataInputStream.readInt());
            fms.setMaxHP(dataInputStream.readLong());
            fms.setMaxMP(dataInputStream.readLong());
            fms.setPad(dataInputStream.readInt());
            fms.setPdr(dataInputStream.readInt());
            fms.setMad(dataInputStream.readInt());
            fms.setMdr(dataInputStream.readInt());
            fms.setAcc(dataInputStream.readInt());
            fms.setEva(dataInputStream.readInt());
            fms.setPushed(dataInputStream.readInt());
            fms.setExp(dataInputStream.readLong());
            mob.setSummonType(dataInputStream.readInt());
            mob.setMobType(dataInputStream.readUTF());
            fms.setSpeed(dataInputStream.readInt());
            mob.setFs(dataInputStream.readDouble());
            mob.setElemAttr(dataInputStream.readUTF());
            mob.setHpTagColor(dataInputStream.readInt());
            mob.setHpTagBgcolor(dataInputStream.readInt());

            int reviveRespawnDelaySize = dataInputStream.readInt();
            HashMap<DeathType, Long> reviveRespawnDelays = new HashMap<>();
            for(int i = 0; i < reviveRespawnDelaySize; i++) {
                reviveRespawnDelays.put(DeathType.getByVal(dataInputStream.readInt()), dataInputStream.readLong());
            }
            mob.setReviveRespawnDelays(reviveRespawnDelays);

            mob.setHPgaugeHide(dataInputStream.readBoolean());
            mob.setBoss(dataInputStream.readBoolean());
            mob.setUndead(dataInputStream.readBoolean());
            mob.setNoRegen(dataInputStream.readBoolean());
            mob.setInvincible(dataInputStream.readBoolean());
            mob.setHideName(dataInputStream.readBoolean());
            mob.setHideHP(dataInputStream.readBoolean());
            mob.setNoFlip(dataInputStream.readBoolean());
            mob.setPublicReward(dataInputStream.readBoolean());
            mob.setIgnoreFieldOut(dataInputStream.readBoolean());
            mob.setNoDoom(dataInputStream.readBoolean());
            mob.setKnockback(dataInputStream.readBoolean());
            mob.setRemoveQuest(dataInputStream.readBoolean());
            mob.setRareItemDropLevel(dataInputStream.readInt());
            mob.setHpRecovery(dataInputStream.readInt());
            mob.setMpRecovery(dataInputStream.readInt());
            mob.setMbookID(dataInputStream.readInt());
            mob.setChaseSpeed(dataInputStream.readInt());
            mob.setExplosiveReward(dataInputStream.readInt());
            mob.setFlySpeed(dataInputStream.readInt());
            mob.setSummonEffect(dataInputStream.readInt());
            mob.setFixedDamage(dataInputStream.readInt());
            mob.setRemoveAfter(dataInputStream.readInt());
            mob.setCoolDamage(dataInputStream.readInt());
            mob.setCoolDamageProb(dataInputStream.readInt());
            mob.setEscortMob(dataInputStream.readBoolean());
            short size = dataInputStream.readShort();
            for (int i = 0; i < size; i++) {
                mob.addRevive(dataInputStream.readInt());
            }
//            short skillSize = dataInputStream.readShort();
//            short attackSize = dataInputStream.readShort();
//            for (int i = 0; i < skillSize + attackSize; i++) {
//                MobSkill ms = new MobSkill();
//                ms.setSkillSN(dataInputStream.readInt());
//                ms.setSkillID(dataInputStream.readInt());
//                ms.setAction(dataInputStream.readByte());
//                ms.setLevel(dataInputStream.readInt());
//                ms.setEffectAfter(dataInputStream.readInt());
//                ms.setSkillAfter(dataInputStream.readInt());
//                ms.setOnlyFsm(dataInputStream.readBoolean());
//                ms.setOnlyOtherSkill(dataInputStream.readBoolean());
//                ms.setDoFirst(dataInputStream.readBoolean());
//                ms.setAfterDead(dataInputStream.readBoolean());
//                ms.setAfterAttack(dataInputStream.readInt());
//                ms.setAfterAttackCount(dataInputStream.readInt());
//                ms.setAfterDelay(dataInputStream.readInt());
//                ms.setFixDamR(dataInputStream.readInt());
//                ms.setCoolTime(dataInputStream.readInt());
//                ms.setInfo(dataInputStream.readUTF());
//                ms.setSpeak(dataInputStream.readUTF());
//                if (i < skillSize) {
//                    mob.addSkill(ms);
//                } else {
//                    mob.addAttack(ms);
//                }
//            }
//
//            Option pImmuneOpt = new Option();
//            pImmuneOpt.nOption = dataInputStream.readInt();
//            mts.addStatOptions(PImmune, pImmuneOpt);

            mob.setMoveAction((byte) 5); // normal monster?
            mob.setHp(fms.getMaxHP());
            mob.setMaxHp(fms.getMaxHP());
            mob.setMp(fms.getMaxMP());
            mob.setMaxMp(fms.getMaxMP());
            // TODO DROPS
//            mob.setDrops(DropData.getDropInfoByID(mob.getTemplateId()).stream().filter(dropInfo -> !dropInfo.getReactorDrop()).collect(Collectors.toSet()));
//
//            int mobLevel = mob.getForcedMobStat().getLevel();
//            int levelMultiplier = mobLevel * (mobLevel < 91 ? 1 : mobLevel < 150 ? 5 : mobLevel < 200 ? 9 : mobLevel < 235 ? 12 : 15);
//
//            mob.getDrops().add(new DropInfo(GameConstants.MAX_DROP_CHANCE,
//                    GameConstants.MIN_MONEY_MULT * levelMultiplier,
//                    GameConstants.MAX_MONEY_MULT * levelMultiplier
//            ));
//            for (DropInfo di : mob.getDrops()) {
//                di.generateNextDrop();
//            }
            addMob(mob);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mob;
    }


    public static void loadMobsFromWz() {
        String wzDir1 = ServerConstants.WZ_DIR + "/Mob.wz";
        File dir1 = new File(wzDir1);
        File[] files1 = dir1.listFiles();
        List<File> files = new ArrayList<>();
        files.addAll(Arrays.asList(files1));
        for (File file : files) {
            if (file.isDirectory()) {
                continue;
            }
            Document doc = XMLApi.getRoot(file);
            Node node = XMLApi.getAllChildren(doc).get(0);
            if (node == null) {
                continue;
            }
            int id = Integer.parseInt(XMLApi.getNamedAttribute(node, "name").replace(".img", ""));
            Node infoNode = XMLApi.getFirstChildByNameBF(node, "info");
            Mob mob = new Mob(id);
            // handle death delay calculation
            String[] deathNodes = { "die1", "die2", "dieF" }; // idk there may be more
            HashMap<DeathType, Long> reviveRespawnDelays = new HashMap<>();
            for (String s : deathNodes) {
                Node deathNode = XMLApi.getFirstChildByNameBF(node, s);
                if (deathNode == null) {
                    continue;
                }
                long delay = 0;
                for (Node n : XMLApi.getAllChildren(deathNode)) {
                    for (Node n2 : XMLApi.getAllChildren(n)) {
                        String name = XMLApi.getNamedAttribute(n2, "name");
                        if (!name.equalsIgnoreCase("delay")) {
                            continue;
                        }
                        String value = XMLApi.getNamedAttribute(n2, "value");
                        delay += Long.parseLong(value);
                    }
                }
                if (delay > 0) {
                    reviveRespawnDelays.put(DeathType.getByNodeName(s), delay);
                }
            }

            ForcedMobStat fms = mob.getForcedMobStat();
            MobTemporaryStat mts = mob.getTemporaryStat();
            for (Node n : XMLApi.getAllChildren(infoNode)) {
                String name = XMLApi.getNamedAttribute(n, "name");
                String value = XMLApi.getNamedAttribute(n, "value");
                switch (name) {
                    case "level":
                    case "Level":
                        fms.setLevel(Integer.parseInt(value));
                        break;
                    case "firstAttack":
                    case "firstattack":
                        mob.setFirstAttack((int) Double.parseDouble(value));
                        break;
                    case "bodyAttack":
                    case "bodyattack": // ...
//                        Option bodyOpt = new Option();
//                        bodyOpt.nOption = Integer.parseInt(value);
//                        mts.addStatOptions(MobStat.BodyAttack, bodyOpt); TODO?
                        break;
                    case "maxHP":
                    case "finalmaxHP":
                        if (Util.isNumber(value)) {
                            fms.setMaxHP(Long.parseLong(value));
                        } else {
                            fms.setMaxHP(1337);
                        }
                        break;
                    case "maxMP":
                        fms.setMaxMP(Integer.parseInt(value));
                        break;
                    case "PADamage":
                        fms.setPad(Integer.parseInt(value));
                        break;
                    case "PDDamage":
//                            mts.addStatOptions(PDR, "nPDR", Integer.parseInt(value));
                        break;
                    case "PDRate":
                        fms.setPdr(Integer.parseInt(value));
                        break;
                    case "MADamage":
                        fms.setMad(Integer.parseInt(value));
                        break;
                    case "MDDamage":
//                            mts.addStatOptions(PDR, "nMDR", Integer.parseInt(value));
                        break;
                    case "MDRate":
                        fms.setMdr(Integer.parseInt(value));
                        break;
                    case "acc":
                        fms.setAcc(Integer.parseInt(value));
                        break;
                    case "eva":
                        fms.setEva(Integer.parseInt(value));
                        break;
                    case "pushed":
                        fms.setPushed(Integer.parseInt(value));
                        break;
                    case "exp":
                        fms.setExp(Integer.parseInt(value));
                        break;
                    case "summonType":
                        mob.setSummonType(Integer.parseInt(value));
                        break;
                    case "mobType":
                        mob.setMobType(value);
                        break;
                    case "speed":
                    case "Speed":
                        fms.setSpeed(Integer.parseInt(value));
                        break;
                    case "fs":
                        mob.setFs(Double.parseDouble(value));
                        break;
                    case "elemAttr":
                        mob.setElemAttr(value);
                        break;
                    case "hpTagColor":
                        mob.setHpTagColor(Integer.parseInt(value));
                        break;
                    case "hpTagBgcolor":
                        mob.setHpTagBgcolor(Integer.parseInt(value));
                        break;
                    case "HPgaugeHide":
                        mob.setHPgaugeHide(Integer.parseInt(value) == 1);
                        break;
                    case "boss":
                        mob.setBoss(Integer.parseInt(value) == 1);
                        break;
                    case "undead":
                    case "Undead":
                        mob.setUndead(Integer.parseInt(value) == 1);
                        break;
                    case "noregen":
                        mob.setNoRegen(Integer.parseInt(value) == 1);
                        break;
                    case "invincible":
                    case "invincibe": // neckson pls
                        mob.setInvincible(Integer.parseInt(value) == 1);
                        break;
                    case "hideName":
                    case "hidename":
                        mob.setHideName(Integer.parseInt(value) == 1);
                        break;
                    case "hideHP":
                        mob.setHideHP(Integer.parseInt(value) == 1);
                        break;
                    case "noFlip":
                        mob.setNoFlip(Integer.parseInt(value) == 1);
                        break;
                    case "publicReward":
                        mob.setPublicReward(Integer.parseInt(value) == 1);
                        break;
                    case "ignoreFieldOut":
                        mob.setIgnoreFieldOut(Integer.parseInt(value) == 1);
                        break;
                    case "noDoom":
                        mob.setNoDoom(Integer.parseInt(value) == 1);
                        break;
                    case "knockback":
                        mob.setKnockback(Integer.parseInt(value) == 1);
                        break;
                    case "removeQuest":
                        mob.setRemoveQuest(Integer.parseInt(value) == 1);
                        break;
                    case "rareItemDropLevel":
                        mob.setRareItemDropLevel(Integer.parseInt(value));
                        break;
                    case "hpRecovery":
                        mob.setHpRecovery(Integer.parseInt(value));
                        break;
                    case "mpRecovery":
                        mob.setMpRecovery(Integer.parseInt(value));
                        break;
                    case "mbookID":
                        mob.setMbookID(Integer.parseInt(value));
                        break;
                    case "chaseSpeed":
                        mob.setChaseSpeed(Integer.parseInt(value));
                        break;
                    case "explosiveReward":
                        mob.setExplosiveReward(Integer.parseInt(value));
                        break;
                    case "flyspeed":
                    case "flySpeed":
                    case "FlySpeed":
                        mob.setFlySpeed(Integer.parseInt(value));
                        break;
                    case "summonEffect":
                        mob.setSummonEffect(Integer.parseInt(value));
                        break;
                    case "fixedDamage":
                        mob.setFixedDamage(Integer.parseInt(value));
                        break;
                    case "removeAfter":
                        mob.setRemoveAfter(Integer.parseInt(value));
                        break;
                    case "coolDamage":
                        mob.setCoolDamage(Integer.parseInt(value));
                        break;
                    case "coolDamageProb":
                        mob.setCoolDamageProb(Integer.parseInt(value));
                        break;
                    case "PImmune":
                        Option immOpt = new Option();
                        immOpt.nOption = Integer.parseInt(value);
                        mts.addStatOptions(MobStat.PImmune, immOpt);
                        break;
                    case "revive":
                        mob.setReviveRespawnDelays(reviveRespawnDelays);
                        for (Node reviveNode : XMLApi.getAllChildren(n)) {
                            String reviveNodeName = XMLApi.getNamedAttribute(reviveNode, "name");
                            int reviveNodeVal = Integer.parseInt((XMLApi.getNamedAttribute(reviveNode, "value")));
                            if (Util.isNumber(reviveNodeName)) {
                                mob.addRevive(reviveNodeVal);
                            } else {
                                switch(reviveNodeName) {
                                    // TODO: look into this, guessing it means whether the revives need to inherit the original 'changaeble' value of the main mob or not
                                    case "inheritChangaeble":
                                        // probably need a new member with method like:
                                        //      setInheritChangaeble(bool)
                                        // usage:
                                        //      setInheritChangaeble(reviveNodeVal == 1)
                                        break;
                                }
                            }
                        }
                        break;
                    case "skill":
                    case "attack":
                        boolean attack = "attack".equalsIgnoreCase(name);
                        for (Node skillIDNode : XMLApi.getAllChildren(n)) {
                            if(!Util.isNumber(XMLApi.getNamedAttribute(skillIDNode, "name"))) {
                                continue;
                            }
                            MobSkill mobSkill = new MobSkill();
                            mobSkill.setSkillSN(Integer.parseInt(XMLApi.getNamedAttribute(skillIDNode, "name")));
                            for (Node skillInfoNode : XMLApi.getAllChildren(skillIDNode)) {
                                String skillNodeName = XMLApi.getNamedAttribute(skillInfoNode, "name");
                                String skillNodeValue = XMLApi.getNamedAttribute(skillInfoNode, "value");
                                switch (skillNodeName) {
                                    case "skill":
                                        mobSkill.setSkillID(Integer.parseInt(skillNodeValue));
                                        break;
                                    case "action":
                                        mobSkill.setAction(Byte.parseByte(skillNodeValue));
                                        break;
                                    case "level":
                                        mobSkill.setLevel(Integer.parseInt(skillNodeValue));
                                        break;
                                    case "effectAfter":
                                        if (!skillNodeValue.isEmpty()) {
                                            mobSkill.setEffectAfter(Integer.parseInt(skillNodeValue));
                                        }
                                        break;
                                    case "skillAfter":
                                        mobSkill.setSkillAfter(Integer.parseInt(skillNodeValue));
                                        break;
                                    case "onlyFsm":
                                        mobSkill.setOnlyFsm(Integer.parseInt(skillNodeValue) != 0);
                                        break;
                                    case "onlyOtherSkill":
                                        mobSkill.setOnlyOtherSkill(Integer.parseInt(skillNodeValue) != 0);
                                        break;
                                    case "doFirst":
                                        mobSkill.setDoFirst(Integer.parseInt(skillNodeValue) != 0);
                                        break;
                                    case "afterDead":
                                        mobSkill.setAfterDead(Integer.parseInt(skillNodeValue) != 0);
                                        break;
                                    case "afterAttack":
                                        mobSkill.setAfterAttack(Integer.parseInt(skillNodeValue));
                                        break;
                                    case "afterAttackCount":
                                        mobSkill.setAfterAttackCount(Integer.parseInt(skillNodeValue));
                                        break;
                                    case "afterDelay":
                                        mobSkill.setAfterDelay(Integer.parseInt(skillNodeValue));
                                        break;
                                    case "fixDamR":
                                        mobSkill.setFixDamR(Integer.parseInt(skillNodeValue));
                                        break;
                                    case "cooltime":
                                        mobSkill.setCoolTime(Integer.parseInt(skillNodeValue));
                                        break;
                                    case "info":
                                        mobSkill.setInfo(skillNodeValue);
                                        break;
                                    case "speak":
                                        mobSkill.setSpeak(skillNodeValue);
                                        break;
                                    default:
                                        if (LOG_UNKS) {
                                            log.warn("Unknown skill node {} with value {}", skillNodeName, skillNodeValue);
                                        }
                                }
                            }
                            if (attack) {
                                mob.addAttack(mobSkill);
                            } else {
                                mob.addSkill(mobSkill);
                            }
                        }
                        break;
                    case "selfDestruction":
                        // TODO Maybe more info?
                        mob.setSelfDestruction(true);
                        break;
                    case "escort":
                        mob.setEscortMob(Integer.parseInt(value) != 0);
                        break;
                    case "speak":
                    case "thumbnail":
                    case "default":
                    case "defaultHP":
                    case "defaultMP":
                    case "passive":
                    case "firstAttackRange":
                    case "nonLevelCheckEVA":
                    case "nonLevelCheckACC":
                    case "changeImg":
                    case "showNotRemoteDam":
                    case "damagedBySelectedMob":
                    case "damagedByMob":
                    case "getCP":
                    case "loseItem":
                    case "0":
                    case "onlyNormalAttack":
                    case "notConsiderFieldSet":
                    case "overSpeed":
                    case "ignoreMovable":
                    case "jsonLoad":
                    case "fixedBodyAttackDamageR":
                    case "adjustLayerZ":
                    case "damagedBySelectedSkill":
                    case "option_damagedByMob":
                    case "bodyKnockBack":
                    case "straightMoveDir":
                    case "onlyHittedByCommonAttack":
                    case "invincibleAttack":
                    case "noChase":
                    case "notAttack":
                    case "alwaysInAffectedRect":
                    case "firstShowHPTag":
                    case "pointFPSMode":
                    case "11":
                    case "prevLinkMob":
                    case "option_skeleton":
                    case "lifePoint":
                    case "defenseMob":
                    case "forceChaseEscort":
                    case "damageModification":
                    case "randomFlyingBoss":
                    case "randomFlyingMob":
                    case "stalking":
                    case "minimap":
                    case "removeOnMiss":
                    case "fieldEffect":
                    case "onceReward":
                    case "onMobDeadQR":
                    case "peddlerMob":
                    case "peddlerDamR":
                    case "rewardSprinkle":
                    case "rewardSprinkleCount":
                    case "rewardSprinkleSpeed":
                    case "ignorMoveImpact":
                    case "dropItemPeriod":
                    case "hideMove":
                    case "atom":
                    case "smartPhase":
                    case "trans":
                    case "chaseEffect":
                    case "dualGauge":
                    case "noReturnByDead":
                    case "AngerGauge":
                    case "ChargeCount":
                    case "upperMostLayer":
                    case "cannotEvade":
                    case "phase":
                    case "doNotRemove":
                    case "healOnDestroy":
                    case "debuffobj":
                    case "obtacle":
                    case "mobZone":
                    case "weapon":
                    case "forcedFlip":
                    case "001":
                    case "002":
                    case "003":
                    case "004":
                    case "005":
                    case "006":
                    case "007":
                    case "008":
                    case "009":
                    case "010":
                    case "011":
                    case "onlySelectedSkill":
                    case "finalAdjustedDamageRate":
                    case "battlePvP":
                    case "mobJobCategory":
                    case "considerUserCount":
                    case "randomMob":
                    case "dieHeight":
                    case "dieHeightTime":
                    case "notChase":
                    case "fixedStat":
                    case "allyMob":
                    case "linkMob":
                    case "skelAniMixRate":
                    case "mobZoneObjID":
                    case "mobZoneObjType":
                    case "holdRange":
                    case "targetChaseTime":
                    case "copyCharacter":
                    case "disable":
                    case "underObject":
                    case "1582":
                    case "peddlerCount":
                    case "bodyAttackInfo":
                    case "mobZoneType":
                    case "largeDamageRecord":
                    case "considerUserCounter":
                    case "damageByObtacleAtom":
                    case "info":
                    case "cantPassByTeleport":
                    case "250000":
                    case "forward_direction":
                        break;
                    default:
                        if (LOG_UNKS) {
                            log.warn("Unknown property {} with value {}.", name, value);
                        }
                }
                getMobs().put(mob.getTemplateId(), mob);
            }
        }
    }

    public static void main(String[] args) {
        generateDatFiles();
    }

    public static void clear() {
        getMobs().clear();
    }
}
