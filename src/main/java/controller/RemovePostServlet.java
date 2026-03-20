package controller;

import dao.PostImageRepository;
import dao.PostRepository;
import dto.PostImage;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@WebServlet("/processRemovePost")
public class RemovePostServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        // 이미지 파일 정리 (DB 삭제 전 경로 수집)
        String uploadDir = getServletContext().getRealPath("/resources/images");
        if (uploadDir != null) {
            List<PostImage> images = PostImageRepository.getImagesByPostId(postId);
            for (PostImage img : images) {
                if (img.getImagePath() != null && !img.getImagePath().isEmpty()) {
                    try {
                        Files.deleteIfExists(Paths.get(uploadDir, img.getImagePath()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        PostRepository.delete(postId, userId);
        response.sendRedirect(request.getContextPath() + "/community/board.jsp");
    }
}
