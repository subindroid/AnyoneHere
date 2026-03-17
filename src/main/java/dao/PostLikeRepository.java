package dao;

import util.DBUtil;

import java.sql.*;

public class PostLikeRepository {

    /**
     * 좋아요 토글. 단일 Connection + 트랜잭션으로 race condition 방지.
     * @return true = 좋아요 추가됨, false = 취소됨
     */
    public static boolean toggle(int postId, String userId) {
        String checkSql  = "SELECT COUNT(*) FROM post_likes WHERE post_id = ? AND user_id = ?";
        String insertSql = "INSERT IGNORE INTO post_likes (post_id, user_id) VALUES (?, ?)";
        String deleteSql = "DELETE FROM post_likes WHERE post_id = ? AND user_id = ?";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                boolean liked;
                try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                    ps.setInt(1, postId);
                    ps.setString(2, userId);
                    try (ResultSet rs = ps.executeQuery()) {
                        liked = rs.next() && rs.getInt(1) > 0;
                    }
                }

                String sql = liked ? deleteSql : insertSql;
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setInt(1, postId);
                    ps.setString(2, userId);
                    ps.executeUpdate();
                }

                conn.commit();
                return !liked;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isLiked(int postId, String userId) {
        String sql = "SELECT COUNT(*) FROM post_likes WHERE post_id = ? AND user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.setString(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getLikeCount(int postId) {
        String sql = "SELECT COUNT(*) FROM post_likes WHERE post_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void like(int postId, String userId) {
        String sql = "INSERT IGNORE INTO post_likes (post_id, user_id) VALUES (?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void unlike(int postId, String userId) {
        String sql = "DELETE FROM post_likes WHERE post_id = ? AND user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
