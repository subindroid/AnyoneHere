package controller;

import dao.PostCommentRepository;
import dto.PostComment;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/processAddComment")
public class AddCommentServlet extends HttpServlet {

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
        String content   = request.getParameter("content");

        int postId;
        try {
            postId = Integer.parseInt(postIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/community/board.jsp");
            return;
        }

        if (content == null || content.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/community/post.jsp?postId=" + postId);
            return;
        }
        if (content.length() > 1000) {
            response.sendRedirect(request.getContextPath() + "/community/post.jsp?postId=" + postId + "&error=toolong");
            return;
        }

        PostComment comment = new PostComment();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setContent(content.trim());

        PostCommentRepository.insert(comment);
        response.sendRedirect(request.getContextPath() + "/community/post.jsp?postId=" + postId + "#comments");
    }
}
