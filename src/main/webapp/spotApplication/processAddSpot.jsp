<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.oreilly.servlet.*"%>
<%@ page import="com.oreilly.servlet.multipart.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.*"%>
<%@ include file="../dbconn.jsp"%>
<%@ page import="dto.Store" %>
<%@ page import="dao.StoreRepository" %>

<%
request.setCharacterEncoding("UTF-8");

String userId = (String) session.getAttribute("userId");
Store store = StoreRepository.getStoreByUserId(userId);
int s_id = store != null ? store.getStoreId() : null;

if (userId == null) {
    out.println("<script>alert('로그인 해야 상품을 등록할 수 있습니다!'); location.href='addStore.jsp';</script>");
    return;
}


// 이미지 저장 경로
String realFolder = application.getRealPath("/resources/images");
int maxSize = 5 * 1024 * 1024; // 5MB
String encType = "utf-8";
%>

<%
MultipartRequest multi = new MultipartRequest(request, realFolder, maxSize, encType, new DefaultFileRenamePolicy());

// 폼 데이터 수집
String p_name = multi.getParameter("productName");
String p_unitPrice = multi.getParameter("unitPrice");
String p_description = multi.getParameter("product_description");
String p_brand = multi.getParameter("brand");
String p_category = multi.getParameter("category");
String p_condition = multi.getParameter("condition");

// 파일 처리
Enumeration files = multi.getFileNames();
String fname = (String) files.nextElement();
String p_filename = multi.getFilesystemName(fname);

// 숫자 파싱
int price = (p_unitPrice != null && !p_unitPrice.isEmpty()) ? Integer.parseInt(p_unitPrice) : 0;

// DB INSERT
PreparedStatement pstmt = null;
String sql = "INSERT INTO product (s_id, seller_id, p_name, p_unitPrice, p_description, p_brand, p_category, p_condition, p_filename) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

try {
	pstmt = conn.prepareStatement(sql);
	pstmt.setInt(1, s_id);
	pstmt.setString(2, userId);
	pstmt.setString(3, p_name);
	pstmt.setInt(4, price);
	pstmt.setString(5, p_description);
	pstmt.setString(6, p_brand);
	pstmt.setString(7, p_category);
	pstmt.setString(8, p_condition);
	pstmt.setString(9, p_filename);
	pstmt.executeUpdate();
} catch (SQLException e) {
	out.println("❌ 상품 등록 중 오류 발생: " + e.getMessage());
	return;
} finally {
	if (pstmt != null) pstmt.close();
	if (conn != null) conn.close();
}

// 등록 후 상품 목록 페이지로 이동
response.sendRedirect("userStore.jsp");
%>
