<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.ArrayList"%>
<%@ page import="java.sql.*"%>
<%@ page import="dao.SpotRepository"%>
<%@ page import="dto.Spot"%>
<%@ page isErrorPage="true"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="../../../resources/css/bootstrap.min.css" />

<meta charset="UTF-8">
<title>Wishlist</title>
</head>
<body>
	<%
	String userId = (String) session.getAttribute("userId");
	if (userId == null) {
	%>
	<script>
		alert("로그인을 해야 찜 목록을 볼 수 있습니다!");
		location.href = "../member/loginMember.jsp";
	</script>
	<% 
		return;
	}

	%>
	
<div class="container py-4">
	<%@ include file="../common/menu.jsp" %>
	
	<div class="p-5 mb-4 bg-body-tertiary rounded-3">
		<div class="container-fluid py-5">
			<h1 class="alert alert-warning">찜 목록
			<p class="col-md-8 fs-4">wishList</p>
			</h1>			
		</div>
	</div>
	<%@ include file="../dbconn.jsp" %>
	<div class="row align-items-md-stretch text-center">
	
<%

PreparedStatement pstmt = null;
ResultSet rs = null;

String sql = "SELECT w.*, s.spot_name, s.spot_image " +
             "FROM wishlist w " +
             "JOIN spots s ON w.spot_id = s.spot_id " +
             "WHERE w.user_id = ?";

pstmt = conn.prepareStatement(sql);
pstmt.setString(1, userId);
rs = pstmt.executeQuery();
%>

<% while (rs.next()) { %>
  <div class="col-md-4">
	<div class="h-100 p-2">
		<img src="<%=request.getContextPath()%>/resources/images/<%=rs.getString("spot_image")%>" width="100">
		<p><%= rs.getString("spot_name") %>
    <form action="removeWishlist.jsp" method="post">
      <input type="hidden" name="spotId" value="<%=rs.getInt("spot_id")%>">
      <button type="submit" class="btn btn-danger">찜 삭제</button>
    </form>
  </div>
  </div>
<% } 

if (rs != null) try { rs.close(); } catch (SQLException e) {}
if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
if (conn != null) try { conn.close(); } catch (SQLException e) {}
%>

</div>
	<div class="row align-items-md-stretch text-center">
		<p> <a href="../spot/spots.jsp?" class="btn btn-secondary" role="button">장소 보러가기 &raquo;></a>
	</div>
	<%@ include file="../common/footer.jsp" %>
</div>
</body>
</html>