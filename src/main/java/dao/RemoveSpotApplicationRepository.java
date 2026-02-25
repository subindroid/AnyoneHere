package dao;
import dto.AddSpotApplication;
import dto.RemoveSpotApplication;
import util.DBUtil;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.Timestamp;
public class RemoveSpotApplicationRepository {
    public static RemoveSpotApplication getRemoveApplicationByRemoveId(int applicationId) {
        RemoveSpotApplication removeSpotApplication = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            String sql = "SELECT * FROM remove_spot_applications WHERE remove_application_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, applicationId);
            rs = ps.executeQuery();

            if (rs.next()) {
                removeSpotApplication = new RemoveSpotApplication();
                removeSpotApplication.setSpotId(rs.getInt("spot_id"));
                removeSpotApplication.setApplicationId(rs.getInt("remove_application_id"));
                removeSpotApplication.setRemoveReason(rs.getString("remove_reason"));
                removeSpotApplication.setStatus(rs.getString("remove_status"));
                Timestamp ts = rs.getTimestamp("remove_spot_created_at");
                if (ts != null) {
                    removeSpotApplication.setCreatedAt(ts.toLocalDateTime());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(rs, ps, conn);
        }

        return removeSpotApplication;
    }

    public static void insert(RemoveSpotApplication app) {
        String sql = """
        INSERT INTO remove_spot_applications
        (user_id, spot_id, remove_reason, remove_status, remove_spot_created_at)
        VALUES (?, ?, ?, ?, NOW())
    """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, app.getUserId());
            pstmt.setInt(2, app.getSpotId());
            pstmt.setString(3, app.getRemoveReason());
            pstmt.setString(4, app.getStatus());

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateStatus(int applicationId, String status) {
        String sql = """
        UPDATE rempve_spot_applications
        SET remove_status = ?
        WHERE remove_application_id = ?
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

}
