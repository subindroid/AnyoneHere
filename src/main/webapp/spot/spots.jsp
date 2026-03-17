<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="dao.SpotRepository" %>
<%@ page import="dto.Spot" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>AnyoneHere - 장소 목록</title>
    <link rel="stylesheet" href="../resources/css/bootstrap.min.css"/>
</head>
<body>
<div class="container py-4">
    <jsp:include page="../common/menu.jsp"/>

    <%
        String userId   = (String) session.getAttribute("userId");
        String category = request.getParameter("category");
        String keyword  = request.getParameter("keyword");
        String pageStr  = request.getParameter("page");

        if (category == null) category = "ALL";
        if (keyword  == null) keyword  = "";
        // category 화이트리스트 검증
        java.util.Set<String> validCategories = new java.util.HashSet<>(
            java.util.Arrays.asList("ALL", "1", "2", "3", "4", "5"));
        if (!validCategories.contains(category)) category = "ALL";

        int currentPage = 1;
        try { currentPage = Integer.parseInt(pageStr); } catch (Exception ignored) {}

        String filterCat = "ALL".equals(category) ? null : category;
        String filterKwd = keyword.isEmpty() ? null : keyword;

        ArrayList<Spot> spots = SpotRepository.getSpotsByFilter(filterCat, filterKwd, currentPage);
        int total      = SpotRepository.getSpotCount(filterCat, filterKwd);
        int totalPages = (int) Math.ceil((double) total / 12);
        if (totalPages < 1) totalPages = 1;

        String csrfToken = util.CsrfUtil.getOrCreateToken(session);
    %>

    <div class="p-4 mb-4 bg-body-tertiary rounded-3">
        <h1 class="display-5 fw-bold">드라이브 스팟</h1>
        <p class="fs-5">드라이브하기 좋은 장소를 찾아보세요.</p>
        <a href="../spotApplication/spotAddApplication.jsp" class="btn btn-primary">장소 등록 요청</a>
    </div>

    <!-- 카테고리 탭 -->
    <ul class="nav nav-tabs mb-3">
        <li class="nav-item"><a class="nav-link <%= "ALL".equals(category) ? "active" : "" %>" href="spots.jsp?category=ALL&keyword=<%= keyword %>">전체</a></li>
        <li class="nav-item"><a class="nav-link <%= "1".equals(category) ? "active" : "" %>" href="spots.jsp?category=1&keyword=<%= keyword %>">카페/음식점</a></li>
        <li class="nav-item"><a class="nav-link <%= "2".equals(category) ? "active" : "" %>" href="spots.jsp?category=2&keyword=<%= keyword %>">공원/자연</a></li>
        <li class="nav-item"><a class="nav-link <%= "3".equals(category) ? "active" : "" %>" href="spots.jsp?category=3&keyword=<%= keyword %>">쇼핑</a></li>
        <li class="nav-item"><a class="nav-link <%= "4".equals(category) ? "active" : "" %>" href="spots.jsp?category=4&keyword=<%= keyword %>">관광/랜드마크</a></li>
        <li class="nav-item"><a class="nav-link <%= "5".equals(category) ? "active" : "" %>" href="spots.jsp?category=5&keyword=<%= keyword %>">문화/공연</a></li>
    </ul>

    <!-- 검색 폼 -->
    <form method="get" action="spots.jsp" class="d-flex gap-2 mb-4">
        <input type="hidden" name="category" value="<%= category %>">
        <input type="text" name="keyword" class="form-control" placeholder="장소명 또는 설명으로 검색" value="<%= util.HtmlUtil.escape(keyword) %>">
        <button type="submit" class="btn btn-outline-secondary">검색</button>
        <% if (!keyword.isEmpty()) { %>
        <a href="spots.jsp?category=<%= category %>" class="btn btn-outline-danger">초기화</a>
        <% } %>
    </form>

    <p class="text-muted small mb-3">검색 결과 <%= total %>개</p>

    <!-- 스팟 목록 -->
    <% if (spots.isEmpty()) { %>
    <div class="alert alert-secondary">조건에 맞는 장소가 없습니다.</div>
    <% } else { %>
    <div class="row g-4">
        <% for (Spot spot : spots) {
            String desc = spot.getSpotDescription();
            String shortDesc = (desc != null && desc.length() > 40)
                    ? desc.substring(0, 40) + "..." : (desc != null ? desc : "");
        %>
        <div class="col-md-4">
            <div class="card h-100">
                <% if (spot.getSpotImage() != null && !spot.getSpotImage().isEmpty()) { %>
                <img src="<%= request.getContextPath() %>/resources/images/<%= spot.getSpotImage() %>"
                     class="card-img-top" style="height:200px; object-fit:cover;" alt="스팟 이미지">
                <% } %>
                <div class="card-body d-flex flex-column">
                    <h5 class="card-title fw-bold"><%= util.HtmlUtil.escape(spot.getSpotName()) %></h5>
                    <p class="card-text text-muted small flex-grow-1"><%= util.HtmlUtil.escape(shortDesc) %></p>
                    <div class="d-flex gap-2 mt-2">
                        <a href="spot.jsp?spotId=<%= spot.getSpotId() %>" class="btn btn-secondary btn-sm">상세 보기</a>
                        <% if (userId != null) { %>
                        <form action="<%= request.getContextPath() %>/processAddWishlist" method="post" style="display:inline;">
                            <input type="hidden" name="spotId" value="<%= spot.getSpotId() %>">
                            <input type="hidden" name="_csrf" value="<%= csrfToken %>">
                            <button type="submit" class="btn btn-warning btn-sm">찜하기</button>
                        </form>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>
        <% } %>
    </div>

    <!-- 페이지네이션 -->
    <% if (totalPages > 1) { %>
    <nav class="mt-4">
        <ul class="pagination justify-content-center">
            <% if (currentPage > 1) { %>
            <li class="page-item">
                <a class="page-link" href="spots.jsp?category=<%= category %>&keyword=<%= keyword %>&page=<%= currentPage - 1 %>">이전</a>
            </li>
            <% } %>
            <% for (int i = 1; i <= totalPages; i++) { %>
            <li class="page-item <%= i == currentPage ? "active" : "" %>">
                <a class="page-link" href="spots.jsp?category=<%= category %>&keyword=<%= keyword %>&page=<%= i %>"><%= i %></a>
            </li>
            <% } %>
            <% if (currentPage < totalPages) { %>
            <li class="page-item">
                <a class="page-link" href="spots.jsp?category=<%= category %>&keyword=<%= keyword %>&page=<%= currentPage + 1 %>">다음</a>
            </li>
            <% } %>
        </ul>
    </nav>
    <% } %>
    <% } %>

    <jsp:include page="../common/footer.jsp"/>
</div>
</body>
</html>
