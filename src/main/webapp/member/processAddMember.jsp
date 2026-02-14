<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>

<%
    request.setCharacterEncoding("UTF-8");

    String id = request.getParameter("id");
    String password = request.getParameter("password");
    String name = request.getParameter("name");
    String gender = request.getParameter("gender");  // 폼에 추가 필요
    String year = request.getParameter("birthyy");
    String month = request.getParameter("birthmm");
    String day = request.getParameter("birthdd");
    String birth = String.format("%s-%02d-%02d", year, Integer.parseInt(month), Integer.parseInt(day));

    String mail1 = request.getParameter("mail1");     // 폼에 추가 필요
    String mail2 = request.getParameter("mail2");
    String mail = mail1 + "@" + mail2;

    String phone = request.getParameter("phone");     // 폼에 추가 필요
    String address = request.getParameter("address");
    String latitude = request.getParameter("latitude");
    String longitude = request.getParameter("longitude");

    java.sql.Timestamp joinDate = new java.sql.Timestamp(System.currentTimeMillis());
    System.out.println("latitude: " + latitude);
%>

<sql:setDataSource var="dataSource"
    url="jdbc:mysql://localhost:3306/AnyoneHereDB"
    driver="com.mysql.jdbc.Driver"
    user="root"
    password="1234" />

<sql:update dataSource="${dataSource}" var="resultSet">
    INSERT INTO users (
        user_id, user_password, user_name, user_gender, user_birth, user_email,
        user_phone, user_address, created_at
    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)

    <sql:param value="<%=id%>" />
    <sql:param value="<%=password%>" />
    <sql:param value="<%=name%>" />
    <sql:param value="<%=gender%>" />
    <sql:param value="<%=birth%>" />
    <sql:param value="<%=mail%>" />
    <sql:param value="<%=phone%>" />
    <sql:param value="<%=address%>" />
    <sql:param value="<%=joinDate%>" />
	<sql:param value="<%=Double.valueOf(latitude)%>" />
	<sql:param value="<%=Double.valueOf(longitude)%>" />
</sql:update>

<c:if test="${resultSet >= 1}">
    <c:redirect url="resultMember.jsp?msg=1" />
</c:if>
<c:if test="${resultSet < 1}">
    <c:redirect url="resultMember.jsp?msg=0" />
</c:if>
