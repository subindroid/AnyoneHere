<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>AnyoneHere - 신청 완료</title>
    <link rel="stylesheet" href="../resources/css/bootstrap.min.css"/>
</head>
<body>
<div class="container py-4">
    <%@ include file="../common/menu.jsp" %>

    <div class="p-5 mb-4 bg-body-tertiary rounded-3">
        <div class="container-fluid py-5">
            <h1 class="display-5 fw-bold">신청 완료</h1>
            <p class="col-md-8 fs-4">신청이 정상적으로 접수되었습니다. 관리자 검토 후 처리됩니다.</p>
        </div>
    </div>

    <div class="container">
        <a href="${pageContext.request.contextPath}/spot/spots.jsp" class="btn btn-secondary">&laquo; 장소 목록으로</a>
    </div>

    <%@ include file="../common/footer.jsp" %>
</div>
</body>
</html>
