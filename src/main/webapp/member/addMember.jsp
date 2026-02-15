<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>

<head>
    <link rel="stylesheet" href="../resources/css/bootstrap.min.css" />
    <title>회원 가입</title>
	<script src="../resources/js/validation.js"></script>
</head>
<body>

<div class="container py-4">
    <jsp:include page="../common/menu.jsp" />

    <div class="p-5 mb-4 bg-body-tertiary rounded-3">
        <div class="container-fluid py-5">
            <h1 class="display-5 fw-bold">회원 가입</h1>
            <p class="col-md-8 fs-4">Membership Joining</p>
        </div>
    </div>

    <!-- 회원가입 폼 -->
    <form name="newMember" action="processAddMember.jsp" method="post" onsubmit="return validateSignInForm()">
        <!-- 아이디, 비밀번호, 비밀번호 확인, 이름 등 기존 폼은 그대로 유지 -->
        <div class="mb-3 row">
            <label class="col-sm-2">아이디</label>
            <div class="col-sm-3">
                <input name="id" type="text" class="form-control" placeholder="id">
            </div>
        </div>
        <div class="mb-3 row">
            <label class="col-sm-2">비밀번호</label>
            <div class="col-sm-3">
                <input name="password" type="password" class="form-control" placeholder="password">
            </div>
        </div>
        <div class="mb-3 row">
            <label class="col-sm-2">비밀번호확인</label>
            <div class="col-sm-3">
                <input name="password_confirm" type="text" class="form-control" placeholder="password confirm">
            </div>
        </div>
        <div class="mb-3 row">
            <label class="col-sm-2">성명</label>
            <div class="col-sm-3">
                <input name="name" type="text" class="form-control" placeholder="name">
            </div>
        </div>

			<div class="mb-3 row">
				<label class="col-sm-2">성별</label>
				<div class="col-sm-2">
					<input name="gender" type="radio" value="남" /> 남 
					<input name="gender" type="radio" value="여" /> 여
				</div>
			</div>
			
			<div class="mb-3 row">
				<label class="col-sm-2">생일</label>
				<div class="col-sm-10  ">
				  <div class="row">
				  	<div class="col-sm-2">
						<input type="text" name="birthyy" maxlength="4"  class="form-control" placeholder="년(4자)" size="6"> 
					</div>
					<div class="col-sm-2">
					<select name="birthmm" class="form-select">
						<option value="">월</option>
						<option value="01">1</option>
						<option value="02">2</option>
						<option value="03">3</option>
						<option value="04">4</option>
						<option value="05">5</option>
						<option value="06">6</option>
						<option value="07">7</option>
						<option value="08">8</option>
						<option value="09">9</option>
						<option value="10">10</option>
						<option value="11">11</option>
						<option value="12">12</option>
					</select> 
					</div>
					<div class="col-sm-2">
					<input type="text" name="birthdd" maxlength="2" class="form-control" placeholder="일" size="4">
					</div>
				</div>
				</div>
			</div>
			
		<div class="mb-3 row">
			<label class="col-sm-2">이메일</label>
				<div class="col-sm-10">
				  <div class="row">
					<div class="col-sm-4">
						<input type="text" name="mail1" maxlength="50" class="form-control"  placeholder="email">
					</div> @
					<div class="col-sm-3">
						 <select name="mail2_select" id="mailSelect" class="form-select">
							<option value="naver.com">naver.com</option>
							<option value="daum.net">daum.net</option>
							<option value="gmail.com">gmail.com</option>
							<option value="nate.com">nate.com</option>
							 <option value="custom">직접입력</option>
						</select>
					</div>
					  <div class="col-sm-3">
					  	<input type="text" name="mail2" id="customMail" maxlength="50" class="form-control"  placeholder="입력" style= "display:none;">
					  </div>
					  </div>
				</div>		
			</div>		
			<div class="mb-3 row">
				<label class="col-sm-2">전화번호</label>
				<div class="col-sm-3">
					<input name="phone" type="text" class="form-control" placeholder="phone" >

				</div>
			</div>

        <!-- 주소 입력란과 위경도 변환 버튼 추가 -->
        <div class="mb-3 row">
            <label class="col-sm-2">주소</label>
            <div class="col-sm-5">
                <input name="address" id="address" type="text" class="form-control" placeholder="도로명 주소 입력">
            </div>
        </div>

        <!-- 회원가입 제출 버튼 -->
        <div class="mb-3 row">
            <div class="col-sm-offset-2 col-sm-10">
                <input type="submit" class="btn btn-primary" value="등록">
                <input type="reset" class="btn btn-primary" value="취소">
            </div>
        </div>
    </form>

    <jsp:include page="../common/footer.jsp" />
</div>
<script>
	document.getElementById("mailSelect").addEventListener("change", function() {
		const customInput = document.getElementById("customMail");

		if (this.value === "custom") {
			customInput.style.display = "block";
		} else {
			customInput.style.display = "none";
			customInput.value = ""; // 기존 값 초기화
		}
	});
</script>
</body>
</html>
