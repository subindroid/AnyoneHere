package dao;

import dto.AddSpotApplication;
import util.DBUtil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class AddSpotApplicationRepository {


    public static AddSpotApplication getAddApplicationByAddApplicationId(int applicationId) {
        AddSpotApplication addSpotApplication = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM add_spot_applications WHERE application_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, applicationId);
            rs = ps.executeQuery();

            if (rs.next()) {
                addSpotApplication = new AddSpotApplication();
                addSpotApplication.setApplicationId(rs.getInt("application_id"));
                addSpotApplication.setSpotName(rs.getString("spot_name"));
                addSpotApplication.setSpotDescription(rs.getString("spot_description"));
                addSpotApplication.setSpotLatitude(rs.getDouble("spot_latitude"));
                addSpotApplication.setSpotLongitude(rs.getDouble("spot_longitude"));
                addSpotApplication.setStatus(rs.getString("add_spot_application_status"));
                Timestamp ts = rs.getTimestamp("add_spot_created_at");
                if (ts != null) {
                    addSpotApplication.setCreatedAt(ts.toLocalDateTime());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }

        return addSpotApplication;
    }

    public static void insert(AddSpotApplication app) {
        String sql = """
        INSERT INTO add_spot_applications
        (user_id, spot_name, spot_latitude, spot_longitude,
         spot_description, add_spot_application_status, add_spot_created_at)
        VALUES (?, ?, ?, ?, ?, ?, NOW())
    """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, app.getUserId());
            pstmt.setString(2, app.getSpotName());
            pstmt.setDouble(3, app.getSpotLatitude());
            pstmt.setDouble(4, app.getSpotLongitude());
            pstmt.setString(5, app.getSpotDescription());
            pstmt.setString(6, app.getStatus());

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateStatus(int applicationId, String status) {
        String sql = """
        UPDATE add_spot_applications
        SET add_spot_application_status = ?
        WHERE application_id = ?
    """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, applicationId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public static ArrayList<AddSpotApplication> getAddSpotApplicationByUserId(String userId) {
        ArrayList<AddSpotApplication> addSpotApplications = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM add_spot_applications WHERE user_id = ? ORDER BY add_spot_created_at DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                AddSpotApplication addSpotApplication = new AddSpotApplication();
                addSpotApplication.setApplicationId(rs.getInt("application_id"));
                addSpotApplication.setUserId(rs.getString("user_id"));
                addSpotApplication.setSpotName(rs.getString("spot_name"));
                addSpotApplication.setSpotDescription(rs.getString("spot_description"));
                Timestamp ts = rs.getTimestamp("add_spot_created_at");
                if (ts != null) {
                    addSpotApplication.setCreatedAt(ts.toLocalDateTime());
                }

                addSpotApplication.setSpotLatitude(rs.getDouble("spot_latitude"));
                addSpotApplication.setSpotLongitude(rs.getDouble("spot_longitude"));
                addSpotApplication.setStatus(rs.getString("add_spot_application_status"));
                addSpotApplications.add(addSpotApplication);
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

        return addSpotApplications;
    }

}
