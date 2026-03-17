package dao;

import dto.AddSpotApplication;
import dto.Spot;
import java.util.ArrayList;
import java.sql.*;
import util.DBUtil;

public class SpotRepository {

    public static Spot getSpotBySpotId(int spotId) {
        String sql = "SELECT * FROM spots WHERE spot_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, spotId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Spot spot = new Spot();
                    spot.setSpotId(rs.getInt("spot_id"));
                    spot.setSpotName(rs.getString("spot_name"));
                    spot.setSpotDescription(rs.getString("spot_description"));
                    spot.setSpotLatitude(rs.getDouble("latitude"));
                    spot.setSpotLongitude(rs.getDouble("longitude"));
                    spot.setRadiusM(rs.getDouble("radius_m"));
                    spot.setSpotCategory(rs.getString("spot_category"));
                    spot.setSpotImage(rs.getString("spot_image"));
                    return spot;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private static final int SPOT_PAGE_SIZE = 12;

    /** 카테고리 + 키워드 + 페이지 지원 */
    public static ArrayList<Spot> getSpotsByFilter(String category, String keyword, int page) {
        if (page < 1) page = 1;
        ArrayList<Spot> spots = new ArrayList<>();
        boolean hasCategory = (category != null && !category.isEmpty());
        boolean hasKeyword  = (keyword  != null && !keyword.trim().isEmpty());

        StringBuilder sql = new StringBuilder("SELECT * FROM spots WHERE 1=1");
        if (hasCategory) sql.append(" AND spot_category = ?");
        if (hasKeyword)  sql.append(" AND (spot_name LIKE ? OR spot_description LIKE ?)");
        sql.append(" ORDER BY spot_id DESC LIMIT ? OFFSET ?");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (hasCategory) ps.setString(idx++, category);
            if (hasKeyword) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }
            ps.setInt(idx++, SPOT_PAGE_SIZE);
            ps.setInt(idx,   (page - 1) * SPOT_PAGE_SIZE);

            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spots;
    }

    /** 페이지네이션용 전체 스팟 수 */
    public static int getSpotCount(String category, String keyword) {
        boolean hasCategory = (category != null && !category.isEmpty());
        boolean hasKeyword  = (keyword  != null && !keyword.trim().isEmpty());

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM spots WHERE 1=1");
        if (hasCategory) sql.append(" AND spot_category = ?");
        if (hasKeyword)  sql.append(" AND (spot_name LIKE ? OR spot_description LIKE ?)");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (hasCategory) ps.setString(idx++, category);
            if (hasKeyword) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx,   like);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /** 카테고리 필터 + 키워드 검색 지원. null/빈값이면 전체 조회 (페이지 없는 버전 - 하위 호환) */
    public static ArrayList<Spot> getSpotsByFilter(String category, String keyword) {
        ArrayList<Spot> spots = new ArrayList<>();
        boolean hasCategory = (category != null && !category.isEmpty() && !"ALL".equals(category));
        boolean hasKeyword  = (keyword  != null && !keyword.trim().isEmpty());

        StringBuilder sql = new StringBuilder("SELECT * FROM spots WHERE 1=1");
        if (hasCategory) sql.append(" AND spot_category = ?");
        if (hasKeyword)  sql.append(" AND (spot_name LIKE ? OR spot_description LIKE ?)");
        sql.append(" ORDER BY spot_id DESC");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (hasCategory) ps.setString(idx++, category);
            if (hasKeyword) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx,   like);
            }

            try (ResultSet rs = ps.executeQuery()) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spots;
    }

    public static ArrayList<Spot> getAllSpots() {
        ArrayList<Spot> spots = new ArrayList<>();
        String sql = "SELECT * FROM spots";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

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
        }

        return spots;
    }

    /** 트랜잭션용: 외부에서 받은 Connection으로 실행. autoCommit/close는 호출자가 관리. */
    public static void insertSpot(AddSpotApplication app, Connection conn) throws SQLException {
        int categoryId;
        try {
            categoryId = Integer.parseInt(app.getSpotCategory());
        } catch (Exception e) {
            categoryId = 1;
        }

        String sql = """
            INSERT INTO spots
            (spot_name, latitude, longitude, radius_m, spot_description, spot_image, spot_category)
            VALUES (?, ?, ?, 100.0, ?, ?, ?)
        """;

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, app.getSpotName());
            pstmt.setDouble(2, app.getSpotLatitude());
            pstmt.setDouble(3, app.getSpotLongitude());
            pstmt.setString(4, app.getSpotDescription());
            pstmt.setString(5, app.getSpotImage() != null ? app.getSpotImage() : "");
            pstmt.setInt(6, categoryId);
            pstmt.executeUpdate();
        }
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

    /** 트랜잭션용: 외부에서 받은 Connection으로 실행. autoCommit/close는 호출자가 관리. */
    public static void deleteSpot(int spotId, Connection conn) throws SQLException {
        String sql = "DELETE FROM spots WHERE spot_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, spotId);
            pstmt.executeUpdate();
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
