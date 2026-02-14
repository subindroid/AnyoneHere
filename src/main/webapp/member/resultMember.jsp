<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<link rel="stylesheet" href="../../../resources/css/bootstrap.min.css" />
<title>회원 정보</title>
</head>
<body>

<div class="container py-4">
   <jsp:include page="/WEB-INF/views/menu.jsp" />

 <div class="p-5 mb-4 bg-body-tertiary rounded-3">
      <div class="container-fluid py-5">
      <%
			String msg = request.getParameter("msg");
      		if (msg.equals("0")||msg.equals("2")||msg.equals("3")){
      %>
        <h1 class="display-5 fw-bold">회원 정보</h1>
        <p class="col-md-8 fs-4">Membership Info</p>    
        <% }
      		else if (msg.equals("1")){
        %>  
         <h1 class="display-5 fw-bold">회원 가입</h1>
        <p class="col-md-8 fs-4">Membership Joining</p>    
         <% }%>
      </div>
    </div>
	

	 <div class="row align-items-md-stretch   text-center">
		<%
			

			if (msg != null) {
				if (msg.equals("0")) //회원 수정
					out.println(" <h2 class='alert alert-danger'>회원정보가 수정되었습니다.</h2>");
				else if (msg.equals("1")) //회원 가입
					out.println(" <h2 class='alert alert-danger'>회원가입을 축하드립니다.</h2>");
				else if (msg.equals("2")) { //로그인
					String loginId = (String) session.getAttribute("userId");
					out.println(" <h2 class='alert alert-danger'>" + loginId + "님 환영합니다</h2>");
				}				
			   else if (msg.equals("3")) //가입 탈퇴
				out.println("<h2 class='alert alert-danger'>회원정보가 삭제되었습니다.</h2>");
			}
		%>
	</div>	
	<%@ include file="../footer.jsp"%>
</div>	
</body>
</html>