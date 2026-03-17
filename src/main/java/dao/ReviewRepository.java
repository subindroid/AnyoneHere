package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import util.DBUtil;

import dto.Review;
import dto.User;

public class ReviewRepository {
    private static ReviewRepository instance = new ReviewRepository();

    public static ReviewRepository getInstance() {
        return instance;
    }

    /** 해당 유저가 해당 스팟에 이미 리뷰를 작성했는지 확인 */
    public static boolean hasReviewed(String userId, int spotId) {
        String sql = "SELECT COUNT(*) FROM reviews WHERE user_id = ? AND spot_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setInt(2, spotId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 리뷰 삭제 (본인 리뷰만) */
    public static void deleteReview(int reviewId, String userId) {
        String sql = "DELETE FROM reviews WHERE review_id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, reviewId);
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getAverageRatingBySpotId(int spotId) {
        String sql = "SELECT AVG(review_rating) AS avg_rating FROM reviews WHERE spot_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, spotId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getDouble("avg_rating");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static ArrayList<Review> getReviewsBySpotId(int spotId) {
        ArrayList<Review> reviews = new ArrayList<>();
        String sql = "SELECT r.*, COALESCE(p.nickname, r.user_id) AS author_name "
                + "FROM reviews r LEFT JOIN profile p ON r.user_id = p.user_id "
                + "WHERE r.spot_id = ? ORDER BY r.review_created_at DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, spotId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Review review = new Review();
                    review.setReviewId(rs.getInt("review_id"));
                    review.setUserId(rs.getString("user_id"));
                    review.setAuthorName(rs.getString("author_name"));
                    review.setSpotId(rs.getInt("spot_id"));
                    review.setReviewText(rs.getString("review_text"));
                    java.sql.Timestamp ts = rs.getTimestamp("review_created_at");
                    if (ts != null) review.setReviewCreatedAt(ts.toLocalDateTime());
                    review.setReviewRating(rs.getDouble("review_rating"));
                    reviews.add(review);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviews;
    }

    public static void addReview(String userId, int spotId, String content, double rating) {
        String sql = "INSERT INTO reviews (user_id, spot_id, review_text, review_rating, review_created_at) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setInt(2, spotId);
            pstmt.setString(3, content);
            pstmt.setDouble(4, rating);
            pstmt.setTimestamp(5, new java.sql.Timestamp(System.currentTimeMillis()));
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ReviewRepository() {
        // 기본 생성자
    }
}
