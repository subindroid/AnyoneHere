package controller;

import dao.PostCommentRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/processRemoveComment")
public class RemoveCommentServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String userId = (session != null) ? (String) session.getAttribute("userId") : null;

        if (userId == null) {
            response.sendRedirect(request.getContextPath() + "/member/loginMember.jsp");
            return;
        }

        String commentIdStr = request.getParameter("commentId");
        String postIdStr    = request.getParameter("postId");

        int commentId, postId;
        try {
            commentId = Integer.parseInt(commentIdStr);
            postId    = Integer.parseInt(postIdStr);
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/community/board.jsp");
            return;
        }

        PostCommentRepository.delete(commentId, userId);
        response.sendRedirect(request.getContextPath() + "/community/post.jsp?postId=" + postId + "#comments");
    }
}
