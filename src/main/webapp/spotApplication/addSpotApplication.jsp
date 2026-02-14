<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>HNDY</title>
  <link rel="stylesheet" href="../../../resources/css/bootstrap.min.css" />
	<script src="resources/js/validationApplicationSpot.js"></script>
</head>
<body>
<%
	String userId = (String) session.getAttribute("userId");
	if (userId == null) {
	%>
	<script>
		alert("로그인을 해야 상품 등록을 할 수 있습니다!");
		location.href = "../member/loginMember.jsp";
	</script>
	<% 
		return;
	}
%>
  <fmt:setLocale value='<%=request.getParameter("language")%>' />
  <fmt:bundle basename="bundle.message">
    <div class="container py-4">
      <%@ include file="../menu.jsp" %>

      <div class="p-5 mb-4 bg-body-tertiary rounded-3">
        <div class="container-fluid py-5">
          <h1 class="display-5 fw-bold">
            <fmt:message key="add_spot_application_title" />
          </h1>
          <p class="col-md-8 fs-4">Spot Addition</p>
        </div>
      </div>

      <div class="row align-items-md-stretch">
        <div class="text-end">
          <a href="?language=ko">Korean</a> | <a href="?language=en">English</a>
          <a href="../member/logoutMember.jsp" class="btn btn-sm btn-success pull right">logout</a>
        </div>

        <form name="newApplicationSpot" action="processAddSpot.jsp" method="post"
              class="form-horizontal" enctype="multipart/form-data">

          <div class="mb-3 row">
            <label class="col-sm-2"><fmt:message key="spotName" /></label>
            <div class="col-sm-3">
              <input type="text" id="spotName" name="spotName" class="form-control">
            </div>
          </div>

          <div class="mb-3 row">
            <label class="col-sm-2"><fmt:message key="unitPrice" /></label>
            <div class="col-sm-3">
              <input type="text" id="unitPrice" name="unitPrice" class="form-control">
            </div>
          </div>
          
          <div class="mb-3 row">
            <label class="col-sm-2"><fmt:message key="brand" /></label>
            <div class="col-sm-3">
              <input type="text" id="brand" name="brand" class="form-control">
            </div>
          </div>

          <div class="mb-3 row">
            <label class="col-sm-2"><fmt:message key="spotDescription" /></label>
            <div class="col-sm-3">
              <textarea name="product_description" id="product_description" cols="10" rows="2"
                        class="form-control" placeholder="10자 이상 적어주세요"></textarea>
            </div>
          </div>

          <div class="mb-3 row">
            <label class="col-sm-2"><fmt:message key="category" /></label>
            <div class="col-sm-12">
              <input type="radio" name="category" value="tops"> <fmt:message key="tops" />
              <input type="radio" name="category" value="bottoms"> <fmt:message key="bottoms" />
              <input type="radio" name="category" value="outer"> <fmt:message key="outer" />
              <input type="radio" name="category" value="fashion_etc"> <fmt:message key="fashion_etc" />
              
            </div>
          </div>

          <div class="mb-3 row">
            <label class="col-sm-2"><fmt:message key="condition" /></label>
            <div class="col-sm-5">
              <input type="radio" name="condition" value="New"> <fmt:message key="condition_new" />
              <input type="radio" name="condition" value="lightly_used"> <fmt:message key="condition_used" />
              <input type="radio" name="condition" value="heavily_used"> <fmt:message key="condition_old" />
            </div>
          </div>

          <div class="mb-3 row">
            <label class="col-sm-2 col-form-label"><fmt:message key="productImage" /></label>
            <div class="col-sm-5">
              <input type="file" name="productImage" id="productImage" class="form-control">
            </div>
          </div>

          <div class="mb-3 row">
            <div class="col-sm-offset-2 col-sm-10">
              <input type="submit" class="btn btn-primary"
                     value="<fmt:message key='button' />"
                     onclick="CheckAddProduct()">
            </div>
          </div>
        </form>
      </div>
    </div>
  </fmt:bundle>
<jsp:include page="../footer.jsp" />
</body>
</html>

