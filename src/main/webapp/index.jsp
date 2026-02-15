<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Date"%>
<html>
<head>
<link rel="stylesheet" href="resources/css/bootstrap.min.css" />
<title>Welcome</title>
</head>
<body>
	<div class="container py-4">
		<%@ include file="common/menu.jsp"%>

		<%!String greeting = "차쟁이";
		String tagline = "Anyone Here?";
		String description = "차쟁이들을 위한 공간!";%>


		<div class="p-5 mb-4 bg-body-tertiary rounded-3">
			<div class="container-fluid py-5">
				<h1 class="display-5 fw-bold"><%=greeting%></h1>
				<p class="col-md-8 fs-4">AnyoneHere</p>
			</div>
		</div>

		<div class="row align-items-md-stretch   text-center">
			<div class="col-md-12">
				<div class="h-100 p-5">
					<h3><%=tagline%></h3>
					<p>
					<h5><%=description%></h5>

					<%
          	response.setIntHeader("Refresh", 5);
          	Date day = new java.util.Date();
          	String am_pm;
          	int hour = day.getHours();
          	int minute = day.getMinutes();
          	int second = day.getSeconds();
          	if (hour / 12 == 0) {
          		am_pm = "AM";
          	} else {
          		am_pm = "PM";
          		hour = hour - 12;
          	}
          	String CT = hour + ":" + minute + ":" + second + " " + am_pm;
          	out.println("현재 접속 시각: " + CT + "\n");
          %>
				</div>
			</div>
		</div>
		<div class="row text-center">

			<div class="col-md-4">
				<div class="p-5 mb-4 bg-body-tertiary rounded-3">
					<h5>
						<b>주변 스팟 보기</b>
					</h5>
				</div>
			</div>

			<div class="col-md-4">
				<div class="p-5 mb-4 bg-body-tertiary rounded-3">
					<h5>
						<b>스팟 등록 요청</b>
					</h5>
				</div>
			</div>
		</div>
		<%@ include file="common/footer.jsp"%>
	</div>
</body>
</html>
