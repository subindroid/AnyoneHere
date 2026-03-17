package dao;

import dto.PostImage;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostImageRepository {

    public static List<PostImage> getImagesByPostId(int postId) {
        List<PostImage> list = new ArrayList<>();
        String sql = "SELECT * FROM post_images WHERE post_id = ? ORDER BY sort_order";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    PostImage img = new PostImage();
                    img.setImageId(rs.getInt("image_id"));
                    img.setPostId(rs.getInt("post_id"));
                    img.setImagePath(rs.getString("image_path"));
                    img.setSortOrder(rs.getInt("sort_order"));
                    list.add(img);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void insertImages(int postId, List<String> imagePaths) {
        if (imagePaths == null || imagePaths.isEmpty()) return;

        String sql = "INSERT INTO post_images (post_id, image_path, sort_order) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < imagePaths.size(); i++) {
                ps.setInt(1, postId);
                ps.setString(2, imagePaths.get(i));
                ps.setInt(3, i);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 특정 이미지 ID 삭제 (본인 게시글 이미지인지 postId로 검증)
    public static void deleteImage(int imageId, int postId) {
        String sql = "DELETE FROM post_images WHERE image_id = ? AND post_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, imageId);
            ps.setInt(2, postId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 이미지 경로 조회 (파일 삭제 전 경로 확인용)
    public static String getImagePath(int imageId) {
        String sql = "SELECT image_path FROM post_images WHERE image_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, imageId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString("image_path");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
