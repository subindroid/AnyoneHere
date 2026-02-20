<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>잘못된 요청</title>
    <link rel="stylesheet" href="../resources/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="../common/menu.jsp" />
<div class="jumbotron">
    <div class="container">
        <h1 class="display-3 text-danger">올바르지 않은 접근입니다.</h1>
    </div>
</div>
<div class="container">
    <p class="lead">입력하신 정보가 형식에 맞지 않거나 누락되었습니다.</p>
    <p>확인 후 다시 시도해 주세요.</p>
    <hr>
    <a href="javascript:history.back()" class="btn btn-secondary">&laquo; 이전 페이지로</a>
    <a href="../index.jsp" class="btn btn-primary">홈으로 이동</a>
</div>
<jsp:include page="../common/footer.jsp" />
</body>
</html>