package dao;

import dto.PostReport;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostReportRepository {

    /** PENDING 상태 신고 목록 조회 (관리자용) */
    public static List<PostReport> getAllPending() {
        List<PostReport> list = new ArrayList<>();
        String sql = "SELECT pr.*, p.title AS post_title, "
                + "COALESCE(pf.nickname, pr.reporter_id) AS reporter_name "
                + "FROM post_reports pr "
                + "JOIN posts p ON pr.post_id = p.post_id "
                + "LEFT JOIN profile pf ON pr.reporter_id = pf.user_id "
                + "WHERE pr.report_status = 'PENDING' "
                + "ORDER BY pr.created_at DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PostReport r = new PostReport();
                r.setReportId(rs.getInt("report_id"));
                r.setPostId(rs.getInt("post_id"));
                r.setReporterId(rs.getString("reporter_id"));
                r.setReason(rs.getString("reason"));
                r.setStatus(rs.getString("report_status"));
                r.setPostTitle(rs.getString("post_title"));
                r.setReporterName(rs.getString("reporter_name"));
                Timestamp ts = rs.getTimestamp("created_at");
                if (ts != null) r.setCreatedAt(ts.toLocalDateTime());
                list.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /** 신고 상태 변경 (REVIEWED / DISMISSED) */
    public static void updateStatus(int reportId, String status) {
        String sql = "UPDATE post_reports SET report_status = ? WHERE report_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, reportId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 이미 신고한 게시글인지 확인 */
    public static boolean hasReported(int postId, String reporterId) {
        String sql = "SELECT COUNT(*) FROM post_reports WHERE post_id = ? AND reporter_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.setString(2, reporterId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /** 신고 등록. true = 성공, false = 이미 신고했거나 실패 */
    public static boolean insert(PostReport report) {
        if (hasReported(report.getPostId(), report.getReporterId())) return false;

        String sql = "INSERT INTO post_reports (post_id, reporter_id, reason) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, report.getPostId());
            ps.setString(2, report.getReporterId());
            ps.setString(3, report.getReason());
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
