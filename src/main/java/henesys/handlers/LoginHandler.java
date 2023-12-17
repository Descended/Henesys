package henesys.handlers;

import henesys.Server;
import henesys.ServerConfig;
import henesys.client.Client;
import henesys.client.User;
import henesys.client.dao.UserDao;
import henesys.connection.InPacket;
import henesys.connection.packet.Login;
import henesys.enums.LoginType;
import henesys.handlers.header.InHeader;
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
                user = new User(username, password);
                userDao.registerUser(user);
                c.setUser(user);
                c.write(Login.checkPasswordResult(LoginType.Success, user));
            } else {
                c.write(Login.checkPasswordResult(LoginType.NotRegistered, null));
            }
        } else {
            if (!user.getPassword().equals(password)) {
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
}
