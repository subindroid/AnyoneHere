package dao;

import dto.User;
import util.DBUtil;

import java.sql.*;

public class UserRepository {

    public static User getUserById(String id) {
        User user = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM users WHERE user_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setUserId(rs.getString("user_id"));
                user.setUserPassword(rs.getString("user_password"));
                user.setUserName(rs.getString("user_name"));
                user.setUserEmail(rs.getString("user_email"));
                user.setUserPhone(rs.getString("user_phone"));
                user.setUserAddress(rs.getString("user_address"));
                user.setUserGender(rs.getString("user_gender"));
                Date userBirth = rs.getDate("user_birth");
                if (userBirth != null) {
                    user.setUserBirth(userBirth.toLocalDate());
                }
                Date joinDate = rs.getDate("created_at");
                if (joinDate != null) {
                    user.setCreatedAt(joinDate.toLocalDate());
                }
                user.setUserRole(rs.getString("user_role"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }

        return user;
    }

    public static boolean validateUser(String id, String password) {
        User user = getUserById(id);
        return user != null && password != null &&
                password.equals(user.getUserPassword());
    }

    public static void updateUser(User user) {

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();

            String sql = "UPDATE users SET "
                    + "user_password=?, "
                    + "user_name=?, "
                    + "user_email=?, "
                    + "user_phone=?, "
                    + "user_address=?, "
                    + "user_gender=?, "
                    + "user_birth=? "
                    + "WHERE user_id=?";

            ps = conn.prepareStatement(sql);

            ps.setString(1, user.getUserPassword());
            ps.setString(2, user.getUserName());
            ps.setString(3, user.getUserEmail());
            ps.setString(4, user.getUserPhone());
            ps.setString(5, user.getUserAddress());
            ps.setString(6, user.getUserGender());

            if (user.getUserBirth() != null) {
                ps.setDate(7, Date.valueOf(user.getUserBirth()));
            } else {
                ps.setNull(7, Types.DATE);
            }

            ps.setString(8, user.getUserId());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }
    public static void addUser(User user) {

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DBUtil.getConnection();

            String sql = "INSERT INTO users "
                    + "(user_id, user_password, user_name, user_email, "
                    + "user_phone, user_address, user_gender, user_birth, created_at) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            ps = conn.prepareStatement(sql);

            ps.setString(1, user.getUserId());
            ps.setString(2, user.getUserPassword());
            ps.setString(3, user.getUserName());
            ps.setString(4, user.getUserEmail());
            ps.setString(5, user.getUserPhone());
            ps.setString(6, user.getUserAddress());
            ps.setString(7, user.getUserGender());

            if (user.getUserBirth() != null) {
                ps.setDate(8, Date.valueOf(user.getUserBirth()));
            } else {
                ps.setNull(8, Types.DATE);
            }

            ps.setTimestamp(9, Timestamp.valueOf(user.getCreatedAt().atStartOfDay()));

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(null, ps, conn);
        }
    }
}
