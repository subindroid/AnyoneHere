<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" />
    <title>회원 수정</title>
</head>
<body>

<%
String sessionId = (String) session.getAttribute("userId");
%>

<sql:setDataSource var="dataSource"
    url="jdbc:mysql://localhost:3306/AnyoneHereDB"
    driver="com.mysql.jdbc.Driver"
    user="root" password="1234" />

<sql:query dataSource="${dataSource}" var="resultSet">
    SELECT * FROM USERS WHERE U_ID=?
    <sql:param value="<%=sessionId%>" />
</sql:query>

<div class="container py-4">
    <jsp:include page="/WEB-INF/views/menu.jsp" />

    <div class="p-5 mb-4 bg-body-tertiary rounded-3">
        <div class="container-fluid py-5">
            <h1 class="display-5 fw-bold">회원 수정</h1>
            <p class="col-md-8 fs-4">Membership Updating</p>
        </div>
    </div>

    <c:forEach var="row" items="${resultSet.rows}">
        <c:set var="mail" value="${row.u_email}" />
        <c:set var="mail1" value="${fn:split(mail, '@')[0]}" />
        <c:set var="mail2" value="${fn:split(mail, '@')[1]}" />

        <c:set var="birth" value="${row.u_birth}" />
        <c:set var="year" value="${fn:split(birth, '/')[0]}" />
        <c:set var="monthRaw" value="${fn:split(birth, '/')[1]}" />
        <c:set var="dayRaw" value="${fn:split(birth, '/')[2]}" />
        <c:set var="month" value="${fn:length(monthRaw) == 1 ? '0' + monthRaw : monthRaw}" />
        <c:set var="day" value="${fn:length(dayRaw) == 1 ? '0' + dayRaw : dayRaw}" />

        <c:set var="genderFull" value="${row.u_gender}" />
        <c:set var="genderShort" value="${fn:substring(genderFull, 0, 1)}" />

        <form name="newMember" action="processUpdateMember.jsp" method="post" onsubmit="return checkForm()">
            <div class="mb-3 row">
                <label class="col-sm-2">아이디</label>
                <div class="col-sm-3">
                    <input name="id" type="text" class="form-control" value="${row.u_id}" readonly>
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-2">비밀번호</label>
                <div class="col-sm-3">
                    <input name="password" type="text" class="form-control" value="${row.u_password}">
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-2">비밀번호 확인</label>
                <div class="col-sm-3">
                    <input name="password_confirm" type="text" class="form-control">
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-2">성명</label>
                <div class="col-sm-3">
                    <input name="name" type="text" class="form-control" value="${row.u_name}">
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-2">성별</label>
                <div class="col-sm-3">
                    <input name="gender" type="radio" value="남" <c:if test="${genderShort == '남'}">checked</c:if>> 남
                    <input name="gender" type="radio" value="여" <c:if test="${genderShort == '여'}">checked</c:if>> 여
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-2">생일</label>
                <div class="col-sm-10">
                    <div class="row">
                        <div class="col-sm-2">
                            <input type="text" name="birthyy" maxlength="4" class="form-control" value="${year}">
                        </div>
                        <div class="col-sm-2">
                            <select name="birthmm" id="birthmm" class="form-select">
                                <option value="">월</option>
                                <c:forEach var="i" begin="1" end="12">
                                    <c:set var="val" value="${i lt 10 ? '0' + i : i}" />
                                    <option value="${val}">${i}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-sm-2">
                            <input type="text" name="birthdd" maxlength="2" class="form-control" value="${day}">
                        </div>
                    </div>
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-2">이메일</label>
                <div class="col-sm-10">
                    <div class="row">
                        <div class="col-sm-4">
                            <input type="text" name="mail1" value="${mail1}" class="form-control">
                        </div> @
                        <div class="col-sm-3">
                            <select name="mail2" id="mail2" class="form-select">
                                <option>naver.com</option>
                                <option>daum.net</option>
                                <option>gmail.com</option>
                                <option>nate.com</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-2">전화번호</label>
                <div class="col-sm-3">
                    <input name="phone" type="text" class="form-control" value="${row.u_phone}">
                </div>
            </div>
            <div class="mb-3 row">
                <label class="col-sm-2">주소</label>
                <div class="col-sm-5">
                    <input name="address" type="text" class="form-control" value="${row.u_address}">
                </div>
            </div>
            <div class="mb-3 row">
                <div class="col-sm-offset-2 col-sm-10">
                    <input type="submit" class="btn btn-primary" value="회원수정">
                    <a href="deleteMember.jsp" class="btn btn-danger">회원탈퇴</a>
                </div>
            </div>
        </form>
    </c:forEach>

    <jsp:include page="/WEB-INF/views/footer.jsp" />
</div>

<script>
document.addEventListener("DOMContentLoaded", function () {
    const mail2 = "${mail2}";
    const month = "${month}";

    const mailSelect = document.getElementById("mail2");
    if (mailSelect) {
        for (let i = 0; i < mailSelect.options.length; i++) {
            if (mailSelect.options[i].value === mail2) {
                mailSelect.options[i].selected = true;
                break;
            }
        }
    }

    const birthSelect = document.getElementById("birthmm");
    if (birthSelect) {
        for (let i = 0; i < birthSelect.options.length; i++) {
            if (birthSelect.options[i].value === month) {
                birthSelect.options[i].selected = true;
                break;
            }
        }
    }
});

function checkForm() {
    const form = document.newMember;
    if (!form.id.value) {
        alert("아이디를 입력하세요.");
        return false;
    }
    if (!form.password.value) {
        alert("비밀번호를 입력하세요.");
        return false;
    }
    if (form.password.value !== form.password_confirm.value) {
        alert("비밀번호를 동일하게 입력하세요.");
        return false;
    }
    return true;
}
</script>
</body>
</html>
