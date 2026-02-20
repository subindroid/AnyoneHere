<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<html>
<head>
    <title>데이터베이스 오류</title>
    <link rel="stylesheet" href="../resources/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="../common/menu.jsp" />
<div class="jumbotron">
    <div class="container">
        <h1 class="display-3 text-warning">서비스 이용에 불편을 드려 죄송합니다.</h1>
    </div>
</div>
<div class="container text-center">
    <div class="alert alert-light">
        <p>현재 데이터베이스 서버와의 통신이 원활하지 않습니다.</p>
        <p><strong>잠시 후 다시 시도해 주시기 바랍니다.</strong></p>
    </div>
    <img src="../resources/images/namsan_tower.png" style="width: 200px; opacity: 0.5;" alt="점검중">
    <br><br>
    <a href="../index.jsp" class="btn btn-outline-primary">메인 페이지로 돌아가기</a>
</div>
<jsp:include page="../common/footer.jsp" />
</body>
</html>