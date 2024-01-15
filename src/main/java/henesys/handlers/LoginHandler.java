package henesys.handlers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import henesys.Server;
import henesys.ServerConfig;
import henesys.client.Account;
import henesys.client.Client;
import henesys.client.Trunk;
import henesys.client.User;
import henesys.client.character.Char;
import henesys.client.character.CharacterStat;
import henesys.client.character.avatar.AvatarLook;
import henesys.client.dao.*;
import henesys.connection.InPacket;
import henesys.connection.packet.Login;
import henesys.constants.GameConstants;
import henesys.constants.ItemConstants;
import henesys.enums.CharNameResult;
import henesys.enums.InvType;
import henesys.enums.LoginType;
import henesys.handlers.header.InHeader;
import henesys.items.Equip;
import henesys.items.Inventory;
import henesys.items.dao.InventoryDao;
import henesys.items.dao.ItemDao;
import henesys.loaders.ItemData;
import henesys.world.Channel;
import henesys.world.World;

import java.util.ArrayList;
import java.util.List;

import static henesys.enums.InvType.EQUIP;
import static henesys.enums.InvType.EQUIPPED;


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
                int userId = userDao.registerUser(user);
                user.setId(userId);
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
            TrunkDao trunkDao = new TrunkDao();
            int trunkId = trunkDao.createTrunk(new Trunk(GameConstants.TRUNK_SLOT_COUNT, 0));
            AccountDao dao = new AccountDao();
            int accountId = dao.createAccount(account, user.getId(), worldId, trunkId);
            account.setId(accountId);
            user.addAccount(account);
            account.setUser(user);
        }
        c.setAccount(account);
        c.write(Login.selectWorldResult(user, account));
    }
    @Handler(op = InHeader.SELECT_CHARACTER)
    public static void handleCharSelect(Client c, InPacket inPacket) {
        int characterId = inPacket.decodeInt();
        String macs = inPacket.decodeString();
        String hostString = inPacket.decodeString();
        byte worldId = c.getWorldId();
        byte channelId = c.getChannel();
        Channel channel = Server.getInstance().getWorldById(worldId).getChannelById(channelId);
        if (c.getAccount().hasCharacter(characterId)) {
            c.setChr(c.getAccount().getCharById(characterId));
            InventoryDao inventoryDao = new InventoryDao();
            List<Inventory> inventories = inventoryDao.getInventoriesByCharacterId(characterId);
            for (Inventory inventory : inventories) {
                if (inventory.getType() == EQUIPPED) {
                    c.getChr().setEquippedInventory(inventory);
                } else if (inventory.getType() == InvType.EQUIP) {
                    c.getChr().setEquipInventory(inventory);
                } else if (inventory.getType() == InvType.CONSUME) {
                    c.getChr().setConsumeInventory(inventory);
                } else if (inventory.getType() == InvType.INSTALL) {
                    c.getChr().setInstallInventory(inventory);
                } else if (inventory.getType() == InvType.ETC) {
                    c.getChr().setEtcInventory(inventory);
                } else if (inventory.getType() == InvType.CASH) {
                    c.getChr().setCashInventory(inventory);
                }
            }
            Server.getInstance().getWorldById(worldId).getChannelById(channelId).addClientInTransfer(channelId, characterId, c);
            c.write(Login.selectCharacterResult(LoginType.Success, (byte) 0, channel.getPort(), characterId));
        }
    }
    @Handler(op = InHeader.CHECK_DUPLICATED_ID)
    public static void handleCheckDuplicatedID(Client c, InPacket inPacket) {
        String name = inPacket.decodeString();
        CharNameResult code;
        if (!GameConstants.isValidName(name)) {
            code = CharNameResult.Unavailable_Invalid;
        } else {
            CharacterStatDao characterStatDao = new CharacterStatDao();
            code = characterStatDao.characterExists(name) ? CharNameResult.Unavailable_InUse : CharNameResult.Available;
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
        int finalHair = hair + hairColor;
        CharacterStat characterStat = new CharacterStat(name, gender, (byte) skin, face, hair);
        characterStat.setLevel((byte) 1);
        characterStat.setStr((short) 4);
        characterStat.setDex((short) 4);
        characterStat.setInt((short) 4);
        characterStat.setLuk((short) 4);
        characterStat.setHp(50);
        characterStat.setMaxHp(50);
        characterStat.setMp(5);
        characterStat.setMaxMp(5);
        characterStat.setFieldId(180000001);
        characterStatDao = new CharacterStatDao();
        int characterStatId = characterStatDao.createCharStat(characterStat);
        AvatarLook avatarLook = new AvatarLook(gender, skin, face, hair, weapon, items);
        AvatarLookDao avatarLookDao = new AvatarLookDao();
        int avatarLookId = avatarLookDao.createAvatarLook(avatarLook);
        avatarLookDao.saveEquips(avatarLookId, avatarLook.getHairEquips());
        CharDao charDao = new CharDao();
        int characterId = charDao.createCharacter(acc.getId(), characterStatId, avatarLookId);
        Char chr = new Char(characterId, characterStat, acc.getId(), avatarLook);
        chr.setClient(c);
        InventoryDao inventoryDao = new InventoryDao();
        int equippedInventoryId = inventoryDao.createInventory(chr.getEquippedInventory());
        chr.getEquippedInventory().setId(equippedInventoryId);
        inventoryDao.createInventory(chr.getEquipInventory());
        inventoryDao.createInventory(chr.getConsumeInventory());
        inventoryDao.createInventory(chr.getEtcInventory());
        inventoryDao.createInventory(chr.getInstallInventory());
        inventoryDao.createInventory(chr.getCashInventory());
        for (int i : chr.getAvatarLook().getHairEquips()) {
            Equip equip = ItemData.getEquipDeepCopyFromID(i, false);
            if (equip != null && equip.getItemId() >= 1000000) {
                equip.setBagIndex(ItemConstants.getBodyPartFromItem(
                        equip.getItemId(), chr.getAvatarLook().getGender()));
                chr.addItemToInventory(EQUIPPED, equip, true, true);
            }
        }
        characterStat.setCharacterId(characterId);
        ItemDao itemDao = new ItemDao();
        itemDao.saveItems(chr.getEquippedInventory());
        c.getAccount().addCharacter(chr);
        c.write(Login.createNewCharacterResult(LoginType.Success, chr));
    }

}
