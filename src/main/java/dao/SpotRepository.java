package dao;

import dto.AddSpotApplication;
import dto.Spot;
import java.util.ArrayList;
import java.sql.*;
import util.DBUtil;

public class SpotRepository {

    public static Spot getSpotBySpotId(int spotId) {
        Spot spot = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            System.out.println(">>> DAO conn success: " + (conn != null));

            String sql = "SELECT * FROM spots WHERE spot_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, spotId);
            rs = ps.executeQuery();

            if (rs.next()) {  // 두 번째 rs.next() 호출
                spot = new Spot();
                spot.setSpotId(rs.getInt("spot_id"));
                spot.setSpotName(rs.getString("spot_name"));
                spot.setSpotDescription(rs.getString("spot_description"));
                spot.setSpotLatitude(rs.getDouble("latitude"));
                spot.setSpotLongitude(rs.getDouble("longitude"));
                spot.setRadiusM(rs.getDouble("radius_m"));
                spot.setSpotCategory(rs.getString("spot_category"));
                spot.setSpotImage(rs.getString("spot_image"));
                System.out.println(">>> DAO found spot: " + spot.getSpotName());
            } else {
                System.out.println(">>> DAO NO DATA for spotId: " + spotId);
            }

        } catch (Exception e) {
            System.out.println(">>> DAO Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }

        System.out.println(">>> DAO return spot: " + spot);
        return spot;
    }



    public static ArrayList<Spot> getAllSpots() {
        ArrayList<Spot> spots = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM spots";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Spot spot = new Spot();
                spot.setSpotId(rs.getInt("spot_id"));
                spot.setSpotName(rs.getString("spot_name"));
                spot.setSpotDescription(rs.getString("spot_description"));
                spot.setSpotLatitude(rs.getDouble("latitude"));
                spot.setSpotLongitude(rs.getDouble("longitude"));
                spot.setSpotCategory(rs.getString("spot_category"));
                spot.setSpotImage(rs.getString("spot_image"));

                spots.add(spot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return spots;
    }

    public static void insertSpot(AddSpotApplication app) {
        // category 숫자 문자열 → int 변환 (1~5)
        int categoryId;
        try {
            categoryId = Integer.parseInt(app.getSpotCategory());
        } catch (Exception e) {
            categoryId = 1; // 기본값: 카페/음식점
        }

        String sql = """
            INSERT INTO spots
            (spot_name, latitude, longitude, radius_m, spot_description, spot_image, spot_category)
            VALUES (?, ?, ?, 100.0, ?, ?, ?)
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, app.getSpotName());
            pstmt.setDouble(2, app.getSpotLatitude());
            pstmt.setDouble(3, app.getSpotLongitude());
            pstmt.setString(4, app.getSpotDescription());
            pstmt.setString(5, app.getSpotImage() != null ? app.getSpotImage() : "");
            pstmt.setInt(6, categoryId);

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteSpot(int spotId) {
        String sql = "DELETE FROM spots WHERE spot_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, spotId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
