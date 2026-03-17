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
        String sql = "SELECT * FROM remove_spot_applications WHERE remove_application_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, applicationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    RemoveSpotApplication app = new RemoveSpotApplication();
                    app.setSpotId(rs.getInt("spot_id"));
                    app.setApplicationId(rs.getInt("remove_application_id"));
                    app.setRemoveReason(rs.getString("remove_reason"));
                    app.setStatus(rs.getString("remove_status"));
                    Timestamp ts = rs.getTimestamp("remove_spot_created_at");
                    if (ts != null) app.setCreatedAt(ts.toLocalDateTime());
                    return app;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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


    /** 트랜잭션용: 외부에서 받은 Connection으로 실행. autoCommit/close는 호출자가 관리. */
    public static void updateStatus(int applicationId, String status, Connection conn) throws java.sql.SQLException {
        String sql = "UPDATE remove_spot_applications SET remove_status = ? WHERE remove_application_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, applicationId);
            pstmt.executeUpdate();
        }
    }

    public static void updateStatus(int applicationId, String status) {
        updateStatus(applicationId, status, (String) null);
    }

    /** 거절 사유 포함 상태 변경 */
    public static void updateStatus(int applicationId, String status, String rejectReason) {
        String sql = "UPDATE remove_spot_applications SET remove_status = ?, reject_reason = ? WHERE remove_application_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setString(2, rejectReason);
            pstmt.setInt(3, applicationId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<RemoveSpotApplication> getAllPending() {
        ArrayList<RemoveSpotApplication> list = new ArrayList<>();
        String sql = """
            SELECT r.*, s.spot_name
            FROM remove_spot_applications r
            JOIN spots s ON r.spot_id = s.spot_id
            WHERE r.remove_status = 'PENDING'
            ORDER BY r.remove_spot_created_at DESC
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                RemoveSpotApplication app = new RemoveSpotApplication();
                app.setApplicationId(rs.getInt("remove_application_id"));
                app.setUserId(rs.getString("user_id"));
                app.setSpotId(rs.getInt("spot_id"));
                app.setRemoveReason(rs.getString("remove_reason"));
                app.setStatus(rs.getString("remove_status"));
                app.setSpotName(rs.getString("spot_name"));
                Timestamp ts = rs.getTimestamp("remove_spot_created_at");
                if (ts != null) app.setCreatedAt(ts.toLocalDateTime());
                list.add(app);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<RemoveSpotApplication> getRemoveApplicationByUserId(String userId) {
        ArrayList<RemoveSpotApplication> list = new ArrayList<>();
        String sql = """
            SELECT r.*, s.spot_name
            FROM remove_spot_applications r
            JOIN spots s ON r.spot_id = s.spot_id
            WHERE r.user_id = ?
            ORDER BY r.remove_spot_created_at DESC
        """;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RemoveSpotApplication app = new RemoveSpotApplication();
                    app.setApplicationId(rs.getInt("remove_application_id"));
                    app.setUserId(rs.getString("user_id"));
                    app.setSpotId(rs.getInt("spot_id"));
                    app.setRemoveReason(rs.getString("remove_reason"));
                    app.setStatus(rs.getString("remove_status"));
                    app.setSpotName(rs.getString("spot_name"));
                    app.setRejectReason(rs.getString("reject_reason"));
                    Timestamp ts = rs.getTimestamp("remove_spot_created_at");
                    if (ts != null) app.setCreatedAt(ts.toLocalDateTime());
                    list.add(app);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
