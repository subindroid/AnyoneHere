<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="dao.UserRepository, dao.SpotRepository, dto.User, dto.Spot" %>
<%@ page errorPage="../exceptionPages/exceptionNoSpot.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="../resources/css/bootstrap.min.css"/>
    <title>상품 상세 정보</title>
</head>
<body>
<div class="container py-5">
    <jsp:include page="../common/menu.jsp"/>

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
                    style="width: 70%;"/>
            <h3>
                <b><%=spot.getSpotName()%>
                </b>
            </h3>
            <p><%=spot.getSpotDescription()%>
            </p>
            <p><a href="../review/reviews.jsp?spotId=<%=spot.getSpotId()%>">
                <button type="submit" class="btn btn-primary" role="button">
                    <svg
                            xmlns="http://www.w3.org/2000/svg" width="16" height="16"
                            fill="currentColor" class="bi bi-pencil-square"
                            viewBox="0 0 16 16">
                        <path
                                d="M15.502 1.94a.5.5 0 0 1 0 .706L14.459 3.69l-2-2L13.502.646a.5.5 0 0 1 .707 0l1.293 1.293zm-1.75 2.456-2-2L4.939 9.21a.5.5 0 0 0-.121.196l-.805 2.414a.25.25 0 0 0 .316.316l2.414-.805a.5.5 0 0 0 .196-.12l6.813-6.814z"/>
                        <path fill-rule="evenodd"
                              d="M1 13.5A1.5 1.5 0 0 0 2.5 15h11a1.5 1.5 0 0 0 1.5-1.5v-6a.5.5 0 0 0-1 0v6a.5.5 0 0 1-.5.5h-11a.5.5 0 0 1-.5-.5v-11a.5.5 0 0 1 .5-.5H9a.5.5 0 0 0 0-1H2.5A1.5 1.5 0 0 0 1 2.5z"/>
                    </svg>리뷰 보기
                    &raquo;
                </button>
                <form action="../wishlist/processAddWishlist.jsp" method="post"
                      style="display: inline;">
                    <input type="hidden" name="spotId"
                           value="<%=spot.getSpotId()%>">
                    <button type="submit" class="btn btn-warning" role="button">찜하기
                        &raquo;
                    </button>
                </form>


        </div>
    </div>

</div>
</body>

<jsp:include page="../common/footer.jsp"/>
</html>
