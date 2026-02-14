<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>

<sql:setDataSource var="dataSource"
    driver="com.mysql.jdbc.Driver"
    url="jdbc:mysql://localhost:3306/AnyoneHereDB"
    user="root"
    password="1234" />

<c:set var="id" value="${param.id}" />
<c:set var="password" value="${param.password}" />

<sql:query dataSource="${dataSource}" var="resultSet">
    SELECT * FROM USERS WHERE USER_ID = ? AND USER_PASSWORD = ?
    <sql:param value="${id}" />
    <sql:param value="${password}" />
</sql:query>

<c:choose>
    <c:when test="${not empty resultSet.rows}">
    
    	<!-- 로그인 처리 시 세션에 유저아이디 저장 -->
        <c:set var="userId" value="${param.id}" scope="session" />
        <!-- 세션에 set! -->
        
        <c:redirect url="resultMember.jsp?msg=2" />
    </c:when>
    <c:otherwise>
        <c:redirect url="loginMember.jsp?error=true" />
    </c:otherwise>
</c:choose>

