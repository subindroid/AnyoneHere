<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ page import="java.sql.*"%>
<%@ include file="../dbconn.jsp"%>
<%@ page import="java.time.LocalDate"%>
<html>
<head>
    <title>Title</title>
</head>
<body>
<!-- 작성한 폼을 우리쪽으로 발송하는 파일>
<%
    request.setCharacterEncoding("UTF-8");

    String userId = request.getParameter("userId");
    String content = request.getParameter("content");
    LocalDate createdAt = LocalDate.now();

    String userName = "";
    PreparedStatement psUser = null;
    ResultSet rsUser = null;

    try {
        // 사용자 이름 가져오기
        String sqlUser = "SELECT u_name FROM users WHERE u_id = ?";
        psUser = conn.prepareStatement(sqlUser);
        psUser.setString(1, userId);
        rsUser = psUser.executeQuery();
        if (rsUser.next()) {
            userName = rsUser.getString("u_name");
        }

        // 리뷰 INSERT
        String sql = "INSERT INTO review (r_userId, s_id, r_review, r_rating, r_wroteAt) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, userId);
        pstmt.setInt(2, storeId);
        pstmt.setString(3, content);
        pstmt.setDouble(4, Double.parseDouble(request.getParameter("rating")));
        pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

        pstmt.executeUpdate();
        pstmt.close();
    } catch (SQLException e) {
        out.println("리뷰 등록 실패: " + e.getMessage());
        e.printStackTrace();
    } finally {
        if (rsUser != null)
            rsUser.close();
        if (psUser != null)
            psUser.close();
        if (conn != null)
            conn.close();
    }

    response.sendRedirect("store.jsp?storeId=" + storeId);
%>
</body>
</html>