package henesys.handlers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import henesys.Server;
import henesys.ServerConfig;
import henesys.client.Account;
import henesys.client.Client;
import henesys.client.User;
import henesys.client.dao.AccountDao;
import henesys.client.dao.UserDao;
import henesys.connection.InPacket;
import henesys.connection.packet.Login;
import henesys.enums.LoginType;
import henesys.handlers.header.InHeader;
import henesys.world.World;

import java.util.Set;

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
        c.write(Login.selectWorldResult(user, account));
    }
}
