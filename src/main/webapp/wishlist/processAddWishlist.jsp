<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.oreilly.servlet.*"%>
<%@ page import="com.oreilly.servlet.multipart.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ page import="dao.SpotRepository" %>
<%@ page import="dto.Spot" %>
<%@ include file="../dbconn.jsp"%>
<%
request.setCharacterEncoding("UTF-8");

// 이미지 저장 경로
String realFolder = application.getRealPath("/resources/images");
int maxSize = 5 * 1024 * 1024; // 5MB
String encType = "utf-8";
%>

<%
// 폼 데이터 수집
String spotId = request.getParameter("spotId"); // 임시 판매자 ID, 로그인 연동 시 세션 등에서 가져오면 됨
String userId = (String) session.getAttribute("userId"); // 현재 로그인한 사용자

// DB INSERT
PreparedStatement pstmt = null;
String sql = "INSERT INTO wishlist (user_id, spot_id) VALUES (?, ?)";

try {
	pstmt = conn.prepareStatement(sql);
	pstmt.setString(1, userId);
	pstmt.setInt(2, Integer.parseInt(spotId));
	pstmt.executeUpdate();
	} catch (SQLException e) { 
	out.println("찜 등록 중 오류 발생: " + e.getMessage());
} finally {
	if (pstmt != null)
		pstmt.close();
	if (conn != null)
		conn.close();
}

// 등록 후 상품 목록 페이지로 이동
response.sendRedirect("wishlist.jsp");
%>
