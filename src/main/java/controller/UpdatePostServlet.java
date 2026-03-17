package controller;

import dao.PostImageRepository;
import dao.PostRepository;
import dto.Post;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import util.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebServlet("/processUpdatePost")
@MultipartConfig(maxFileSize = 5 * 1024 * 1024)
public class UpdatePostServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
            return;
        }

        String postIdStr = request.getParameter("postId");
        int postId;
        try {
            postId = Integer.parseInt(postIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/community/board.jsp");
            return;
        }

        String category = request.getParameter("category");
        String title    = request.getParameter("title");
        String content  = request.getParameter("content");

        if (title == null || title.isBlank() || content == null || content.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/community/editPost.jsp?postId=" + postId + "&error=empty");
            return;
        }
        if (title.length() > 100) {
            response.sendRedirect(request.getContextPath() + "/community/editPost.jsp?postId=" + postId + "&error=toolong");
            return;
        }
        if (content.length() > 5000) {
            response.sendRedirect(request.getContextPath() + "/community/editPost.jsp?postId=" + postId + "&error=toolong");
            return;
        }

        Post post = new Post();
        post.setPostId(postId);
        post.setUserId(userId);  // Repository에서 userId로 본인 글 검증
        post.setCategory(category != null ? category : "FREE");
        post.setTitle(title.trim());
        post.setContent(content.trim());
        PostRepository.update(post);

        // 선택된 이미지 삭제
        String[] deleteIds = request.getParameterValues("deleteImageIds");
        if (deleteIds != null) {
            String uploadDir = getServletContext().getRealPath("/resources/images");
            if (uploadDir == null) {
                response.sendRedirect(request.getContextPath() + "/community/post.jsp?postId=" + postId);
                return;
            }
            for (String idStr : deleteIds) {
                try {
                    int imageId = Integer.parseInt(idStr);
                    String path = PostImageRepository.getImagePath(imageId);
                    PostImageRepository.deleteImage(imageId, postId);
                    if (path != null && !path.isEmpty()) {
                        java.nio.file.Files.deleteIfExists(
                                java.nio.file.Paths.get(uploadDir, path));
                    }
                } catch (Exception ignored) {}
            }
        }

        // 새 이미지 추가
        String uploadDir = getServletContext().getRealPath("/resources/images");
        if (uploadDir == null) {
            response.sendRedirect(request.getContextPath() + "/community/post.jsp?postId=" + postId);
            return;
        }
        List<String> newImages = new ArrayList<>();
        Collection<Part> parts = request.getParts();
        for (Part part : parts) {
            if ("postImages".equals(part.getName()) && part.getSize() > 0) {
                try {
                    newImages.add(FileUtil.saveImage(part, uploadDir));
                } catch (IllegalArgumentException ignored) {} catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if (!newImages.isEmpty()) {
            PostImageRepository.insertImages(postId, newImages);
        }

        response.sendRedirect(request.getContextPath() + "/community/post.jsp?postId=" + postId);
    }
}
