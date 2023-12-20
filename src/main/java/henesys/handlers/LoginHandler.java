package henesys.handlers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import henesys.Server;
import henesys.ServerConfig;
import henesys.client.Account;
import henesys.client.Client;
import henesys.client.User;
import henesys.client.character.Char;
import henesys.client.character.CharacterStat;
import henesys.client.dao.AccountDao;
import henesys.client.dao.CharacterStatDao;
import henesys.client.dao.UserDao;
import henesys.connection.InPacket;
import henesys.connection.packet.Login;
import henesys.constants.GameConstants;
import henesys.constants.ItemConstants;
import henesys.enums.CharNameResult;
import henesys.enums.LoginType;
import henesys.handlers.header.InHeader;
import henesys.loaders.ItemData;
import henesys.world.World;


public class LoginHandler {

    @Handler(op = InHeader.CHECK_PASSWORD)
    public static void handleCheckPassword(Client c, InPacket inPacket) {
        String username = inPacket.decodeString();
        String password = inPacket.decodeString();
        UserDao userDao = new UserDao();
        User user = userDao.findByUsername(username);
        if (user == null) {
            if (ServerConfig.AUTO_REGISTER) {
                String encryptedPassword = BCrypt.withDefaults().hashToString(12, password.toCharArray());
                user = new User(username, encryptedPassword);
                userDao.registerUser(user);
                c.setUser(user);
                c.write(Login.checkPasswordResult(LoginType.Success, user));
            } else {
                c.write(Login.checkPasswordResult(LoginType.NotRegistered, null));
            }
        } else {
            if (!BCrypt.verifyer().verify(password.toCharArray(), user.getPassword()).verified) { // If the password is incorrect
                c.write(Login.checkPasswordResult(LoginType.IncorrectPassword, null));
                return;
            }
            c.setUser(user);
            c.write(Login.checkPasswordResult(LoginType.Success, c.getUser()));
        }
    }

    @Handler(op = InHeader.WORLD_REQUEST)
    public static void handleWorldRequest(Client c, InPacket inPacket) {
        for (World world : Server.getInstance().getWorlds()) {
            c.write(Login.sendWorldInformation(world, null));
        }
        c.write(Login.sendWorldInformationEnd());
    }

    @Handler(op = InHeader.WORLD_STATUS_REQUEST)
    public static void handleWorldStatusRequest(Client c, InPacket inPacket) {
        byte worldId = inPacket.decodeByte();
        c.write(Login.sendServerStatus());
    }

    @Handler(op = InHeader.SELECT_WORLD)
    public static void handleSelectWorld(Client c, InPacket inPacket) {
        byte unk = inPacket.decodeByte();
        byte worldId = inPacket.decodeByte();
        byte channel = (byte) (inPacket.decodeByte() + 1);
        User user = c.getUser();
        c.setWorldId(worldId);
        c.setChannel(channel);
        AccountDao accountDao = new AccountDao();
        Account account = accountDao.findByUserAndWorld(user, worldId);
        if (account == null) {
            account = new Account();
            account.setWorldId(worldId);
            user.addAccount(account);
            account.setUser(user);
        }
        c.setAccount(account);
        c.write(Login.selectWorldResult(user, account));
    }

    @Handler(op = InHeader.CHECK_DUPLICATED_ID)
    public static void handleCheckDuplicatedID(Client c, InPacket inPacket) {
        String name = inPacket.decodeString();
        CharNameResult code;
        if (!GameConstants.isValidName(name)) {
            code = CharNameResult.Unavailable_Invalid;
        } else {
//            code = CharDao.getFromDBByNameAndWorld(name, c.getAccount().getWorldId()) == null ? CharNameResult.Available : CharNameResult.Unavailable_InUse;
            code = CharNameResult.Available;
        }
        c.write(Login.checkDuplicatedIDResult(name, code.getVal()));
    }

    @Handler(op = InHeader.CREATE_NEW_CHARACTER)
    public static void handleCreateNewCharacter(Client c, InPacket inPacket) {
        Account acc = c.getAccount();
        String name = inPacket.decodeString();
        int job = inPacket.decodeInt();
        short subJob = inPacket.decodeShort();
        int face = inPacket.decodeInt();
        int hair = inPacket.decodeInt();
        int hairColor = inPacket.decodeInt();

        int skin = inPacket.decodeInt();

        int top = inPacket.decodeInt();
        int bottom = inPacket.decodeInt();
        int shoes = inPacket.decodeInt();
        int weapon = inPacket.decodeInt();
        //face, hair, skin, overall, top, bottom, cape, boots, weapon
        int[] items = new int[]{face, hair, skin, top, bottom, shoes, weapon};
        byte gender = inPacket.decodeByte();
        CharNameResult code = null;
        if (!ItemData.isStartingItems(items) || skin > ItemConstants.MAX_SKIN || skin < 0
                || face < ItemConstants.MIN_FACE || face > ItemConstants.MAX_FACE
                || hair < ItemConstants.MIN_HAIR || hair > ItemConstants.MAX_HAIR) {
//            c.getUser().getOffenseManager().addOffense("Tried to add items unavailable on char creation.");
            code = CharNameResult.Unavailable_CashItem;
        }

        CharacterStatDao characterStatDao = new CharacterStatDao();
        if (!GameConstants.isValidName(name)) {
            code = CharNameResult.Unavailable_Invalid;
        } else if (characterStatDao.characterExists(name)) {
            code = CharNameResult.Unavailable_InUse;
        }
        if (code != null) {
            c.write(Login.checkDuplicatedIDResult(name, code.getVal()));
            return;
        }
        c.getUser().setCurrentAcc(acc);
        int finalHair = hair + hairColor;
        Char chr = new Char(name, acc.getId(), job, subJob, gender, (byte) skin, face, hair, items);
        CharacterStat characterStat = chr.getCharacterStat();
        characterStat.setLevel((byte) 1);
        characterStat.setStr((short) 4);
        characterStat.setDex((short) 4);
        characterStat.setInt((short) 4);
        characterStat.setLuk((short) 4);
        characterStat.setHp(50);
        characterStat.setMaxHp(50);
        characterStat.setMp(5);
        characterStat.setMaxMp(5);
        c.write(Login.createNewCharacterResult(LoginType.Success, chr));
    }

}
