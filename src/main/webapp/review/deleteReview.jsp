<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ include file="../dbconn.jsp" %>
<html>
<head>
    <title>Delete Review</title>
</head>
<body>
<%
    String sessionId = (String) session.getAttribute("userId");
    if (sessionId == null) {
        response.sendRedirect("/member/loginMember.jsp");
        return;
    }

    String deleteId = request.getParameter("id");  // 삭제할 상품 ID
    if (deleteId != null) {
        String deleteSql = "DELETE FROM review WHERE review_id = ? AND user_id = ?";
        PreparedStatement deletePstmt = conn.prepareStatement(deleteSql);
        deletePstmt.setInt(1, Integer.parseInt(deleteId));
        deletePstmt.setString(2, sessionId);
        deletePstmt.executeUpdate();
        deletePstmt.close();
    }

    PreparedStatement pstmt = null;
    ResultSet rs = null;
    String sql = "SELECT * FROM reviews WHERE user_id = ?";
    pstmt = conn.prepareStatement(sql);
    pstmt.setString(1, sessionId);
    rs = pstmt.executeQuery();


    if (rs != null) rs.close();
    if (pstmt != null) pstmt.close();
    if (conn != null) conn.close();
    response.sendRedirect("reviews.jsp");


%>
</body>
</html>
