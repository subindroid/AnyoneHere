<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>
<%@ page import="dao.SpotRepository"%>
<%@ page import="dto.Spot"%>

<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="../../../resources/css/bootstrap.min.css" />
<meta charset="UTF-8">
<title>Spot list</title>
</head>
<body>
<div class="container py-4">
	<jsp:include page="../common/menu.jsp" />
	<div class="p-5 mb-4 bg-body-tertiary rounded-3">
		<div class="container-fluid py-5">
			<h1 class="display-5 fw-bold">장소 목록</h1>
			<p class="col-md-8 fs-4">Spot List</p>
		</div>
        <p><a href="../spotApplication/spotAddApplication.jsp">
        <button type="submit" class="btn btn-primary" role="button">장소 등록 요청
        </button></a>
	</div>
	<%@ include file="../dbconn.jsp" %>
	<div class="row align-items-md-stretch text-center">
		<%
			String userId = (String) session.getAttribute("userId");
			Spot spot = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String sql="SELECT * FROM spots";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while(rs.next()) {

		%>
		<div class="col-md-4">
			<div class="h-100 p-2">
				<img src="<%=request.getContextPath()%>/resources/images/<%=rs.getString("spot_image")%>"
					 style="width:250px; height:350px" />
				<h5><b><%=rs.getString("spot_name") %></b></h5>
				<p> <%=rs.getString("spot_description").substring(0,10) %>...
        		<p> <a href="<%=request.getContextPath()%>/spot/spot.jsp?spotId=<%=rs.getString("spot_id") %>" class="btn btn-secondary" role="button">상세 정보 &raquo;></a>
			  	<form action="../wishlist/processAddWishlist.jsp" method="post" style="display:inline;">
  					<input type="hidden" name="spotId" value="<%=rs.getString("spot_id") %>">
  					<input type="hidden" name="spotName" value="<%=rs.getString("spot_name") %>">
				</form>
			</div>
		</div>
		<%
			}
			if (rs!= null)
				rs.close();
			if (pstmt != null)
				pstmt.close();
			if (conn !=null)
				conn.close();
		%>
	</div>
	<jsp:include page="../common/footer.jsp" />
</div>
<script>
    function checkMyLogin(userId) {
    	if (userId == null) {
			alert("로그인 후 이용할 수 있습니다!");
			location.href = "../member/loginMember.jsp";
			return false;
		}
        return true;
    }
</script>
</body>
</html>