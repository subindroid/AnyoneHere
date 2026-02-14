<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ include file="../dbconn.jsp" %>
<%@ page import="java.sql.*" %>
<%
	String spotId = request.getParameter("spotId");
	String userId = (String) session.getAttribute("userId"); // 로그인한 사용자

	PreparedStatement pstmt = null;

	try {
		String sql = "DELETE FROM wishlist WHERE user_id = ? AND spot_id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, userId);
		pstmt.setInt(2, Integer.parseInt(spotId));
		int result = pstmt.executeUpdate();
		
		if (result == 0) {
			out.println("해당 상품은 찜 목록에 존재하지 않습니다.");
		}
	} catch (Exception e) {
		out.println("삭제 중 오류 발생: " + e.getMessage());
	} finally {
		if (pstmt != null) pstmt.close();
		if (conn != null) conn.close();
	}
	
	response.sendRedirect("wishlist.jsp");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Remove Wishlist</title>
</head>
<body>

</body>
</html>