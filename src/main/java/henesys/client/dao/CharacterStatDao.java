package henesys.client.dao;

import henesys.client.character.CharacterStat;
import henesys.connection.db.DatabaseManager;

import java.sql.*;

public class CharacterStatDao {

    public boolean characterExists(String name) {
        String sql = "SELECT * FROM characterStat WHERE name = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public CharacterStat findById(int characterId, int id) {
        String sql = "SELECT * FROM characterStat WHERE id = ?";
        CharacterStat cs = new CharacterStat();
        cs.setCharacterId(characterId);
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                cs.setId(rs.getInt("id"));
                cs.setName(rs.getString("name"));
                cs.setSkin((byte) rs.getInt("skin"));
                cs.setFace(rs.getInt("face"));
                cs.setHair(rs.getInt("hair"));
                cs.setLevel((byte) rs.getInt("level"));
                cs.setJob((short) rs.getInt("job"));
                cs.setSubJob((short) rs.getInt("subJob"));
                cs.setStr((short) rs.getInt("str"));
                cs.setDex((short) rs.getInt("dex"));
                cs.setInt((short) rs.getInt("intt"));
                cs.setLuk((short) rs.getInt("luk"));
                cs.setHp(rs.getInt("hp"));
                cs.setMaxHp(rs.getInt("maxHp"));
                cs.setMp(rs.getInt("mp"));
                cs.setMaxMp(rs.getInt("maxMp"));
                cs.setAp((short) rs.getInt("ap"));
                cs.setSp((short) rs.getInt("sp"));
                cs.setExp(rs.getInt("exp"));
                cs.setFame((short) rs.getInt("fame"));
                cs.setTempExp(rs.getInt("tempExp"));
                cs.setFieldId(rs.getInt("fieldId"));
                cs.setFieldPortal(rs.getInt("fieldPortal"));
                cs.setPlayTime(rs.getInt("playTime"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cs;
    }
        public int createCharStat (CharacterStat cs){
            String sql = "INSERT INTO characterStat (name, gender, skin, face, hair, level, job, subJob, str, dex, intt, " +
                    "luk, hp, maxHp, mp, maxMp, ap, sp, exp, fame, tempExp, fieldId, fieldPortal, playTime) VALUES (?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, cs.getName());
                statement.setInt(2, cs.getGender());
                statement.setInt(3, cs.getSkin());
                statement.setInt(4, cs.getFace());
                statement.setInt(5, cs.getHair());
                statement.setInt(6, cs.getLevel());
                statement.setInt(7, cs.getJob());
                statement.setInt(8, cs.getSubJob());
                statement.setInt(9, cs.getStr());
                statement.setInt(10, cs.getDex());
                statement.setInt(11, cs.getIntt());
                statement.setInt(12, cs.getLuk());
                statement.setInt(13, cs.getHp());
                statement.setInt(14, cs.getMaxHp());
                statement.setInt(15, cs.getMp());
                statement.setInt(16, cs.getMaxMp());
                statement.setInt(17, cs.getAp());
                statement.setInt(18, cs.getSp());
                statement.setInt(19, cs.getExp());
                statement.setInt(20, cs.getFame());
                statement.setInt(21, cs.getTempExp());
                statement.setInt(22, cs.getFieldId());
                statement.setInt(23, cs.getFieldPortal());
                statement.setInt(24, cs.getPlayTime());
                statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return 0;
        }
    }
