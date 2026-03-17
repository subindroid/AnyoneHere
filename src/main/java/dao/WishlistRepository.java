package dao;

import dto.Spot;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WishlistRepository {

    public static List<Spot> getWishlistSpots(String userId) {
        List<Spot> list = new ArrayList<>();
        String sql = "SELECT s.spot_id, s.spot_name, s.spot_image "
                + "FROM wishlist w JOIN spots s ON w.spot_id = s.spot_id "
                + "WHERE w.user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Spot spot = new Spot();
                    spot.setSpotId(rs.getInt("spot_id"));
                    spot.setSpotName(rs.getString("spot_name"));
                    spot.setSpotImage(rs.getString("spot_image"));
                    list.add(spot);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean isWishlisted(String userId, int spotId) {
        String sql = "SELECT COUNT(*) FROM wishlist WHERE user_id = ? AND spot_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setInt(2, spotId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void addWishlist(String userId, int spotId) {
        String sql = "INSERT INTO wishlist (user_id, spot_id) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setInt(2, spotId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeWishlist(String userId, int spotId) {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND spot_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, userId);
            ps.setInt(2, spotId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
