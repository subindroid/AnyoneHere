<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <link rel="stylesheet" href="../resources/css/bootstrap.min.css"/>
    <title>회원 탈퇴</title>
</head>
<body>
<%
    String sessionId = (String) session.getAttribute("userId");
    if (sessionId == null) {
        response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
        return;
    }
    String csrfToken = util.CsrfUtil.getOrCreateToken(session);
%>
<%@ include file="../common/menu.jsp" %>

<div class="container mt-5" style="max-width: 500px;">
    <h3 class="mb-4">회원 탈퇴</h3>
    <div class="alert alert-danger">
        탈퇴하면 모든 데이터가 삭제되며 복구할 수 없습니다. 정말 탈퇴하시겠습니까?
    </div>
    <form action="<%= request.getContextPath() %>/processDeleteMember" method="post">
        <input type="hidden" name="_csrf" value="<%= csrfToken %>">
        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-danger">탈퇴하기</button>
            <a href="<%= request.getContextPath() %>/member/updateMember.jsp" class="btn btn-secondary">취소</a>
        </div>
    </form>
</div>

<%@ include file="../common/footer.jsp" %>
</body>
</html>
