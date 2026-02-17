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

    public static double getAverageRatingBySpotId(int spotId) {
        double avg = 0.0;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT AVG(review_rating) AS avg_rating FROM reviews WHERE spot_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, spotId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                avg = rs.getDouble("avg_rating");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
            }
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (Exception e) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }

        return avg;
    }

    public static ArrayList<Review> getReviewsBySpotId(int spotId) {
        ArrayList<Review> reviews = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM reviews WHERE spot_id = ? ORDER BY review_created_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, spotId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Review review = new Review();
                review.setReviewId(rs.getInt("review_id"));
                review.setUserId(rs.getString("user_id"));
                review.setSpotId(rs.getInt("spot_id"));
                review.setReviewText(rs.getString("review_text"));
                review.setReviewCreatedAt(rs.getTimestamp("review_created_at").toLocalDateTime());
                review.setReviewRating(rs.getDouble("review_rating"));
                reviews.add(review);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (Exception e) {
            }
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (Exception e) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
            }
        }

        return reviews;
    }

    public ReviewRepository() {
        // 기본 생성자
    }
}
