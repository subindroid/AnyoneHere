package dao;

import dto.PostComment;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostCommentRepository {

    public static List<PostComment> getCommentsByPostId(int postId) {
        List<PostComment> list = new ArrayList<>();
        String sql = "SELECT pc.*, COALESCE(pr.nickname, pc.user_id) AS author_name "
                + "FROM post_comments pc "
                + "LEFT JOIN profile pr ON pc.user_id = pr.user_id "
                + "WHERE pc.post_id = ? AND pc.is_deleted = FALSE "
                + "ORDER BY pc.created_at ASC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PostComment c = new PostComment();
                    c.setCommentId(rs.getInt("comment_id"));
                    c.setPostId(rs.getInt("post_id"));
                    c.setUserId(rs.getString("user_id"));
                    c.setContent(rs.getString("content"));
                    c.setAuthorName(rs.getString("author_name"));
                    Timestamp ts = rs.getTimestamp("created_at");
                    if (ts != null) c.setCreatedAt(ts.toLocalDateTime());
                    list.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void insert(PostComment comment) {
        String sql = "INSERT INTO post_comments (post_id, user_id, content) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, comment.getPostId());
            ps.setString(2, comment.getUserId());
            ps.setString(3, comment.getContent());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 소프트 삭제 (userId로 본인 댓글만)
    public static void delete(int commentId, String userId) {
        String sql = "UPDATE post_comments SET is_deleted = TRUE WHERE comment_id = ? AND user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, commentId);
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
