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
}
