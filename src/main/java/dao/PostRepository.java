package dao;

import dto.Post;
import dto.PostImage;
import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostRepository {

    private static final int PAGE_SIZE = 10;

    // 게시글 목록 조회 (카테고리 + 키워드 검색)
    public static List<Post> getPosts(String category, int page, String keyword) {
        if (page < 1) page = 1;
        List<Post> list = new ArrayList<>();
        boolean filterCat = (category != null && !category.isEmpty() && !category.equals("ALL"));
        boolean filterKwd = (keyword  != null && !keyword.trim().isEmpty());

        String sql = "SELECT p.post_id, p.user_id, p.category, p.title, p.view_count, "
                + "p.created_at, COALESCE(pr.nickname, p.user_id) AS author_name, "
                + "COUNT(DISTINCT pl.like_id) AS like_count, "
                + "COUNT(DISTINCT pc.comment_id) AS comment_count "
                + "FROM posts p "
                + "LEFT JOIN profile pr ON p.user_id = pr.user_id "
                + "LEFT JOIN post_likes pl ON p.post_id = pl.post_id "
                + "LEFT JOIN post_comments pc ON p.post_id = pc.post_id AND pc.is_deleted = FALSE "
                + "WHERE p.is_deleted = FALSE "
                + (filterCat ? "AND p.category = ? " : "")
                + (filterKwd ? "AND (p.title LIKE ? OR p.content LIKE ?) " : "")
                + "GROUP BY p.post_id, p.user_id, p.category, p.title, p.view_count, p.created_at, author_name "
                + "ORDER BY p.created_at DESC "
                + "LIMIT ? OFFSET ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            if (filterCat) ps.setString(idx++, category);
            if (filterKwd) {
                String like = "%" + keyword.trim() + "%";
                ps.setString(idx++, like);
                ps.setString(idx++, like);
            }
            ps.setInt(idx++, PAGE_SIZE);
            ps.setInt(idx,   (page - 1) * PAGE_SIZE);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 검색 포함 전체 게시글 수
    public static int getTotalCount(String category, String keyword) {
        boolean filterCat = (category != null && !category.isEmpty() && !category.equals("ALL"));
        boolean filterKwd = (keyword  != null && !keyword.trim().isEmpty());

        String sql = "SELECT COUNT(*) FROM posts WHERE is_deleted = FALSE"
                + (filterCat ? " AND category = ?" : "")
                + (filterKwd ? " AND (title LIKE ? OR content LIKE ?)" : "");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            if (filterCat) ps.setString(idx++, category);
            if (filterKwd) {
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

    // 게시글 목록 조회 (전체 또는 카테고리 필터)
    public static List<Post> getPosts(String category, int page) {
        if (page < 1) page = 1;
        List<Post> list = new ArrayList<>();
        String categoryFilter = (category == null || category.isEmpty() || category.equals("ALL"))
                ? "" : "AND p.category = ? ";

        String sql = "SELECT p.post_id, p.user_id, p.category, p.title, p.view_count, "
                + "p.created_at, COALESCE(pr.nickname, p.user_id) AS author_name, "
                + "COUNT(DISTINCT pl.like_id) AS like_count, "
                + "COUNT(DISTINCT pc.comment_id) AS comment_count "
                + "FROM posts p "
                + "LEFT JOIN profile pr ON p.user_id = pr.user_id "
                + "LEFT JOIN post_likes pl ON p.post_id = pl.post_id "
                + "LEFT JOIN post_comments pc ON p.post_id = pc.post_id AND pc.is_deleted = FALSE "
                + "WHERE p.is_deleted = FALSE " + categoryFilter
                + "GROUP BY p.post_id, p.user_id, p.category, p.title, p.view_count, p.created_at, author_name "
                + "ORDER BY p.created_at DESC "
                + "LIMIT ? OFFSET ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int idx = 1;
            if (!categoryFilter.isEmpty()) ps.setString(idx++, category);
            ps.setInt(idx++, PAGE_SIZE);
            ps.setInt(idx, (page - 1) * PAGE_SIZE);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 전체 게시글 수 (페이지네이션용)
    public static int getTotalCount(String category) {
        boolean filterAll = (category == null || category.isEmpty() || category.equals("ALL"));
        String sql = "SELECT COUNT(*) FROM posts WHERE is_deleted = FALSE"
                + (filterAll ? "" : " AND category = ?");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (!filterAll) ps.setString(1, category);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 게시글 단건 조회 (이미지 포함)
    public static Post getPostById(int postId) {
        String sql = "SELECT p.*, COALESCE(pr.nickname, p.user_id) AS author_name, "
                + "COUNT(DISTINCT pl.like_id) AS like_count, "
                + "COUNT(DISTINCT pc.comment_id) AS comment_count "
                + "FROM posts p "
                + "LEFT JOIN profile pr ON p.user_id = pr.user_id "
                + "LEFT JOIN post_likes pl ON p.post_id = pl.post_id "
                + "LEFT JOIN post_comments pc ON p.post_id = pc.post_id AND pc.is_deleted = FALSE "
                + "WHERE p.post_id = ? AND p.is_deleted = FALSE "
                + "GROUP BY p.post_id, p.user_id, p.category, p.title, p.content, "
                + "p.view_count, p.created_at, p.updated_at, p.is_deleted, author_name";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Post post = mapRow(rs);
                    post.setContent(rs.getString("content"));
                    post.setDeleted(rs.getBoolean("is_deleted"));
                    post.setImages(PostImageRepository.getImagesByPostId(postId));
                    return post;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 게시글 등록. 생성된 post_id 반환 (-1이면 실패)
    public static int insert(Post post) {
        String sql = "INSERT INTO posts (user_id, category, title, content) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, post.getUserId());
            ps.setString(2, post.getCategory());
            ps.setString(3, post.getTitle());
            ps.setString(4, post.getContent());
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 게시글 수정 (제목, 내용, 카테고리만)
    public static void update(Post post) {
        String sql = "UPDATE posts SET category = ?, title = ?, content = ? WHERE post_id = ? AND user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, post.getCategory());
            ps.setString(2, post.getTitle());
            ps.setString(3, post.getContent());
            ps.setInt(4, post.getPostId());
            ps.setString(5, post.getUserId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 게시글 소프트 삭제 (userId로 본인 글만 삭제)
    public static void delete(int postId, String userId) {
        String sql = "UPDATE posts SET is_deleted = TRUE WHERE post_id = ? AND user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 조회수 증가
    public static void incrementViewCount(int postId) {
        String sql = "UPDATE posts SET view_count = view_count + 1 WHERE post_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Post mapRow(ResultSet rs) throws SQLException {
        Post post = new Post();
        post.setPostId(rs.getInt("post_id"));
        post.setUserId(rs.getString("user_id"));
        post.setCategory(rs.getString("category"));
        post.setTitle(rs.getString("title"));
        post.setViewCount(rs.getInt("view_count"));
        post.setAuthorName(rs.getString("author_name"));
        post.setLikeCount(rs.getInt("like_count"));
        post.setCommentCount(rs.getInt("comment_count"));
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) post.setCreatedAt(createdAt.toLocalDateTime());
        return post;
    }
}
