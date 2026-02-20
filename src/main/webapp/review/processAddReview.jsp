<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page import="java.sql.*"%>
<%@ include file="../dbconn.jsp"%>
<%@ page import="java.time.LocalDate"%>

<%
request.setCharacterEncoding("UTF-8");

String userId = request.getParameter("userId");
int spotId = Integer.parseInt(request.getParameter("spotId"));
int rating = Integer.parseInt(request.getParameter("rating")); // ⭐️ 별점 받아오기
String content = request.getParameter("content");
LocalDate cretedAt = LocalDate.now();

String userName = "";
PreparedStatement psUser = null;
ResultSet rsUser = null;

try {
	// 사용자 이름 가져오기
	String sqlUser = "SELECT user_name FROM users WHERE user_id = ?";
	psUser = conn.prepareStatement(sqlUser);
	psUser.setString(1, userId);
	rsUser = psUser.executeQuery();
	if (rsUser.next()) {
		userName = rsUser.getString("user_name");
	}

	// 리뷰 INSERT
	String sql = "INSERT INTO reviews (user_id, spot_id, review_text, review_rating, review_created_at) VALUES (?, ?, ?, ?, ?)";
	PreparedStatement pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, userId);
	pstmt.setInt(2, spotId);
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

response.sendRedirect("../spot/spot.jsp?spotId=" + spotId);
%>
