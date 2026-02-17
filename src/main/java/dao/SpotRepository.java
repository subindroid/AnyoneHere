package dao;

import dto.Spot;
import java.util.ArrayList;
import java.sql.*;
import util.DBUtil;

public class SpotRepository {
    private static SpotRepository instance = new SpotRepository();

    public static SpotRepository getInstance() {
        return instance;
    }

    public SpotRepository() {
        // TODO Auto-generated constructor stub
    }

    public Spot getSpotBySpotId(int spotId) {
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
}
