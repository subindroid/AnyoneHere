<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ page import="dao.UserRepository, dao.SpotRepository, dto.User, dto.Spot"%>
<%@ page errorPage="../exceptionPages/exceptionNoSpot.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="../resources/css/bootstrap.min.css" />
	<title>상품 상세 정보</title>
</head>
<body>
<div class="container py-5">
	<jsp:include page="../common/menu.jsp" />

	<div class="p-5 mb-4 bg-body-tertiary rounded-3">
		<div class="container-fluid py-5">
			<h1 class="display-5 fw-bold">상품정보</h1>
			<p class="col-md-8 fs-4">description</p>
		</div>
	</div>

	<%
		// 세션에서 사용자 ID 확인
		String userId = (String) session.getAttribute("userId");
		if (userId == null) {
			response.sendRedirect("../member/loginMember.jsp"); // 로그인 안된 경우
			return;
		}
		String spotIdString = request.getParameter("spotId");
		System.out.println(">>> spot.jsp spotIdString = " + spotIdString);

		int spotId = Integer.parseInt(spotIdString);
		System.out.println(">>> spot.jsp spotId(int) = " + spotId);

		SpotRepository repo = SpotRepository.getInstance();   // 인스턴스 메소드로 바꾼 상태라 가정
		Spot spot = repo.getSpotBySpotId(spotId);
		System.out.println(">>> spot.jsp spot = " + spot);

		if (spot == null) {
			response.sendRedirect("../exceptionPages/exceptionNoSpot.jsp?spotId=" + spotId);
			return;
		}
	%>
		<div class="row align-items-md-stretch">
		<div class="col-md-8">
		<img
		src="<%=request.getContextPath()%>/resources/images/<%=spot.getSpotImage()%>"
	style="width: 70%;" />
	<h3>
		<b><%=spot.getSpotName()%></b>
	</h3>
	<p><%=spot.getSpotDescription()%></p>

	<form action="../wishlist/processAddWishlist.jsp" method="post"
		  style="display: inline;">
		<input type="hidden" name="spotId"
			   value="<%=spot.getSpotId()%>">
		<button type="submit" class="btn btn-warning" role="button">찜하기
			&raquo;</button>
	</form>


</div>
</div>

</div>
</body>

<jsp:include page="../common/footer.jsp" />
</html>
