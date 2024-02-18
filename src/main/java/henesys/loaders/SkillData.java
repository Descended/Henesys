package henesys.loaders;

import henesys.ServerConstants;
import henesys.client.character.skills.info.SkillInfo;
import henesys.enums.SkillStat;
import henesys.enums.WeaponType;
import henesys.life.mob.skill.MobSkillStat;
import henesys.loaders.containers.MobSkillInfo;
import henesys.skills.Skill;
import henesys.util.Position;
import henesys.util.Rect;
import henesys.util.Util;
import henesys.util.XMLApi;
import org.apache.logging.log4j.LogManager;
import org.w3c.dom.Node;

import java.io.*;
import java.util.*;

/**
 * Created on 12/20/2017.
 */
public class SkillData {

    private static Map<Integer, SkillInfo> skills = new HashMap<>();
    private static Map<Short, Map<Short, MobSkillInfo>> mobSkillInfos = new HashMap<>();
    private static final org.apache.logging.log4j.Logger log = LogManager.getRootLogger();
    private static final boolean LOG_UNKNOWN_STATS = false;

    public static void saveSkills(String dir) {
        Util.makeDirIfAbsent(dir);
        for (Map.Entry<Integer, SkillInfo> entry : skills.entrySet()) {
            SkillInfo si = entry.getValue();
            File file = new File(String.format("%s/%d.dat", dir, si.getSkillId()));
            try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(file))) {
                dataOutputStream.writeInt(si.getSkillId());
                dataOutputStream.writeInt(si.getRootId());
                dataOutputStream.writeInt(si.getMaxLevel());
                dataOutputStream.writeInt(si.getMasterLevel());
                dataOutputStream.writeInt(si.getFixLevel());
                dataOutputStream.writeBoolean(si.isInvisible());
                dataOutputStream.writeBoolean(si.isMassSpell());
                dataOutputStream.writeInt(si.getType());
                dataOutputStream.writeUTF(si.getElemAttr());
                dataOutputStream.writeInt(si.getHyper());
                dataOutputStream.writeInt(si.getHyperStat());
                dataOutputStream.writeInt(si.getVehicleId());
                dataOutputStream.writeInt(si.getReqTierPoint());
                dataOutputStream.writeBoolean(si.isNotCooltimeReset());
                dataOutputStream.writeBoolean(si.isNotIncBuffDuration());
                dataOutputStream.writeBoolean(si.isPsd());
                dataOutputStream.writeShort(si.getSkillStatInfo().size());
                for (Map.Entry<SkillStat, String> ssEntry : si.getSkillStatInfo().entrySet()) {
                    dataOutputStream.writeUTF(ssEntry.getKey().toString());
                    if (ssEntry.getValue() == null) {
                        dataOutputStream.writeUTF("");
                    } else {
                        dataOutputStream.writeUTF(ssEntry.getValue());
                    }
                }
                dataOutputStream.writeShort(si.getRects().size());
                for (Rect r : si.getRects()) {
                    dataOutputStream.writeInt(r.getLeft());
                    dataOutputStream.writeInt(r.getTop());
                    dataOutputStream.writeInt(r.getRight());
                    dataOutputStream.writeInt(r.getBottom());
                }
                dataOutputStream.writeShort(si.getPsdSkills().size());
                for (int i : si.getPsdSkills()) {
                    dataOutputStream.writeInt(i);
                }
                dataOutputStream.writeShort(si.getReqSkills().size());
                for (Map.Entry<Integer, Integer> reqSkill : si.getReqSkills().entrySet()) {
                    dataOutputStream.writeInt(reqSkill.getKey());
                    dataOutputStream.writeInt(reqSkill.getValue());
                }
                dataOutputStream.writeShort(si.getAddAttackSkills().size());
                for (int i : si.getAddAttackSkills()) {
                    dataOutputStream.writeInt(i);
                }
                dataOutputStream.writeShort(si.getExtraSkillInfo().size());
                for (Map.Entry<Integer, Integer> extraSkillInfo : si.getExtraSkillInfo().entrySet()) {
                    dataOutputStream.writeInt(extraSkillInfo.getKey());
                    dataOutputStream.writeInt(extraSkillInfo.getValue());
                }
                dataOutputStream.writeShort(si.getPsdWT().size());
                for (Map.Entry<WeaponType, Map<SkillStat, Integer>> psdWTEntry : si.getPsdWT().entrySet()) {
                    dataOutputStream.writeByte(psdWTEntry.getKey().getVal());

                    dataOutputStream.writeShort(psdWTEntry.getValue().size());
                    for (Map.Entry<SkillStat, Integer> ssEntry : psdWTEntry.getValue().entrySet()) {
                        dataOutputStream.writeUTF(ssEntry.getKey().toString());
                        dataOutputStream.writeInt(ssEntry.getValue());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadSkills() {
        log.info("Loading all player skills");
        File dir = new File(ServerConstants.DAT_DIR + "/skills/");
        if (dir.listFiles() == null) {
            log.error("Can't load skills folder!");
            return;
        } else {
            for (File file : dir.listFiles()) {
                loadSkill(file);
            }
        }
        log.info("Done loading all player skills!");
    }

    public static void loadSkill(File file) {
        try (DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file))) {
            SkillInfo skillInfo = new SkillInfo();
            skillInfo.setSkillId(dataInputStream.readInt());
            skillInfo.setRootId(dataInputStream.readInt());
            skillInfo.setMaxLevel(dataInputStream.readInt());
            skillInfo.setMasterLevel(dataInputStream.readInt());
            skillInfo.setFixLevel(dataInputStream.readInt());
            skillInfo.setInvisible(dataInputStream.readBoolean());
            skillInfo.setMassSpell(dataInputStream.readBoolean());
            skillInfo.setType(dataInputStream.readInt());
            skillInfo.setElemAttr(dataInputStream.readUTF());
            skillInfo.setHyper(dataInputStream.readInt());
            skillInfo.setHyperStat(dataInputStream.readInt());
            skillInfo.setVehicleId(dataInputStream.readInt());
            skillInfo.setReqTierPoint(dataInputStream.readInt());
            skillInfo.setNotCooltimeReset(dataInputStream.readBoolean());
            skillInfo.setNotIncBuffDuration(dataInputStream.readBoolean());
            skillInfo.setPsd(dataInputStream.readBoolean());

            short ssSize = dataInputStream.readShort();
            for (int j = 0; j < ssSize; j++) {
                skillInfo.addSkillStatInfo(SkillStat.getSkillStatByString(
                        dataInputStream.readUTF()), dataInputStream.readUTF());
            }
            short rectSize = dataInputStream.readShort();
            for (int j = 0; j < rectSize; j++) {
                int left = dataInputStream.readInt();
                int top = dataInputStream.readInt();
                int right = dataInputStream.readInt();
                int bottom = dataInputStream.readInt();
                skillInfo.addRect(new Rect(left, top, right, bottom));
            }
            short psdSize = dataInputStream.readShort();
            for (int j = 0; j < psdSize; j++) {
                skillInfo.addPsdSkill(dataInputStream.readInt());
            }
            short reqSkillSize = dataInputStream.readShort();
            for (int j = 0; j < reqSkillSize; j++) {
                skillInfo.addReqSkill(dataInputStream.readInt(), dataInputStream.readInt());
            }
            short addAttackSize = dataInputStream.readShort();
            for (int j = 0; j < addAttackSize; j++) {
                skillInfo.addAddAttackSkills(dataInputStream.readInt());
            }
            short extraSkillInfoSize = dataInputStream.readShort();
            for (int j = 0; j < extraSkillInfoSize; j++) {
                skillInfo.addExtraSkillInfo(dataInputStream.readInt(), dataInputStream.readInt());
            }
            short psdWTMapSize = dataInputStream.readShort();
            for (int i = 0; i < psdWTMapSize; i++) {
                WeaponType wt = WeaponType.getByVal(dataInputStream.readByte());
                short skillStatMapSize = dataInputStream.readShort();
                Map<SkillStat, Integer> skillStatMap = new HashMap<>();
                for (int j = 0; j < skillStatMapSize; j++) {
                    SkillStat ss = SkillStat.getSkillStatByString(dataInputStream.readUTF());
                    int value = dataInputStream.readInt();
                    skillStatMap.put(ss, value);
                }
                skillInfo.addPsdWT(wt, skillStatMap);
            }
            getSkillInfos().put(skillInfo.getSkillId(), skillInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadSkillsFromWz() {
        String wzDir = ServerConstants.WZ_DIR + "/Skill.wz";
        File dir = new File(wzDir);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.getName().contains("Dragon")) {
                continue;
            }
            Node node = XMLApi.getRoot(file);
            if (node == null) {
                continue;
            }
            List<Node> nodes = XMLApi.getAllChildren(node);
            for (Node mainNode : nodes) {
                Map<String, String> attributes = XMLApi.getAttributes(mainNode);
                String rootIdStr = attributes.get("name").replace(".img", "");
                int rootId;
                if (Util.isNumber(rootIdStr)) {
                    rootId = Integer.parseInt(rootIdStr);
                } else {
                    continue;
                }
                Set<String> unkVals = new HashSet<>();
                Node skillChild = XMLApi.getFirstChildByNameBF(mainNode, "skill");
                for (Node skillNode : XMLApi.getAllChildren(skillChild)) {
                    Map<String, String> skillAttributes = XMLApi.getAttributes(skillNode);
                    String skillIdStr = skillAttributes.get("name").replace(".img", "");
                    int skillId;
                    if (Util.isNumber(skillIdStr)) {
                        SkillInfo skill = new SkillInfo();
                        skill.setRootId(rootId);
                        if (Util.isNumber(skillIdStr)) {
                            skillId = Integer.parseInt(skillIdStr);
                            skill.setSkillId(skillId);
                        } else {
                            if (LOG_UNKNOWN_STATS) {
                                log.warn("{} is not a number.", skillIdStr);
                            }
                            continue;
                        }
                        for (Node mainLevelNode : XMLApi.getAllChildren(skillNode)) {
                            String mainName = XMLApi.getNamedAttribute(mainLevelNode, "name");
                            String mainValue = XMLApi.getNamedAttribute(mainLevelNode, "value");
                            int intVal = -1337;
                            if (Util.isNumber(mainValue)) {
                                intVal = Integer.parseInt(mainValue);
                            }
                            switch (mainName) {
                                case "masterLevel":
                                    skill.setMasterLevel(intVal);
                                    break;
                                case "fixLevel":
                                    skill.setFixLevel(intVal);
                                    break;
                                case "invisible":
                                    skill.setInvisible(intVal != 0);
                                    break;
                                case "affected":
                                    skill.setMassSpell(true);
                                    break;
                                case "massSpell":
                                    skill.setMassSpell(intVal != 0);
                                    break;
                                case "type":
                                    skill.setType(intVal);
                                    break;
                                case "psd":
                                    skill.setPsd(intVal != 0);
                                    break;
                                case "psdSkill":
                                    for (Node psdSkillNode : XMLApi.getAllChildren(mainLevelNode)) {
                                        skill.addPsdSkill(Integer.parseInt(XMLApi.getAttributes(psdSkillNode).get("name")));
                                    }
                                    break;
                                case "elemAttr":
                                    skill.setElemAttr(mainValue);
                                    break;
                                case "hyper":
                                    skill.setHyper(intVal);
                                    break;
                                case "hyperStat":
                                    skill.setHyperStat(intVal);
                                    break;
                                case "vehicleID":
                                    skill.setVehicleId(intVal);
                                    break;
                                case "notCooltimeReset":
                                    skill.setNotCooltimeReset(intVal != 0);
                                    break;
                                case "notIncBuffDuration":
                                    skill.setNotIncBuffDuration(intVal != 0);
                                    break;
                                case "req":
                                    for (Node reqChild : XMLApi.getAllChildren(mainLevelNode)) {
                                        String childName = XMLApi.getNamedAttribute(reqChild, "name");
                                        String childValue = XMLApi.getNamedAttribute(reqChild, "value");
                                        if ("reqTierPoint".equalsIgnoreCase(childName)) {
                                            skill.setReqTierPoint(Integer.parseInt(childValue));
                                        } else if (Util.isNumber(childName)) {
                                            skill.addReqSkill(Integer.parseInt(childName), Integer.parseInt(childValue));
                                        }
                                    }
                                    break;
                                case "common":
                                case "info":
                                case "info2":
                                    for (Node commonNode : XMLApi.getAllChildren(mainLevelNode)) {
                                        Map<String, String> commonAttr = XMLApi.getAttributes(commonNode);
                                        String nodeName = commonAttr.get("name");
                                        if (nodeName.equals("maxLevel")) {
                                            skill.setMaxLevel(Integer.parseInt(XMLApi.getNamedAttribute(commonNode, "value")));
                                        } else if (nodeName.contains("lt") && nodeName.length() <= 3) {
                                            Node rbNode = XMLApi.getFirstChildByNameBF(mainLevelNode, nodeName.replace("lt", "rb"));
                                            int left = Integer.parseInt(XMLApi.getNamedAttribute(commonNode, "x"));
                                            int top = Integer.parseInt(XMLApi.getNamedAttribute(commonNode, "y"));
                                            int right = Integer.parseInt(XMLApi.getNamedAttribute(rbNode, "x"));
                                            int bottom = Integer.parseInt(XMLApi.getNamedAttribute(rbNode, "y"));
                                            skill.addRect(new Rect(left, top, right, bottom));
                                        } else {
                                            SkillStat skillStat = SkillStat.getSkillStatByString(nodeName);
                                            if (skillStat != null) {
                                                skill.addSkillStatInfo(skillStat, commonAttr.get("value"));
                                            } else if (!unkVals.contains(nodeName)) {
                                                if (LOG_UNKNOWN_STATS) {
                                                    log.warn("[SkillData] Unknown SkillStat {}", nodeName);
                                                }
                                                skill.addUnknownSkillStat(nodeName);
                                                unkVals.add(nodeName);
                                            }
                                        }
                                    }
                                    break;
                                case "addAttack":
                                    for (Node addAttackNode : XMLApi.getAllChildren(mainLevelNode)) {
                                        Map<String, String> addAttackAttr = XMLApi.getAttributes(addAttackNode);
                                        String nodeName = addAttackAttr.get("name");
                                        String nodeValue = addAttackAttr.get("value");
                                        switch (nodeName) {
                                            case "skillPlus":
                                                for (Node skillPlusNode : XMLApi.getAllChildren(addAttackNode)) {
                                                    Map<String, String> skillPlusAttr = XMLApi.getAttributes(skillPlusNode);
                                                    String skillPlusNodeName = skillPlusAttr.get("name");
                                                    String skillPlusNodeValue = skillPlusAttr.get("value");
                                                    skill.addAddAttackSkills(Integer.parseInt(skillPlusNodeValue));
                                                }
                                                break;
                                        }
                                    }
                                    break;
                                case "extraSkillInfo":
                                    for (Node extraSkillInfoNode : XMLApi.getAllChildren(mainLevelNode)) {
                                        Map<String, String> extraSkillInfoAttr = XMLApi.getAttributes(extraSkillInfoNode);
                                        int extraSkillDelay = 0;
                                        int extraSkillId = -1;
                                        for (Node extraSkillInfoIndividual : XMLApi.getAllChildren(extraSkillInfoNode)) {
                                            Map<String, String> extraSkillAttr = XMLApi.getAttributes(extraSkillInfoIndividual);
                                            String extraSkillName = extraSkillAttr.get("name");
                                            String extraSkillValue = extraSkillAttr.get("value");
                                            switch (extraSkillName) {
                                                case "delay":
                                                    extraSkillDelay = Integer.parseInt(extraSkillValue);
                                                    break;
                                                case "skill":
                                                    extraSkillId = Integer.parseInt(extraSkillValue);
                                                    break;
                                                default:
                                                    if (LOG_UNKNOWN_STATS) {
                                                        log.warn("Unknown Extra Skill Info Name: {}", extraSkillName);
                                                    }
                                                    break;
                                            }
                                        }
                                        if (extraSkillId > 0) {
                                            skill.addExtraSkillInfo(extraSkillId, extraSkillDelay);
                                        }
                                    }
                                    break;
                                case "psdWT":
                                    for (Node psdWTNode : XMLApi.getAllChildren(mainLevelNode)) {
                                        Map<String, String> psdWTAttr = XMLApi.getAttributes(psdWTNode);
                                        String weaponType = psdWTAttr.get("name");
                                        WeaponType wt = WeaponType.getByVal(Integer.parseInt(weaponType));
                                        if (wt == null) {
                                            log.error(String.format("Unknown WeaponType %s, in SkillID: %d", weaponType, skillId));
                                            continue;
                                        }
                                        Map<SkillStat, Integer> skillStatMap = new HashMap<>();
                                        for (Node skillStatNode : XMLApi.getAllChildren(psdWTNode)) {
                                            Map<String, String> skillStatAttr = XMLApi.getAttributes(skillStatNode);
                                            String skillStatStr = skillStatAttr.get("name");
                                            String skillStatValue = skillStatAttr.get("value");
                                            SkillStat skillStat = SkillStat.getSkillStatByString(skillStatStr);
                                            if (skillStat == null) {
                                                log.error(String.format("Unknown psdWT SkillStat: %s, in SkillID: %d", skillStatStr, skillId));
                                                continue;
                                            }
                                            if (!Util.isNumber(skillStatValue)) {
                                                log.error(String.format("Unknown psdWT SkillStat Value: %s, in SkillID: %d", skillStatStr, skillId));
                                                continue;
                                            }
                                            skillStatMap.put(skillStat, Integer.parseInt(skillStatValue));
                                        }
                                        skill.addPsdWT(wt, skillStatMap);
                                    }
                                    break;
                            }
                        }
                        skills.put(skillId, skill);
                    }
                }
            }
        }
    }

    public static Map<Integer, SkillInfo> getSkillInfos() {
        return skills;
    }

    public static SkillInfo getSkillInfoById(int skillId) {
        if (!getSkillInfos().containsKey(skillId)) {
            File file = new File(String.format("%s/skills/%d.dat", ServerConstants.DAT_DIR, skillId));
            if (file.exists()) {
                loadSkill(file);
            }
        }
        return getSkillInfos().get(skillId);

    }

    public static List<Skill> getSkillsByJob(short jobId) {
        List<Skill> res = new ArrayList<>();
        getSkillInfos().forEach((key, si) -> {
            if (si.getRootId() == jobId && !si.isInvisible()) {
                res.add(getSkillDeepCopyById(key));
            }
        });
        return res;
    }

    public static Skill getSkillDeepCopyById(int skillId) {
        SkillInfo si = getSkillInfoById(skillId);
        if (si == null) {
            return null;
        }
        Skill skill = new Skill();
        skill.setSkillId(si.getSkillId());
        skill.setRootId(si.getRootId());
        skill.setMasterLevel(si.getMaxLevel());
//        skill.setMasterLevel(si.getMasterLevel()); // for now, Maybe always?
        skill.setMaxLevel(si.getMaxLevel());
        if (si.getMasterLevel() <= 0) {
            skill.setMasterLevel(si.getMaxLevel());
        }
        if (si.getFixLevel() > 0) {
            skill.setCurrentLevel(si.getFixLevel());
        } else {
            skill.setCurrentLevel(0);
        }
        return skill;
    }

    public static Map<Short, Map<Short, MobSkillInfo>> getMobSkillInfos() {
        return mobSkillInfos;
    }

    public static void addMobSkillInfo(MobSkillInfo msi) {
        getMobSkillInfos().computeIfAbsent(msi.getId(), k -> new HashMap<>());
        Map<Short, MobSkillInfo> msiLevelMap = getMobSkillInfos().get(msi.getId());
        msiLevelMap.put(msi.getLevel(), msi);
        getMobSkillInfos().put(msi.getId(), msiLevelMap);
    }
    public static void loadMobSkillsFromWz() {
        String wzDir = ServerConstants.WZ_DIR + "/Skill.wz/MobSkill.img.xml";
        File file = new File(wzDir);
        Node root = XMLApi.getRoot(file);
        Node mainNode = XMLApi.getAllChildren(root).getFirst();
        List<Node> nodes = XMLApi.getAllChildren(mainNode);
        Set<String> unks = new HashSet<>();
        for (Node node : nodes) {
            short skillID = Short.parseShort(XMLApi.getNamedAttribute(node, "name"));
            for (Node levelNode : XMLApi.getAllChildren(XMLApi.getFirstChildByNameBF(node, "level"))) {
                short level = Short.parseShort(XMLApi.getNamedAttribute(levelNode, "name"));
                MobSkillInfo msi = new MobSkillInfo();
                msi.setId(skillID);
                msi.setLevel(level);
                for (Node skillStatNode : XMLApi.getAllChildren(levelNode)) {
                    String name = XMLApi.getNamedAttribute(skillStatNode, "name");
                    String value = XMLApi.getNamedAttribute(skillStatNode, "value");
                    String x = XMLApi.getNamedAttribute(skillStatNode, "x");
                    String y = XMLApi.getNamedAttribute(skillStatNode, "y");
                    if (Util.isNumber(name)) {
                        msi.addIntToList(Integer.parseInt(value));
                        continue;
                    }
                    switch (name) {
                        case "x":
                            msi.putMobSkillStat(MobSkillStat.x, value);
                            break;
                        case "mpCon":
                            msi.putMobSkillStat(MobSkillStat.mpCon, value);
                            break;
                        case "interval":
                        case "inteval":
                            msi.putMobSkillStat(MobSkillStat.interval, value);
                            break;
                        case "hp":
                        case "HP":
                            msi.putMobSkillStat(MobSkillStat.hp, value);
                            break;
                        case "info":
                            msi.putMobSkillStat(MobSkillStat.info, value);
                            break;
                        case "y":
                            msi.putMobSkillStat(MobSkillStat.y, value);
                            break;
                        case "lt":
                            msi.setLt(new Position(Integer.parseInt(x), Integer.parseInt(y)));
                            break;
                        case "rb":
                            msi.setRb(new Position(Integer.parseInt(x), Integer.parseInt(y)));
                            break;
                        case "lt2":
                            msi.setLt2(new Position(Integer.parseInt(x), Integer.parseInt(y)));
                            break;
                        case "rb2":
                            msi.setRb2(new Position(Integer.parseInt(x), Integer.parseInt(y)));
                            break;
                        case "lt3":
                            msi.setLt3(new Position(Integer.parseInt(x), Integer.parseInt(y)));
                            break;
                        case "rb3":
                            msi.setRb3(new Position(Integer.parseInt(x), Integer.parseInt(y)));
                            break;
                        case "limit":
                            msi.putMobSkillStat(MobSkillStat.limit, value);
                            break;
                        case "broadCastScreenMsg":
                            msi.putMobSkillStat(MobSkillStat.broadCastScreenMsg, value);
                            break;
                        case "w":
                            msi.putMobSkillStat(MobSkillStat.w, value);
                            break;
                        case "z":
                            msi.putMobSkillStat(MobSkillStat.z, value);
                            break;
                        case "parsing":
                            msi.putMobSkillStat(MobSkillStat.parsing, value);
                            break;
                        case "prop":
                            msi.putMobSkillStat(MobSkillStat.prop, value);
                            break;
                        case "ignoreResist":
                            msi.putMobSkillStat(MobSkillStat.ignoreResist, value);
                            break;
                        case "count":
                            msi.putMobSkillStat(MobSkillStat.count, value);
                            break;
                        case "time":
                            msi.putMobSkillStat(MobSkillStat.time, value);
                            break;
                        case "targetAggro":
                            msi.putMobSkillStat(MobSkillStat.targetAggro, value);
                            break;
                        case "fieldScript":
                            msi.putMobSkillStat(MobSkillStat.fieldScript, value);
                            break;
                        case "elemAttr":
                            msi.putMobSkillStat(MobSkillStat.elemAttr, value);
                            break;
                        case "delay":
                            msi.putMobSkillStat(MobSkillStat.delay, value);
                            break;
                        case "rank":
                            msi.putMobSkillStat(MobSkillStat.rank, value);
                            break;
                        case "HPDeltaR":
                            msi.putMobSkillStat(MobSkillStat.HPDeltaR, value);
                            break;
                        case "summonEffect":
                            msi.putMobSkillStat(MobSkillStat.summonEffect, value);
                            break;
                        case "y2":
                            msi.putMobSkillStat(MobSkillStat.y2, value);
                            break;
                        case "q":
                            msi.putMobSkillStat(MobSkillStat.q, value);
                            break;
                        case "q2":
                            msi.putMobSkillStat(MobSkillStat.q2, value);
                            break;
                        case "s2":
                            msi.putMobSkillStat(MobSkillStat.s2, value);
                            break;
                        case "u":
                            msi.putMobSkillStat(MobSkillStat.u, value);
                            break;
                        case "u2":
                            msi.putMobSkillStat(MobSkillStat.u2, value);
                            break;
                        case "v":
                            msi.putMobSkillStat(MobSkillStat.v, value);
                            break;
                        case "z2":
                            msi.putMobSkillStat(MobSkillStat.z2, value);
                            break;
                        case "w2":
                            msi.putMobSkillStat(MobSkillStat.w2, value);
                            break;
                        case "skillAfter":
                            msi.putMobSkillStat(MobSkillStat.skillAfter, value);
                            break;
                        case "x2":
                            msi.putMobSkillStat(MobSkillStat.x2, value);
                            break;
                        case "script":
                            msi.putMobSkillStat(MobSkillStat.script, value);
                            break;
                        case "attackSuccessProp":
                            msi.putMobSkillStat(MobSkillStat.attackSuccessProp, value);
                            break;
                        case "bossHeal":
                            msi.putMobSkillStat(MobSkillStat.bossHeal, value);
                            break;
                        case "face":
                            msi.putMobSkillStat(MobSkillStat.face, value);
                            break;
                        case "callSkill":
                            msi.putMobSkillStat(MobSkillStat.callSkill, value);
                            break;
                        case "level":
                            msi.putMobSkillStat(MobSkillStat.level, value);
                            break;
                        case "linkHP":
                            msi.putMobSkillStat(MobSkillStat.linkHP, value);
                            break;
                        case "timeLimitedExchange":
                            msi.putMobSkillStat(MobSkillStat.timeLimitedExchange, value);
                            break;
                        case "summonDir":
                            msi.putMobSkillStat(MobSkillStat.summonDir, value);
                            break;
                        case "summonTerm":
                            msi.putMobSkillStat(MobSkillStat.summonTerm, value);
                            break;
                        case "castingTime":
                            msi.putMobSkillStat(MobSkillStat.castingTime, value);
                            break;
                        case "subTime":
                            msi.putMobSkillStat(MobSkillStat.subTime, value);
                            break;
                        case "reduceCasting":
                            msi.putMobSkillStat(MobSkillStat.reduceCasting, value);
                            break;
                        case "additionalTime":
                            msi.putMobSkillStat(MobSkillStat.additionalTime, value);
                            break;
                        case "force":
                            msi.putMobSkillStat(MobSkillStat.force, value);
                            break;
                        case "targetType":
                            msi.putMobSkillStat(MobSkillStat.targetType, value);
                            break;
                        case "forcex":
                            msi.putMobSkillStat(MobSkillStat.forcex, value);
                            break;
                        case "sideAttack":
                            msi.putMobSkillStat(MobSkillStat.sideAttack, value);
                            break;
                        case "afterEffect":
                        case "rangeGap":
                            msi.putMobSkillStat(MobSkillStat.rangeGap, value);
                            break;
                        case "noGravity":
                            msi.putMobSkillStat(MobSkillStat.noGravity, value);
                            break;
                        case "notDestroyByCollide":
                            msi.putMobSkillStat(MobSkillStat.notDestroyByCollide, value);
                            break;
//                        case "fixedPos": {
//                            List<Position> fixedSummonPos = new ArrayList<>();
//                            for (Node fixedPosNode : XMLApi.getAllChildren(skillStatNode)) {
//                                if (Util.isNumber(XMLApi.getNamedAttribute(fixedPosNode, "name"))) {
//                                    fixedSummonPos.add(new Position(
//                                            Integer.parseInt(XMLApi.getNamedAttribute(fixedPosNode, "x")),
//                                            Integer.parseInt(XMLApi.getNamedAttribute(fixedPosNode, "y"))));
//                                }
//                            }
//                            msi.setFixedSummonPos(fixedSummonPos);
//                            msi.putMobSkillStat(MobSkillStat.fixedPos, String.valueOf(fixedSummonPos.size()));
//                            break;
//                        }
//                        case "fixedDir": {
//                            List<Byte> fixedSummonDir = new ArrayList<>();
//                            for (Node fixedDirNode : XMLApi.getAllChildren(skillStatNode)) {
//                                if (Util.isNumber(XMLApi.getNamedAttribute(fixedDirNode, "name"))) {
//                                    fixedSummonDir.add(Byte.parseByte(XMLApi.getNamedAttribute(fixedDirNode, "value")));
//                                }
//                            }
//                            msi.setFixedSummonDir(fixedSummonDir);
//                            msi.putMobSkillStat(MobSkillStat.fixedDir, String.valueOf(fixedSummonDir.size()));
//                            break;
//                        }
                        case "effect":
                        case "mob":
                        case "mob0":
                        case "hit":
                        case "affected":
                        case "affectedOtherSkill":
                        case "crash":
                        case "effectToUser":
                        case "affected_after":
                        case "fixDamR":
                        case "limitMoveSkill":
                        case "tile":
                        case "footholdRect":
                        case "targetMobType":
                        case "areaWarning":
                        case "arType":
                        case "tremble":
                        case "otherSkill":
                        case "etcEffect":
                        case "etcEffect1":
                        case "etcEffect2":
                        case "etcEffect3":
                        case "bombInfo":
                        case "affected_pre":
                        case "fixDamR_BT":
                        case "affectedPhase":
                        case "screen":
                        case "notMissAttack":
                        case "ignoreEvasion":
                        case "fadeinfo":
                        case "randomTarget":
                        case "option_linkedMob":
                        case "affected0":
                        case "summonOnce":
                        case "head":
                        case "mobGroup":
                        case "exceptRange":
                        case "exchangeAttack":
                        case "range":
                        case "addDam":
                        case "special":
                        case "target":
                        case "i52":
                        case "start":
                        case "cancleType":
                        case "succeed":
                        case "failed":
                        case "during":
                        case "castingBarHide":
                        case "skillCancelAlways":
                        case "cancleDamage":
                        case "cancleDamageMultiplier":
                        case "bounceBall":
                        case "info2":
                        case "regen":
                        case "kockBackD":
                        case "areaSequenceDelay":
                        case "areaSequenceRandomSplit":
                        case "accelerationEffect":
                        case "repeatEffect":
                        case "brightness":
                        case "brightnessDuration":
                        case "success":
                        case "fail":
                        case "affected_S":
                        case "appear":
                        case "affected_XS":
                        case "disappear":
                        case "command":
                        case "damIncPos": // May be useful
                        case "option_poison": // ?
                        case "phaseUserCount": // I think this is done client side (users hit mapped to phase?)
                            break;
                        default:
                            if (!unks.contains(name)) {
                                if (LOG_UNKNOWN_STATS) {
                                    log.warn("Unknown MobSkillStat {} with value {} (skill {} level {})", name, value, skillID, level);
                                }
                                unks.add(name);
                            }
                            break;
                    }
                }
                addMobSkillInfo(msi);
            }
        }

    }

    public static void saveMobSkillsToDat(String dir) {
        Util.makeDirIfAbsent(dir);
        for (Map.Entry<Short, Map<Short, MobSkillInfo>> entry : getMobSkillInfos().entrySet()) {
            short id = entry.getKey();
            for (Map.Entry<Short, MobSkillInfo> entry2 : entry.getValue().entrySet()) {
                short level = entry2.getKey();
                MobSkillInfo msi = entry2.getValue();
                File file = new File(String.format("%s/%d-%d.dat", dir, id, level));
                try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(file))) {
                    dos.writeShort(msi.getId());
                    dos.writeShort(msi.getLevel());
                    // wow this is ugly
                    // but it's just a de/encoder, so who cares
                    boolean hasLt = msi.getLt() != null;
                    dos.writeBoolean(hasLt);
                    if (hasLt) {
                        dos.writeInt(msi.getLt().getX());
                        dos.writeInt(msi.getLt().getY());
                    }
                    boolean hasRb = msi.getRb() != null;
                    dos.writeBoolean(hasRb);
                    if (hasRb) {
                        dos.writeInt(msi.getRb().getX());
                        dos.writeInt(msi.getRb().getY());
                    }
                    boolean hasLt2 = msi.getLt2() != null;
                    dos.writeBoolean(hasLt2);
                    if (hasLt2) {
                        dos.writeInt(msi.getLt2().getX());
                        dos.writeInt(msi.getLt2().getY());
                    }
                    boolean hasRb2 = msi.getRb2() != null;
                    dos.writeBoolean(hasRb2);
                    if (hasRb2) {
                        dos.writeInt(msi.getRb2().getX());
                        dos.writeInt(msi.getRb2().getY());
                    }
                    boolean hasLt3 = msi.getLt3() != null;
                    dos.writeBoolean(hasLt3);
                    if (hasLt3) {
                        dos.writeInt(msi.getLt3().getX());
                        dos.writeInt(msi.getLt3().getY());
                    }
                    boolean hasRb3 = msi.getRb3() != null;
                    dos.writeBoolean(hasRb3);
                    if (hasRb3) {
                        dos.writeInt(msi.getRb3().getX());
                        dos.writeInt(msi.getRb3().getY());
                    }
                    dos.writeShort(msi.getMobSkillStats().size());
                    for (Map.Entry<MobSkillStat, String> msiString : msi.getMobSkillStats().entrySet()) {
                        dos.writeByte(msiString.getKey().ordinal());
                        dos.writeUTF(msiString.getValue());
                    }
                    dos.writeShort(msi.getInts().size());
                    for (int i : msi.getInts()) {
                        dos.writeInt(i);
                    }
                } catch (IOException e) {
                    log.error("Error saving mob skill {} (level {}) to file.", id, level);
                }
            }
        }
    }

    private static MobSkillInfo loadMobSkillFromFile(File f) {
        MobSkillInfo msi = null;
        try (DataInputStream dis = new DataInputStream(new FileInputStream(f))) {
            msi = new MobSkillInfo();
            msi.setId(dis.readShort());
            msi.setLevel(dis.readShort());
            boolean hasPos = dis.readBoolean();
            if (hasPos) {
                msi.setLt(new Position(dis.readInt(), dis.readInt()));
            }
            hasPos = dis.readBoolean();
            if (hasPos) {
                msi.setRb(new Position(dis.readInt(), dis.readInt()));
            }
            hasPos = dis.readBoolean();
            if (hasPos) {
                msi.setLt2(new Position(dis.readInt(), dis.readInt()));
            }
            hasPos = dis.readBoolean();
            if (hasPos) {
                msi.setRb2(new Position(dis.readInt(), dis.readInt()));
            }
            hasPos = dis.readBoolean();
            if (hasPos) {
                msi.setLt3(new Position(dis.readInt(), dis.readInt()));
            }
            hasPos = dis.readBoolean();
            if (hasPos) {
                msi.setRb3(new Position(dis.readInt(), dis.readInt()));
            }
            short size = dis.readShort();
            for (int i = 0; i < size; i++) {
                MobSkillStat mss = MobSkillStat.values()[dis.readByte()];
                String value = dis.readUTF();
                msi.putMobSkillStat(mss, value);
            }
            size = dis.readShort();
            for (int i = 0; i < size; i++) {
                msi.addIntToList(dis.readInt());
            }
//            size = dis.readByte();
//            if (size > 0) {
//                List<Position> fixedSummonPos = new ArrayList<>();
//                for (int i = 0; i < size; i++) {
//                    fixedSummonPos.add(new Position(dis.readShort(), dis.readShort()));
//                }
//                msi.setFixedSummonPos(fixedSummonPos);
//            }
//            size = dis.readByte();
//            if (size > 0) {
//                List<Byte> fixedSummonDir = new ArrayList<>();
//                for (int i = 0; i < size; i++) {
//                    fixedSummonDir.add(dis.readByte());
//                }
//                msi.setFixedSummonDir(fixedSummonDir);
//            }
            addMobSkillInfo(msi);
        } catch (IOException e) {
            log.error("Error loading mob skill from file.", e);
        }
        return msi;
    }

    private static MobSkillInfo getMobSkillInfoByIdAndLevel(short id, short level) {
        Map<Short, MobSkillInfo> innerMap = getMobSkillInfos().get(id);
        if (innerMap == null || innerMap.get(level) == null) {
            return loadMobSkillInfoByIdAndLevel(id, level);
        } else {
            return innerMap.get(level);
        }
    }

    public static MobSkillInfo getMobSkillInfoByIdAndLevel(int id, int level) {
        return getMobSkillInfoByIdAndLevel((short) id, (short) level);
    }

    private static MobSkillInfo loadMobSkillInfoByIdAndLevel(short id, short level) {
        File file = new File(String.format("%s/mobSkills/%d-%d.dat", ServerConstants.DAT_DIR, id, level));
        if (file.exists()) {
            return loadMobSkillFromFile(file);
        } else {
            log.error("Could not load mob skill {} (level {}).", id, level);
            return null;
        }
    }

    public static void generateDatFiles() {
        log.info("Started generating skill data.");
        long start = System.currentTimeMillis();
        loadSkillsFromWz();
        saveSkills(ServerConstants.DAT_DIR + "/skills");
        log.info(String.format("Completed generating skill data in %dms.", System.currentTimeMillis() - start));
        log.info("Started generating mob skill data.");
        start = System.currentTimeMillis();
        loadMobSkillsFromWz();
        saveMobSkillsToDat(ServerConstants.DAT_DIR + "/mobSkills");
        log.info(String.format("Completed generating mob skill data in %dms.", System.currentTimeMillis() - start));
    }

    public static void main(String[] args) {
        generateDatFiles();
    }

    public static void clear() {
        getMobSkillInfos().clear();
    }
}
