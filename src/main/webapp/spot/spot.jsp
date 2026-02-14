<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ page import="dto.Spot, dao.ProductRepository"%>
<%@ page import="dto.Store, dao.StoreRepository"%>
<%@ page import="dao.UserRepository, dao.UserRepository, dto.User"%>
<%@ page import="dao.SpotRepository" %>
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
	<%@ include file="../menu.jsp"%>

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

		//productId 파라미터 받기
		String spotIdString = request.getParameter("spotId"); // String으로 받음
		int spotId = Integer.parseInt(spotIdString); // String을 int로 변환

		SpotRepository repo = SpotRepository.getInstance();
		Spot spot = repo.getSpotBySpotId(spotId);

		if (spot == null) {
			response.sendRedirect("exceptionNoProduct.jsp?spotId=" + spotId);
			return;
		}

		// 관련 정보 조회
		User user = UserRepository.getUserById(userId);

		<div class="row align-items-md-stretch">
		<div class="col-md-8">
		<img
		src="<%=request.getContextPath()%>/resources/images/<%=product.getFileName()%>"
	style="width: 70%;" />
	<h3>
		<b><%=spot.getSpotName()%></b>
	</h3>
	<p><%=spot.getDescription()%></p>

	<form action="../wishlist/processAddWishlist.jsp" method="post"
		  style="display: inline;">
		<input type="hidden" name="productId"
			   value="<%=product.getProductId()%>"> <input
			type="hidden" name="storeId" value="<%=product.getStoreId()%>">
		<button type="submit" class="btn btn-warning" role="button">찜하기
			&raquo;</button>
	</form>


</div>
</div>

</div>
</body>

<jsp:include page="../footer.jsp" />
</html>
