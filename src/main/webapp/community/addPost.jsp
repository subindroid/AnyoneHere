<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>게시글 작성</title>
    <link rel="stylesheet" href="../resources/css/bootstrap.min.css"/>
    <script src="../resources/js/validationPost.js"></script>
</head>
<body>
<div class="container py-4">
    <jsp:include page="../common/menu.jsp"/>

    <%
        String loginUserId = (String) session.getAttribute("userId");
        if (loginUserId == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
            return;
        }
        String csrfToken = util.CsrfUtil.getOrCreateToken(session);
    %>

    <div class="row justify-content-center">
        <div class="col-lg-8">
            <h4 class="mb-4">게시글 작성</h4>

            <% if ("empty".equals(request.getParameter("error"))) { %>
            <div class="alert alert-warning">제목과 내용을 입력해주세요.</div>
            <% } %>

            <form action="<%= request.getContextPath() %>/processAddPost" method="post" enctype="multipart/form-data" onsubmit="return validatePostForm()">

                <!-- 카테고리 -->
                <div class="mb-3">
                    <label class="form-label fw-bold">카테고리</label>
                    <div class="d-flex gap-3">
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="category" value="FREE" id="catFree" checked>
                            <label class="form-check-label" for="catFree">자유</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="category" value="REVIEW" id="catReview">
                            <label class="form-check-label" for="catReview">드라이브 후기</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="category" value="QUESTION" id="catQuestion">
                            <label class="form-check-label" for="catQuestion">질문</label>
                        </div>
                        <div class="form-check">
                            <input class="form-check-input" type="radio" name="category" value="RECOMMEND" id="catRecommend">
                            <label class="form-check-label" for="catRecommend">추천</label>
                        </div>
                    </div>
                </div>

                <!-- 제목 -->
                <div class="mb-3">
                    <label for="title" class="form-label fw-bold">제목</label>
                    <input type="text" class="form-control" id="title" name="title"
                           maxlength="100" placeholder="제목을 입력하세요" required>
                </div>

                <!-- 내용 -->
                <div class="mb-3">
                    <label for="content" class="form-label fw-bold">내용</label>
                    <textarea class="form-control" id="content" name="content"
                              rows="12" placeholder="내용을 입력하세요" required></textarea>
                </div>

                <!-- 이미지 첨부 -->
                <div class="mb-4">
                    <label for="postImages" class="form-label fw-bold">이미지 첨부 <small class="text-muted fw-normal">(jpg, jpeg, png, gif, webp / 최대 5MB / 여러 장 선택 가능)</small></label>
                    <input type="file" class="form-control" id="postImages" name="postImages"
                           multiple accept="image/jpeg,image/png,image/gif,image/webp">
                    <div id="previewArea" class="d-flex flex-wrap gap-2 mt-2"></div>
                </div>

                <input type="hidden" name="_csrf" value="<%= csrfToken %>">
                <div class="d-flex gap-2">
                    <button type="submit" class="btn btn-primary">등록</button>
                    <a href="board.jsp" class="btn btn-secondary">취소</a>
                </div>
            </form>
        </div>
    </div>

    <jsp:include page="../common/footer.jsp"/>
</div>
<script>
document.getElementById('postImages').addEventListener('change', function () {
    const preview = document.getElementById('previewArea');
    preview.innerHTML = '';
    for (const file of this.files) {
        const reader = new FileReader();
        reader.onload = e => {
            const img = document.createElement('img');
            img.src = e.target.result;
            img.style.cssText = 'height:100px; border-radius:4px; border:1px solid #ddd;';
            preview.appendChild(img);
        };
        reader.readAsDataURL(file);
    }
});
</script>
</body>
</html>
