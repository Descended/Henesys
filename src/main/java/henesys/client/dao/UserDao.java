package henesys.client.dao;

import henesys.client.User;
import henesys.connection.db.DatabaseManager;
import henesys.enums.UserType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    public User findByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        User user = null;
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setPic(rs.getString("pic"));
                user.setBanExpireDate(rs.getDate("banExpireDate"));
                user.setBanReason(rs.getString("banReason"));
                user.setOffensePoints(rs.getInt("offensePoints"));
                user.setNxPrepaid(rs.getInt("nxPrepaid"));
                user.setVotePoints(rs.getInt("votePoints"));
                user.setDonationPoints(rs.getInt("donationPoints"));
                user.setMaplePoints(rs.getInt("maplePoints"));
                user.setUserType(UserType.getByVal(rs.getInt("userType")));
                user.setBirthDate(rs.getDate("birthDate"));
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void registerUser(User user) {
        String sql = "INSERT INTO user (username, email, password, pic, banExpireDate, banReason, offensePoints, " +
                "nxPrepaid, votePoints, donationPoints, maplePoints, userType, birthDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getPic());
            statement.setDate(5, user.getBanExpireDate());
            statement.setString(6, user.getBanReason());
            statement.setInt(7, user.getOffensePoints());
            statement.setInt(8, user.getNxPrepaid());
            statement.setInt(9, user.getVotePoints());
            statement.setInt(10, user.getDonationPoints());
            statement.setInt(11, user.getMaplePoints());
            statement.setInt(12, user.getUserType().getVal());
            statement.setDate(13, user.getBirthDate());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
