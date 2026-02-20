<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="dto.Review" %>
<%@ page import="dao.ReviewRepository" %>
<%@ page import="dto.Spot" %>
<%@ page import="dao.SpotRepository" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>AnyoneHere - Reviews</title>
    <link rel="stylesheet" href="../../../resources/css/bootstrap.min.css"/>
    <%
        String spotIdStr = request.getParameter("spotId");
        if (spotIdStr != null && !spotIdStr.isEmpty()) {
            int spotId = Integer.parseInt(spotIdStr);

            // static 메서드 호출
            ArrayList<Review> reviewList = ReviewRepository.getReviewsBySpotId(spotId);
            double avgRating = new ReviewRepository().getAverageRatingBySpotId(spotId);

            request.setAttribute("reviewList", reviewList);
            request.setAttribute("avgRating", avgRating);
        }
    %>


</head>
<body>
<fmt:setLocale value="${param.language}"/>
<fmt:bundle basename="bundle.message">
    <div class="container py-4">
        <%@ include file="../common/menu.jsp" %>

        <div class="p-5 mb-4 bg-body-tertiary rounded-3">
            <div class="container-fluid py-5">
                <h1 class="display-5 fw-bold">
                    <fmt:message key="reviewListTitle"/>
                </h1>
                <p class="col-md-8 fs-4">
                    <fmt:message key="reviewListSubtitle"/>
                </p>
            </div>
        </div>

        <!-- 리뷰 평균 별점 -->
        <c:if test="${not empty avgRating}">
            <div class="mb-3">
                <strong><fmt:message key="averageRating"/>:</strong> <span
                    class="text-warning">${avgRating} / 5.0</span>
            </div>
        </c:if>

        <!-- 리뷰 리스트 -->
        <c:if test="${empty reviewList}">
            <div class="alert alert-secondary">
                <fmt:message key="noReviews"/>
            </div>
        </c:if>

        <c:forEach var="review" items="${reviewList}">
            <div class="card mb-3">
                <div class="card-body">
                    <h5 class="card-title">
                        <c:set var="ratingInt" value="${review.reviewRating - (review.reviewRating % 1)}"/>
                        <c:forEach begin="1" end="${ratingInt}">
                            ⭐
                        </c:forEach>
                    </h5>
                    <p class="card-text">${review.reviewText}</p>
                    <p class="card-text"><small class="text-muted">${review.reviewCreatedAt}</small></p>
                    <p class="card-text"><small class="text-muted">작성자: ${review.userId}</small></p>
                </div>
            </div>
        </c:forEach>


        <!-- 리뷰 작성 버튼 -->
        <div class="text-end">
            <a href="addReview.jsp?spotId=${param.spotId}"
               class="btn btn-primary"> <fmt:message key="writeReview"/>
            </a>
        </div>

        <%@ include file="../common/footer.jsp" %>
    </div>
</fmt:bundle>
</body>
</html>
