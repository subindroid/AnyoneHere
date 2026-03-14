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


    public static AddSpotApplication getAddApplicationById(int applicationId) {
        AddSpotApplication addSpotApplication = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM add_spot_applications WHERE add_application_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, applicationId);
            rs = ps.executeQuery();

            if (rs.next()) {
                addSpotApplication = new AddSpotApplication();
                addSpotApplication.setApplicationId(rs.getInt("add_application_id"));
                addSpotApplication.setSpotName(rs.getString("spot_name"));
                addSpotApplication.setSpotDescription(rs.getString("spot_description"));
                addSpotApplication.setSpotLatitude(rs.getDouble("spot_latitude"));
                addSpotApplication.setSpotLongitude(rs.getDouble("spot_longitude"));
                addSpotApplication.setStatus(rs.getString("add_status"));
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
         spot_description, add_status, spot_category, spot_image, added_spot_address)
        VALUES (?, ?, ?, ?, ?, 'PENDING', ?, ?, ?)
    """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, app.getUserId());
            pstmt.setString(2, app.getSpotName());
            pstmt.setDouble(3, app.getSpotLatitude());
            pstmt.setDouble(4, app.getSpotLongitude());
            pstmt.setString(5, app.getSpotDescription());
            pstmt.setString(6, app.getSpotCategory() != null ? app.getSpotCategory() : "cafe");
            pstmt.setString(7, app.getSpotImage() != null ? app.getSpotImage() : "");
            pstmt.setString(8, app.getSpotAddress() != null ? app.getSpotAddress() : "");

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<AddSpotApplication> getAllPending() {
        ArrayList<AddSpotApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM add_spot_applications WHERE add_status = 'PENDING' ORDER BY add_spot_created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                AddSpotApplication app = new AddSpotApplication();
                app.setApplicationId(rs.getInt("add_application_id"));
                app.setUserId(rs.getString("user_id"));
                app.setSpotName(rs.getString("spot_name"));
                app.setSpotLatitude(rs.getDouble("spot_latitude"));
                app.setSpotLongitude(rs.getDouble("spot_longitude"));
                app.setSpotDescription(rs.getString("spot_description"));
                app.setStatus(rs.getString("add_status"));
                app.setSpotCategory(rs.getString("spot_category"));
                app.setSpotImage(rs.getString("spot_image"));
                app.setSpotAddress(rs.getString("added_spot_address"));
                Timestamp ts = rs.getTimestamp("add_spot_created_at");
                if (ts != null) app.setCreatedAt(ts.toLocalDateTime());
                list.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    /** 트랜잭션용: 외부에서 받은 Connection으로 실행. autoCommit/close는 호출자가 관리. */
    public static void updateStatus(int applicationId, String status, Connection conn) throws java.sql.SQLException {
        String sql = """
        UPDATE add_spot_applications
        SET add_status = ?
        WHERE add_application_id = ?
    """;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, applicationId);
            pstmt.executeUpdate();
        }
    }

    public static void updateStatus(int applicationId, String status) {
        String sql = """
        UPDATE add_spot_applications
        SET add_status = ?
        WHERE add_application_id = ?
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



    public static ArrayList<AddSpotApplication> getAddApplicationByUserId(String userId) {
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
                addSpotApplication.setApplicationId(rs.getInt("add_application_id"));
                addSpotApplication.setUserId(rs.getString("user_id"));
                addSpotApplication.setSpotName(rs.getString("spot_name"));
                addSpotApplication.setSpotDescription(rs.getString("spot_description"));
                addSpotApplication.setSpotCategory(rs.getString("spot_category"));
                Timestamp ts = rs.getTimestamp("add_spot_created_at");
                if (ts != null) {
                    addSpotApplication.setCreatedAt(ts.toLocalDateTime());
                }

                addSpotApplication.setSpotLatitude(rs.getDouble("spot_latitude"));
                addSpotApplication.setSpotLongitude(rs.getDouble("spot_longitude"));
                addSpotApplication.setStatus(rs.getString("add_status"));
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
