<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%><html>
<head>
    <title>장소 등록 요청</title>
</head>
<body>
<%

  String userId = (String) session.getAttribute("userId");
  String spotId = request.getParameter("spotId");
  if (userId == null) {
%>

<script>
  alert("로그인이 필요합니다!");
  location.href = "../member/loginMember.jsp";
</script>
<%
    return;
  }
%>
<fmt:setLocale value="${param.language}" />
<fmt:bundle basename="bundle.message">
  <div class="container py-4">
    <%@ include file="../menu.jsp"%>

    <div class="p-5 mb-4 bg-body-tertiary rounded-3">
      <div class="container-fluid py-5">
        <h1 class="display-5 fw-bold">
          <fmt:message key="writeReview" />
        </h1>
        <p class="col-md-8 fs-4">장소 추가 신청</p>
      </div>
    </div>

    <div class="row align-items-md-stretch">
      <div class="text-end mb-3">
        <a href="?language=ko">Korean</a> | <a href="?language=en">English</a>
        <a href="../member/logoutMember.jsp" class="btn btn-sm btn-success ms-3">logout</a>
      </div>

      <!-- 리뷰 작성 폼 -->
      <form name="newReview" action="processSpotAddApplication.jsp" method="post" class="form-horizontal">

        <div class="mb-3 row">
          <label class="col-sm-2 col-form-label">장소명</label>
          <div class="col-sm-5">
                     <textarea name="content" rows="1" class="form-control"></textarea>
          </div>
        </div>
        <div class="mb-3 row">
          <label class="col-sm-2 col-form-label">장소 위치</label>
          <div class="col-sm-5">
                     <textarea name="content" rows="5" class="form-control"
                               placeholder="지도에서 찝은 후 그 주소 입력됨"></textarea>
          </div>
        </div>
        <div class="mb-3 row">
          <label class="col-sm-2 col-form-label">장소 설명</label>
          <div class="col-sm-5">
                     <textarea name="content" rows="5" class="form-control"
                               placeholder="추가할 장소의 상세 설명"></textarea>
          </div>
        </div>

        <div class="mb-3 row">
          <div class="offset-sm-2 col-sm-10">
            <input type="submit" class="btn btn-primary" value="제출하기">
          </div>
        </div>

      </form>
    </div>

    <%@ include file="../footer.jsp"%>
  </div>
</fmt:bundle>
</body>
</html>