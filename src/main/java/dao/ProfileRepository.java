package dao;

import dto.Profile;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfileRepository {

    public static Profile getProfileByUserId(String userId) {
        String sql = "SELECT * FROM profile WHERE user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Profile profile = new Profile();
                    profile.setUserId(rs.getString("user_id"));
                    profile.setNickname(rs.getString("nickname"));
                    profile.setDescription(rs.getString("description"));
                    profile.setProfileImage(rs.getString("profile_image"));
                    return profile;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 프로필 없으면 INSERT, 있으면 UPDATE
    public static void upsertProfile(Profile profile) {
        String sql = """
            INSERT INTO profile (user_id, nickname, description, profile_image)
            VALUES (?, ?, ?, ?)
            ON DUPLICATE KEY UPDATE
                nickname = VALUES(nickname),
                description = VALUES(description),
                profile_image = VALUES(profile_image)
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, profile.getUserId());
            pstmt.setString(2, profile.getNickname());
            pstmt.setString(3, profile.getDescription());
            pstmt.setString(4, profile.getProfileImage());
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
